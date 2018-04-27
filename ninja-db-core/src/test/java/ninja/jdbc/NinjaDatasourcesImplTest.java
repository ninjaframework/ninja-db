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

import com.google.common.collect.Lists;
import java.util.List;
import javax.sql.DataSource;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class NinjaDatasourcesImplTest {
    
    public NinjaDatasourcesImplTest() {
    }

    @Test
    public void testGetDatasources() {
        DataSource dataSource1 = Mockito.mock(DataSource.class);
        NinjaDatasource ninjaDatasource1 = new NinjaDatasource("datasource1", dataSource1);
        DataSource dataSource2 = Mockito.mock(DataSource.class);
        NinjaDatasource ninjaDatasource2 = new NinjaDatasource("datasource1", dataSource2);
        NinjaDatasources ninjaDatasources = new NinjaDatasourcesImpl(Lists.newArrayList(ninjaDatasource1, ninjaDatasource2));
        
        List<NinjaDatasource> result = ninjaDatasources.getDatasources();
        
        assertThat(result, CoreMatchers.hasItem(ninjaDatasource1));
        assertThat(result, CoreMatchers.hasItem(ninjaDatasource2));
    }
    
    @Test
    public void testGetDatasource_withExistingDatasource() {
        DataSource dataSource1 = Mockito.mock(DataSource.class);
        NinjaDatasource ninjaDatasource1 = new NinjaDatasource("datasource1", dataSource1);
        DataSource dataSource2 = Mockito.mock(DataSource.class);
        NinjaDatasource ninjaDatasource2 = new NinjaDatasource("datasource2", dataSource2);
        NinjaDatasources ninjaDatasources = new NinjaDatasourcesImpl(Lists.newArrayList(ninjaDatasource1, ninjaDatasource2));
        
        NinjaDatasource result = ninjaDatasources.getDatasource("datasource2");
        
        assertThat(result, CoreMatchers.is(ninjaDatasource2));
    }
    
    @Test(expected = RuntimeException.class)
    public void testGetDatasource_throwsExceptionWhenDatasourceDoesNotExist() {
        DataSource dataSource1 = Mockito.mock(DataSource.class);
        NinjaDatasource ninjaDatasource1 = new NinjaDatasource("datasource1", dataSource1);
        DataSource dataSource2 = Mockito.mock(DataSource.class);
        NinjaDatasource ninjaDatasource2 = new NinjaDatasource("datasource2", dataSource2);
        NinjaDatasources ninjaDatasources = new NinjaDatasourcesImpl(Lists.newArrayList(ninjaDatasource1, ninjaDatasource2));
        
        // we expect an exception here:
        NinjaDatasource result = ninjaDatasources.getDatasource("datasource3");
    }
    
}
