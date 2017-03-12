package ninja.flyway;

import com.google.inject.AbstractModule;

public class NinjaFlyway extends AbstractModule {

    @Override
    protected void configure() {
        // Important: Bind as eager Singleton to run migrations before
        // everything else...
        bind(NinjaFlywayMigrator.class).asEagerSingleton();
    }

}
