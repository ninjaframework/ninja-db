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

package ninja.jdbc;

import com.google.common.collect.ImmutableList;
import java.util.List;


public class NinjaDatasourcesImpl implements NinjaDatasources {
    
    private final List<NinjaDatasource> ninjaDatasources;
    
    public NinjaDatasourcesImpl(List<NinjaDatasource> datasources) {
        this.ninjaDatasources = ImmutableList.copyOf(datasources);
    }

    @Override
    public List<NinjaDatasource> getDatasources() {
        return ninjaDatasources;
    }

    @Override
    public NinjaDatasource getDatasource(String name) {
        return ninjaDatasources
                .stream()
                .filter(n -> n.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Datasource " + name + " not found. Make sure it is configured in application.conf."));
    }
    
}
