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
package org.wintersleep.avro.itest.main;

import org.junit.Test;

import static org.junit.Assert.*;

public class Record0MetaDataTest {

    @Test
    public void testIntScalar() {
        Record0MetaData md = new Record0MetaData("myField");
        assertFalse(md.intScalar.hasRange());
    }

    @Test
    public void testIntScalarWithRange() {
        Record0MetaData md = new Record0MetaData("myFieldWithRange");
        assertTrue(md.intScalarWithRange.hasRange());
        assertEquals(-10, (int) md.intScalarWithRange.getRange().lowerEndpoint());
        assertEquals(10, (int) md.intScalarWithRange.getRange().upperEndpoint());
    }

    @Test
    public void testLongScalar() {
        Record0MetaData md = new Record0MetaData("myField");
        assertFalse(md.longScalar.hasRange());
    }

    @Test
    public void testLongScalarWithRange() {
        Record0MetaData md = new Record0MetaData("myField");
        assertTrue(md.longScalarWithRange.hasRange());
        assertEquals(-100, (long) md.longScalarWithRange.getRange().lowerEndpoint());
        assertEquals(100, (long) md.longScalarWithRange.getRange().upperEndpoint());
    }

}
