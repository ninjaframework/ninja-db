package services;

import com.google.inject.Inject;
import java.util.List;
import models.Guestbook;
import ninja.jdbi.NinjaJdbi;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.IDBI;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public class GuestbooksService {
    
    interface DbServiceInterface {  
        @SqlQuery("SELECT id, email, content FROM guestbooks")
        @Mapper(Guestbook.GuestbookMapper.class)
        List<Guestbook> listGuestBookEntries();
        
        @SqlUpdate("INSERT INTO guestbooks (email, content) VALUES (:email, :content)")
        void createGuestbook(@BindBean Guestbook guestbook);
    }

    private final DBI dbi;
            
    @Inject     
    public GuestbooksService(NinjaJdbi ninjaJdbi) {
        this.dbi = ninjaJdbi.getDbi("default");
    }

    public List<Guestbook> listGuestBookEntries() {
        return dbi.open().attach(DbServiceInterface.class).listGuestBookEntries();
    }

    public void createGuestbook(Guestbook guestbook) {
        dbi.open().attach(DbServiceInterface.class).createGuestbook(guestbook);
    }
    
}
