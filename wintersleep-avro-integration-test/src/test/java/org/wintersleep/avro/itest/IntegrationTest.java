/*-
 * #%L
 * org.wintersleep.avro:wintersleep-avro-integration-test
 * %%
 * Copyright (C) 2018 Davy Verstappen
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.wintersleep.avro.itest;

import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecordBuilder;
import org.junit.Test;
import org.wintersleep.avro.itest.main.Record0;

import static org.junit.Assert.assertNotNull;

public class IntegrationTest {

    @Test
    public void test() {
        GenericData.Record record = new GenericRecordBuilder(Record0.getClassSchema()).build();
        assertNotNull(record.getSchema());
    }

}
