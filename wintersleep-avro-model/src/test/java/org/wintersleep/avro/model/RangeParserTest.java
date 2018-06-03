/*-
 * #%L
 * org.wintersleep.avro:wintersleep-avro-model
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
package org.wintersleep.avro.model;


import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import org.junit.Test;

import static org.junit.Assert.*;

public class RangeParserTest {

    @Test(expected = IllegalArgumentException.class)
    public void parseIntEmpty() {
        RangeParser.parseInt("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseIntSingleDot() {
        RangeParser.parseInt(".");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseIntDoubleDot() {
        RangeParser.parseInt("..");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseIntMissingBegin() {
        RangeParser.parseInt("..1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseIntMissingEnd() {
        RangeParser.parseInt("0..");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseIntNotInteger() {
        RangeParser.parseInt("a..b");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseIntLowerGreaterThanUpper() {
        RangeParser.parseInt("1..0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseIntOutOfRange() {
        long tooBig = Integer.MAX_VALUE + 1L;
        RangeParser.parseInt("0.." + tooBig);
    }

    @Test
    public void parseInt() {
        Range<Integer> range = RangeParser.parseInt("0..1");
        assertFalse(range.contains(-1));
        assertTrue(range.contains(0));
        assertTrue(range.contains(1));
        assertFalse(range.contains(2));

        assertEquals(BoundType.CLOSED, range.lowerBoundType());
        assertEquals(Integer.valueOf(0), range.lowerEndpoint());
        assertEquals(Integer.valueOf(1), range.upperEndpoint());
        assertEquals(BoundType.CLOSED, range.upperBoundType());
    }

    @Test
    public void parseIntNegative() {
        Range<Integer> range = RangeParser.parseInt("-20..-10");
        assertFalse(range.contains(-21));
        assertTrue(range.contains(-20));
        assertTrue(range.contains(-10));
        assertFalse(range.contains(-9));
    }

    @Test
    public void parseLong() {
        Range<Long> range = RangeParser.parseLong("0..1");
        assertFalse(range.contains(-1L));
        assertTrue(range.contains(0L));
        assertTrue(range.contains(1L));
        assertFalse(range.contains(2L));

        assertEquals(BoundType.CLOSED, range.lowerBoundType());
        assertEquals(Long.valueOf(0), range.lowerEndpoint());
        assertEquals(Long.valueOf(1), range.upperEndpoint());
        assertEquals(BoundType.CLOSED, range.upperBoundType());
    }

}
