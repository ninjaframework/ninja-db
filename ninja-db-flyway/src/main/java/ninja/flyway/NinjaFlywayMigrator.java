package ninja.flyway;

import com.google.inject.Inject;
import ninja.jdbc.NinjaDatasourceConfig;
import org.flywaydb.core.Flyway;
import ninja.jdbc.NinjaDatasourceConfigs;

public class NinjaFlywayMigrator {

    @Inject
    public NinjaFlywayMigrator(NinjaDatasourceConfigs ninjaDatasourceConfigs) {

        for (NinjaDatasourceConfig datasourceConfig : ninjaDatasourceConfigs.getDatasources()) {
            if (datasourceConfig.migrationEnabled) {
                Flyway flyway = new Flyway();
                flyway.setDataSource(datasourceConfig.jdbcUrl, datasourceConfig.migrationUsername, datasourceConfig.migrationPassword);
                flyway.setLocations("classpath:db/" + datasourceConfig.name + "/migration");
                flyway.migrate();
            }
        }
    }

}
