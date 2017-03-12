package ninja.jdbi;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ninja.jdbc.NinjaDatasource;
import ninja.jdbc.NinjaDatasources;
import org.skife.jdbi.v2.DBI;

public class NinjaJdbiImpl implements NinjaJdbi {

    final Map<String, DBI> map;

    public NinjaJdbiImpl(NinjaDatasources ninjaDatasources) {

        Map<String, DBI> map = new HashMap<>();

        for (NinjaDatasource ninjaDatasource : ninjaDatasources.getDatasources()) {
            DBI dbi = new DBI(ninjaDatasource.getDataSource());
            map.put(ninjaDatasource.getName(), dbi);
        }

        this.map = ImmutableMap.copyOf(map);
    }

    @Override
    public DBI getDbi(String datasourceName) {
        return map.get(datasourceName);
    }

}
