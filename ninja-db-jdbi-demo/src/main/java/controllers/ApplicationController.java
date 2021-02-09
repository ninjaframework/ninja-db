/**
 * Copyright (C) the original author or authors.
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

package controllers;

import java.util.List;
import java.util.Map;

import models.Guestbook;
import ninja.Result;
import ninja.Results;
import ninja.i18n.Lang;
import ninja.params.Param;

import org.slf4j.Logger;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import services.GuestbooksService;

@Singleton
public class ApplicationController {

    private Logger logger;

    private Lang lang;

    private GuestbooksService dbService;

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
        List<Guestbook> guestBookEntries = dbService.listGuestBookEntries();
        
        Map<String, Object> toRender = Maps.newHashMap();
        toRender.put("guestBookEntries", guestBookEntries);

        // Default rendering is simple by convention
        // This renders the page in views/ApplicationController/index.ftl.html
        return Results.html().render(toRender);

    }

    public Result post(@Param("email") String email,
                       @Param("content") String content) {

        Guestbook guestbook = new Guestbook(email, content);
        
        dbService.createGuestbook(guestbook);
        
        // ... and redirect to main page
        return Results.redirect("/");

    }
}
