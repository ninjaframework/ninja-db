/**
 * Copyright (C) 2017-2019 the original author or authors.
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
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.customizer.BindBean;

import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;


public class GuestbooksService {
    
    public interface DbServiceInterface {  
        @SqlQuery("SELECT id, email, content FROM guestbooks")
        @UseRowMapper(Guestbook.GuestbookMapper.class)
        List<Guestbook> listGuestBookEntries();
        
        @SqlUpdate("INSERT INTO guestbooks (email, content) VALUES (:email, :content)")
        void createGuestbook(@BindBean Guestbook guestbook);
    }

    private final Jdbi jdbi;
            
    @Inject     
    public GuestbooksService(NinjaJdbi ninjaJdbi) {
        this.jdbi = ninjaJdbi.getJdbi("default");
    }

    public List<Guestbook> listGuestBookEntries() {
        return jdbi.open().attach(DbServiceInterface.class).listGuestBookEntries();
    }

    public void createGuestbook(Guestbook guestbook) {
        jdbi.open().attach(DbServiceInterface.class).createGuestbook(guestbook);
    }
    
}
