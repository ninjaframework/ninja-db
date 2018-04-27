/**
 * Copyright (C) 2017-2018 the original author or authors.
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

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.List;
import java.util.stream.Collectors;

public class NinjaDbHikariProvider implements Provider<NinjaDatasources> {

    private final NinjaDatasourceConfigs ninjaDatasourceConfigs;

    @Inject
    public NinjaDbHikariProvider(NinjaDatasourceConfigs ninjaDatasourceConfigs) {
        this.ninjaDatasourceConfigs = ninjaDatasourceConfigs;
    }

    @Singleton
    @Override
    public NinjaDatasources get() {

        List<NinjaDatasource> ninjaDatasources
                = ninjaDatasourceConfigs.getDatasources().stream().map(ninjaDatasourceConfig -> {

                    HikariConfig config = new HikariConfig();

                    config.setDriverClassName(ninjaDatasourceConfig.getDriver());
                    config.setJdbcUrl(ninjaDatasourceConfig.getJdbcUrl());
                    config.setUsername(ninjaDatasourceConfig.getUsername());
                    config.setPassword(ninjaDatasourceConfig.getPassword());

                    HikariDataSource dataSource = new HikariDataSource(config);

                    NinjaDatasource ninjaDatasource = new NinjaDatasource(
                            ninjaDatasourceConfig.getName(),
                            dataSource);

                    return ninjaDatasource;

                }).collect(Collectors.toList());

        return new NinjaDatasourcesImpl(ninjaDatasources);

    }

}
