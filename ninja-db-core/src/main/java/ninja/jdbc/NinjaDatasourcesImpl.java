package ninja.jdbc;

import com.google.common.collect.ImmutableList;
import java.util.List;


public class NinjaDatasourcesImpl implements NinjaDatasources {
    
    private final List<NinjaDatasource> ninjaDatasources;
    
    public NinjaDatasourcesImpl(List<NinjaDatasource> datasources) {
        this.ninjaDatasources = ImmutableList.copyOf(datasources);
    }

    @Override
    public List<NinjaDatasource> getDatasources() {
        return ninjaDatasources;
    }

    @Override
    public NinjaDatasource getDatasource(String name) {
        return ninjaDatasources
                .stream()
                .filter(n -> n.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Datasource " + name + " not found. Make sure it is configured in application.conf."));
    }
    
}
