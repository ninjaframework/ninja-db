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

package ninja.jdbc;

import com.google.common.collect.Lists;
import java.util.List;
import javax.sql.DataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.assertj.core.api.Assertions.*;


@RunWith(MockitoJUnitRunner.class)
public class NinjaDatasourcesImplTest {
    
    public NinjaDatasourcesImplTest() {
    }

    @Test
    public void testGetDatasources() {
        // given
        DataSource dataSource1 = Mockito.mock(DataSource.class);
        NinjaDatasource ninjaDatasource1 = new NinjaDatasource("datasource1", dataSource1);
        DataSource dataSource2 = Mockito.mock(DataSource.class);
        NinjaDatasource ninjaDatasource2 = new NinjaDatasource("datasource1", dataSource2);
        NinjaDatasources ninjaDatasources = new NinjaDatasourcesImpl(Lists.newArrayList(ninjaDatasource1, ninjaDatasource2));
        
        // when
        List<NinjaDatasource> result = ninjaDatasources.getDatasources();
        
        // then
        assertThat(result).contains(ninjaDatasource1);
        assertThat(result).contains(ninjaDatasource2);
    }
    
    @Test
    public void testGetDatasource_withExistingDatasource() {
        // given
        DataSource dataSource1 = Mockito.mock(DataSource.class);
        NinjaDatasource ninjaDatasource1 = new NinjaDatasource("datasource1", dataSource1);
        DataSource dataSource2 = Mockito.mock(DataSource.class);
        NinjaDatasource ninjaDatasource2 = new NinjaDatasource("datasource2", dataSource2);
        NinjaDatasources ninjaDatasources = new NinjaDatasourcesImpl(Lists.newArrayList(ninjaDatasource1, ninjaDatasource2));
        
        // when
        NinjaDatasource result = ninjaDatasources.getDatasource("datasource2");
        
        // then
        assertThat(result).isEqualTo(ninjaDatasource2);
    }
    
    @Test
    public void testGetDatasource_throwsExceptionWhenDatasourceDoesNotExist() {
        // given
        DataSource dataSource1 = Mockito.mock(DataSource.class);
        NinjaDatasource ninjaDatasource1 = new NinjaDatasource("datasource1", dataSource1);
        DataSource dataSource2 = Mockito.mock(DataSource.class);
        NinjaDatasource ninjaDatasource2 = new NinjaDatasource("datasource2", dataSource2);
        NinjaDatasources ninjaDatasources = new NinjaDatasourcesImpl(Lists.newArrayList(ninjaDatasource1, ninjaDatasource2));
        
        // when
        Throwable throwable = catchThrowable(() -> ninjaDatasources.getDatasource("datasource3"));
        
        // then
        assertThat(throwable).isInstanceOf(RuntimeException.class);
    }
    
}
