/**
 * Copyright (C) 2017-2019 the original author or authors.
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

package ninja.flyway;

import com.google.inject.Inject;
import ninja.jdbc.NinjaDatasourceConfig;
import ninja.jdbc.NinjaDatasourceConfigs;
import org.flywaydb.core.Flyway;

public class NinjaFlywayMigrator {

    @Inject
    public NinjaFlywayMigrator(NinjaDatasourceConfigs ninjaDatasourceConfigs) {

        for (NinjaDatasourceConfig datasourceConfig : ninjaDatasourceConfigs.getDatasources()) {
            datasourceConfig.getMigrationConguration().ifPresent(migrationConfiguration -> {
                Flyway flyway = new Flyway();
                flyway.setDataSource(datasourceConfig.getJdbcUrl(), migrationConfiguration.getMigrationUsername(), migrationConfiguration.getMigrationPassword());
                flyway.setLocations("classpath:migrations/" + datasourceConfig.getName() + "/");
                flyway.migrate();
            });
        }
    }

}
