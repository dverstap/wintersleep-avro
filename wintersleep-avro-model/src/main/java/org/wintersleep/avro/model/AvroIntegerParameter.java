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

import com.google.common.collect.Range;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AvroIntegerParameter extends AvroNumberParameter<Integer> {

    private static final Range<Integer> DEFAULT_RANGE = Range.closed(Integer.MIN_VALUE, Integer.MAX_VALUE);

    private final Range<Integer> range;

    public AvroIntegerParameter(String fieldName) {
        super(fieldName);
        range = DEFAULT_RANGE;
    }

    public AvroIntegerParameter(String fieldName, String rangeExpr) {
        super(fieldName);
        range = RangeParser.parseInt(rangeExpr);
    }

    public boolean hasRange() {
        return range != DEFAULT_RANGE;
    }

    public Range<Integer> getRange() {
        return range;
    }

}
