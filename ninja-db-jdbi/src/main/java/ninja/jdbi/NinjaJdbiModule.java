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


package ninja.jdbi;


import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import ninja.jdbc.NinjaDatasources;
import ninja.jdbc.NinjaDbHikariModule;


public class NinjaJdbiModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new NinjaDbHikariModule());
    }

    @Provides
    @Singleton
    public NinjaJdbi provideJdbi(NinjaDatasources ninjaDatasources) {
        NinjaJdbiImpl ninjaJdbiImpl = new NinjaJdbiImpl(ninjaDatasources);
        return ninjaJdbiImpl;
    }

}
