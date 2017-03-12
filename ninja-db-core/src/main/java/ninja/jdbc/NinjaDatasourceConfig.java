package ninja.jdbc;

public class NinjaDatasourceConfig {

    public String name;
    public String driver;
    
    public String jdbcUrl;
    public String username;
    public String password;
    
    public boolean migrationEnabled;
    public String migrationUsername;
    public String migrationPassword;
}
