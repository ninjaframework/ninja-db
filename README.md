     _______  .___ _______        ____.  _____   
     \      \ |   |\      \      |    | /  _  \  
     /   |   \|   |/   |   \     |    |/  /_\  \
    /    |    \   /    |    \/\__|    /    |    \
    \____|__  /___\____|__  /\________\____|__  /
         web\/framework   \/                  \/



Finest relational database support for Ninja
============================================

- Continuous integration: [![Build Status](https://api.travis-ci.org/ninjaframework/ninja-db.svg)](https://travis-ci.org/ninjaframework/ninja-db)

This project allows Ninja applications to access relational databases.

It is based on three building blocks:

- Migration support via the popular flyway library
- Fast jdbc access via Hikari connection pool
- Support for many jdbc libraries like jdbi and many more


Quickstart with JDBI
====================

Check out ninja-db-jdbi-demo for a working configuration.
If you want to add everything manually do the following:

Add the following dependencies for migrations and access via jdbi.

<pre>
&lt;dependency&gt;
    &lt;groupId&gt;org.ninjaframework&lt;/groupId&gt;
    &lt;artifactId&gt;ninja-db-jdbi&lt;/artifactId&gt;
    &lt;version&gt;NINJA_DB_VERSION&lt;/version&gt;
&lt;/dependency&gt;

&lt;dependency&gt;
    &lt;groupId&gt;org.ninjaframework&lt;/groupId&gt;
    &lt;artifactId&gt;ninja-db-flyway&lt;/artifactId&gt;
    &lt;version&gt;NINJA_DB_VERSION&lt;/version&gt;
&lt;/dependency&gt;

&lt;dependency&gt;
    &lt;groupId&gt;com.h2database&lt;/groupId&gt;
    &lt;artifactId&gt;h2&lt;/artifactId&gt;
    &lt;version&gt;1.4.199&lt;/version&gt;
&lt;/dependency&gt;
</pre>

Note: Instead of h2database you can of course any other database you like (postgresql etc).


Then activate the dependencies in your Module.java file:

<pre>
public class Module extends AbstractModule {

    protected void configure() {
        install(new NinjaFlyway());
        install(new NinjaJdbiModule());
    }

}
</pre>

Finally configure your datasource in the application.conf file:

<pre>
application.datasource.default.driver=org.h2.Driver
application.datasource.default.url=jdbc:h2:mem:test
application.datasource.default.username=sa
application.datasource.default.password=

application.datasource.default.migration.enabled=true
application.datasource.default.migration.username=sa
application.datasource.default.migration.password=

# Hikari can be configured on each datasource
# Parameters are extracted and provided to hikari
# Please refer to the hikari config for all parameters 
# https://github.com/brettwooldridge/HikariCP
application.datasource.default.hikari.idleTimeout=10000
application.datasource.default.hikari.maxLifetime=10000
application.datasource.default.hikari.maximumPoolSize=10
</pre>


Accessing the database is then simply a matter of injecting NinjaJdbi.
In case of JDBI a good way is having an interface with annotated SQL
queries and a service (GuestbooksService) that uses it and can itself
be injected into your controller.

The service:

<pre>
import com.google.inject.Inject;
import java.util.List;
import models.Guestbook;
import ninja.jdbi.NinjaJdbi;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.customizer.BindBean;

import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;


public class GuestbooksService {
    
    public interface DbServiceInterface {  
        @SqlQuery(&quot;SELECT id, email, content FROM guestbooks&quot;)
        @UseRowMapper(Guestbook.GuestbookMapper.class)
        List&lt;Guestbook&gt; listGuestBookEntries();
        
        @SqlUpdate(&quot;INSERT INTO guestbooks (email, content) VALUES (:email, :content)&quot;)
        void createGuestbook(@BindBean Guestbook guestbook);
    }

    private final Jdbi jdbi;
            
    @Inject     
    public GuestbooksService(NinjaJdbi ninjaJdbi) {
        this.jdbi = ninjaJdbi.getJdbi(&quot;default&quot;);
    }

    public List&lt;Guestbook&gt; listGuestBookEntries() {
        return jdbi.open().attach(DbServiceInterface.class).listGuestBookEntries();
    }

    public void createGuestbook(Guestbook guestbook) {
        jdbi.open().attach(DbServiceInterface.class).createGuestbook(guestbook);
    }
    
}

</pre>

and the Controller then'd look like:

<pre>
@Inject
public ApplicationController(Lang lang,
                             Logger logger,
                             GuestbooksService dbService) {
    this.lang = lang;
    this.logger = logger;
    this.dbService = dbService;

}

public Result index() {

    // Get all guestbookentries now:
    List&lt;Guestbook&gt; guestBookEntries = dbService.listGuestBookEntries();

    Map&lt;String, Object&gt; toRender = Maps.newHashMap();
    toRender.put(&quot;guestBookEntries&quot;, guestBookEntries);

    // Default rendering is simple by convention
    // This renders the page in views/ApplicationController/index.ftl.html
    return Results.html().render(toRender);

}
</pre>