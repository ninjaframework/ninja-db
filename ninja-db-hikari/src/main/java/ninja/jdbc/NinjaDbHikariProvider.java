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

                    config.setDriverClassName(ninjaDatasourceConfig.driver);
                    config.setJdbcUrl(ninjaDatasourceConfig.jdbcUrl);
                    config.setUsername(ninjaDatasourceConfig.username);
                    config.setPassword(ninjaDatasourceConfig.password);

                    HikariDataSource dataSource = new HikariDataSource(config);

                    NinjaDatasource ninjaDatasource = new NinjaDatasource(
                            ninjaDatasourceConfig.name,
                            dataSource);

                    return ninjaDatasource;

                }).collect(Collectors.toList());

        return new NinjaDatasourcesImpl(ninjaDatasources);

    }

}
