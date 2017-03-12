
package ninja.jdbi;

import org.skife.jdbi.v2.DBI;


public interface NinjaJdbi {
    
    DBI getDbi(String datasourceName);

}
