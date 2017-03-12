package ninja.jdbc;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import ninja.utils.NinjaProperties;

public class NinjaDatasourceConfigProvider implements Provider<NinjaDatasourceConfigs> {

    private final String DATASOURCE_PREFIX = "application.datasource";
    private final String DATASOURCE_URL = "url";
    private final String DATASOURCE_USERNAME = "username";
    private final String DATASOURCE_PASSWORD = "password";
    private final String DATASOURCE_DRIVER = "driver";

    private final String DATASOURCE_MIGRATION_ENABLED = "migration.enabled";
    private final String DATASOURCE_MIGRATION_USERNAME = "migration.username";
    private final String DATASOURCE_MIGRATION_PASSWORD = "migration.password";
    
    private final NinjaProperties ninjaProperties;
    
    @Inject
    public NinjaDatasourceConfigProvider(NinjaProperties ninjaProperties) {
        this.ninjaProperties = ninjaProperties;
    }

    @Singleton
    @Override
    public NinjaDatasourceConfigs get() {
        List<NinjaDatasourceConfig> ninjaDatasourceConfigs = getDatasources(ninjaProperties);
        return new NinjaDatasourceConfigImpl(ninjaDatasourceConfigs);
       
    }

    private List<NinjaDatasourceConfig> getDatasources(NinjaProperties ninjaProperties) {
        Properties properties = ninjaProperties.getAllCurrentNinjaProperties();

        Set<String> datasourceNames = new HashSet<>();

        // filter datasources from application config
        for (Map.Entry<Object, Object> entrySet : properties.entrySet()) {
            if (((String) entrySet.getKey()).startsWith(DATASOURCE_PREFIX)
                    && ((String) entrySet.getKey()).endsWith(DATASOURCE_URL)) {

                String withoutPrefix = ((String) entrySet.getKey()).split(DATASOURCE_PREFIX + ".")[1];
                String datasourceName = withoutPrefix.split("." + DATASOURCE_URL)[0];

                datasourceNames.add(datasourceName);
            }
        }

        // assemble the datasources in a nice way, so we can build 
        // datasources from it
        List<NinjaDatasourceConfig> ninjaDatasources = Lists.newArrayList();
        for (String datasourceName : datasourceNames) {
            NinjaDatasourceConfig ninjaDatasource = new NinjaDatasourceConfig();
            ninjaDatasource.name = datasourceName;
            
            ninjaDatasource.driver = ninjaProperties.getWithDefault(
                    DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_DRIVER, "");
            
            // datasource
            ninjaDatasource.jdbcUrl = ninjaProperties.getWithDefault(
                    DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_URL, "");
            ninjaDatasource.username = ninjaProperties.getWithDefault(
                    DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_USERNAME, "");
            ninjaDatasource.password = ninjaProperties.getWithDefault(
                    DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_PASSWORD, "");

            // migrations => special username / password may be configured, but fall back to regular ones if not
            ninjaDatasource.migrationEnabled = ninjaProperties.getBooleanWithDefault(
                    DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_MIGRATION_ENABLED, Boolean.FALSE);
            ninjaDatasource.migrationUsername = ninjaProperties.getWithDefault(
                    DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_MIGRATION_USERNAME, ninjaDatasource.username);
            ninjaDatasource.migrationPassword = ninjaProperties.getWithDefault(
                    DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_MIGRATION_PASSWORD, ninjaDatasource.password);

            ninjaDatasources.add(ninjaDatasource);

        }

        return ninjaDatasources;

    }



}
