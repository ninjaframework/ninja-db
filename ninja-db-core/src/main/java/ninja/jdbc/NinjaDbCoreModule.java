package ninja.jdbc;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class NinjaDbCoreModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(NinjaDatasourceConfigs.class).toProvider(NinjaDatasourceConfigProvider.class).in(Singleton.class);
    }

}
