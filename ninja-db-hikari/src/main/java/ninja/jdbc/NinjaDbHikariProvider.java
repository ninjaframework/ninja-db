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

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import ninja.lifecycle.Dispose;

public class NinjaDbHikariProvider implements Provider<NinjaDatasources> {

    private final NinjaDatasourceConfigs ninjaDatasourceConfigs;
    private List<NinjaDatasource> ninjaDatasources;

    @Inject
    public NinjaDbHikariProvider(NinjaDatasourceConfigs ninjaDatasourceConfigs) {
        this.ninjaDatasourceConfigs = ninjaDatasourceConfigs;
    }

    @Singleton
    @Override
    public NinjaDatasources get() {

        List<NinjaDatasource> ninjaDatasources
                = ninjaDatasourceConfigs.getDatasources().stream().map(ninjaDatasourceConfig -> {

                    Properties properties = extractHikariProperties(ninjaDatasourceConfig.getProperties());
                    HikariConfig config = new HikariConfig(properties);

                    config.setDriverClassName(ninjaDatasourceConfig.getDriver());
                    config.setJdbcUrl(ninjaDatasourceConfig.getJdbcUrl());
                    config.setUsername(ninjaDatasourceConfig.getUsername());
                    config.setPassword(ninjaDatasourceConfig.getPassword());
                    

                    HikariDataSource hikariDataSource = new HikariDataSource(config); 

                    NinjaDatasource ninjaDatasource = new NinjaDatasource(
                            ninjaDatasourceConfig.getName(),
                            hikariDataSource);

                    return ninjaDatasource;

                }).collect(Collectors.toList());
        
        this.ninjaDatasources = ninjaDatasources;

        return new NinjaDatasourcesImpl(ninjaDatasources);

    }
    
    // See all supported properties here: https://github.com/brettwooldridge/HikariCP
    private Properties extractHikariProperties(Map<String, String> allPropertiesOfThisDatasource) {
        
        Properties properties = new Properties();
        allPropertiesOfThisDatasource.forEach((key, value) -> {
            
            if (key.startsWith("hikari.")) {
            
                String newKey = key.split("hikari.")[1];
                properties.put(newKey, value);
            
            }

        });
        
        
        return properties;
    
    }
    
    @Dispose
    public void shutdown() {
        for (NinjaDatasource ninjaDatasource : ninjaDatasources) {
            HikariDataSource hikariDataSource = (HikariDataSource) ninjaDatasource.getDataSource();
            hikariDataSource.close();
        }
    }

}
