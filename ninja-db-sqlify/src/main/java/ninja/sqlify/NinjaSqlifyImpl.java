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

package ninja.sqlify;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import ninja.jdbc.NinjaDatasource;
import ninja.jdbc.NinjaDatasources;
import org.r10r.sqlify.Database;

public class NinjaSqlifyImpl implements NinjaSqlify {

    final Map<String, Database> nameToDatabaseMap;

    public NinjaSqlifyImpl(NinjaDatasources ninjaDatasources) {

        Map<String, Database> map = new HashMap<>();

        for (NinjaDatasource ninjaDatasource : ninjaDatasources.getDatasources()) {
            Database database = Database.use(ninjaDatasource.getDataSource());
            map.put(ninjaDatasource.getName(), database);
        }

        this.nameToDatabaseMap = ImmutableMap.copyOf(map);
    }

    @Override
    public Database getDatabase(String datasourceName) {
        return nameToDatabaseMap.get(datasourceName);
    }

}
