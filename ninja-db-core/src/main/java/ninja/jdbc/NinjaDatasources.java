package ninja.jdbc;

import java.util.List;


public interface NinjaDatasources {
    
    /**
     * Get all datasources configured for this Ninja application.
     * 
     * @return all datasources that are configured.
     */
    List<NinjaDatasource> getDatasources();
    
    /**
     * Get Datasource by name.
     * 
     * @param name Name of the datasource you want to get
     * @return The datasource. Or a RuntimeException if the datasource cannot
     *         be found.
     */
    NinjaDatasource getDatasource(String name);
    
}