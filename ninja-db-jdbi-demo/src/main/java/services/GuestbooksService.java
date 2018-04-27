/**
 * Copyright (C) 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
