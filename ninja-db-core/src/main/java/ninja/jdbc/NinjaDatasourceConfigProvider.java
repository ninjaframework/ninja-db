/**
 * Copyright (C) the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ninja.jdbc;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
            if (((String) entrySet.getKey()).startsWith(DATASOURCE_PREFIX) && ((String) entrySet.getKey()).endsWith(DATASOURCE_URL)) {
                String withoutPrefix = ((String) entrySet.getKey()).split(DATASOURCE_PREFIX + ".")[1];
                String datasourceName = withoutPrefix.split("." + DATASOURCE_URL)[0];
                datasourceNames.add(datasourceName);
            }
        }

        // assemble the datasources in a nice way, so we can build datasources from it
        List<NinjaDatasourceConfig> ninjaDatasources = Lists.newArrayList();
        for (String datasourceName : datasourceNames) {
            
            String name = datasourceName;
            String driver = ninjaProperties.getOrDie(DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_DRIVER);
            
            // datasource
            String jdbcUrl = ninjaProperties.getOrDie(DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_URL);
            String username = ninjaProperties.getOrDie(DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_USERNAME);
            String password = ninjaProperties.getOrDie(DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_PASSWORD);


            Optional<NinjaDatasourceConfig.MigrationConfiguration> migrationConfiguration = determineMigrationConfguration(datasourceName, username, password);

            Map<String, String> allProperties = getAllPropertiesOfThisDatasource(datasourceName, properties);
                    
            NinjaDatasourceConfig ninjaDatasource = new NinjaDatasourceConfig(
                    name,
                    driver,
                    jdbcUrl,
                    username,
                    password,
                    migrationConfiguration,
                    allProperties
            );
            ninjaDatasources.add(ninjaDatasource);
        }

        return ninjaDatasources;

    }
    
    private Optional<NinjaDatasourceConfig.MigrationConfiguration> determineMigrationConfguration(String datasourceName, String username, String password) {
        boolean migrationEnabled = ninjaProperties.getBooleanWithDefault(DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_MIGRATION_ENABLED, Boolean.FALSE);

        Optional<NinjaDatasourceConfig.MigrationConfiguration> migrationConfiguration;
        if (migrationEnabled) {
            // fallback to username/password configured in datasource
            String migrationUsername = ninjaProperties.getWithDefault(DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_MIGRATION_USERNAME, username);
            String migrationPassword = ninjaProperties.getWithDefault(DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_MIGRATION_PASSWORD, password);

            migrationConfiguration = Optional.of(new NinjaDatasourceConfig.MigrationConfiguration(migrationUsername, migrationPassword));
        } else {
            migrationConfiguration = Optional.empty();
        }
        
        return migrationConfiguration;
    }
    
    
    private Map<String, String> getAllPropertiesOfThisDatasource(String datasourceName, Properties properties) {
        
        Map<String, String> theseProperties = new HashMap();
        
        properties.entrySet().forEach(entry -> {
            String key = (String) entry.getKey();
            if (key.startsWith(DATASOURCE_PREFIX + "." + datasourceName)) {
                String keyWithoutDatasourceNamePrefix = key.split(DATASOURCE_PREFIX + "." + datasourceName + ".")[1];
                
                theseProperties.put(keyWithoutDatasourceNamePrefix, (String) entry.getValue());
            }
        });
        
        return ImmutableMap.copyOf(theseProperties);
    }

}
