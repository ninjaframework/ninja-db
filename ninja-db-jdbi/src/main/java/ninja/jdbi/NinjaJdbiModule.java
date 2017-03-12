
package ninja.jdbi;


import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import ninja.jdbc.NinjaDatasources;
import ninja.jdbc.NinjaDbHikariModule;
import ninja.utils.NinjaProperties;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.IDBI;


public class NinjaJdbiModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new NinjaDbHikariModule());
    }

    @Provides
    @Singleton
    public NinjaJdbi provideDBI(NinjaDatasources ninjaDatasources) {
        NinjaJdbiImpl ninjaJdbiImpl = new NinjaJdbiImpl(ninjaDatasources);
        return ninjaJdbiImpl;
    }

}
