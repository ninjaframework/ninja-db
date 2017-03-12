package ninja.jdbc;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class NinjaDbHikariModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new NinjaDbCoreModule());
        bind(NinjaDatasources.class).toProvider(NinjaDbHikariProvider.class).in(Singleton.class);
    }

}
