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

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO Support RangeSets with more than one Range, if really necessary.
@ParametersAreNonnullByDefault
public class RangeParser {

    private static final Pattern PATTERN = Pattern.compile("(-?\\d+)\\.\\.(-?\\d+)");

    @Nonnull
    public static Range<Integer> parseInt(String rangeExpr) {
        Matcher m = PATTERN.matcher(rangeExpr);
        if (m.matches()) {
            int lowerBound = Integer.parseInt(m.group(1));
            int upperBound = Integer.parseInt(m.group(2));
            return Range.closed(lowerBound, upperBound);
        }
        throw new IllegalArgumentException(String.format("Expression '%s' does not match pattern '%s", rangeExpr, PATTERN));
    }

    @Nonnull
    public static Range<Long> parseLong(String rangeExpr) {
        Matcher m = PATTERN.matcher(rangeExpr);
        if (m.matches()) {
            long lowerBound = Long.parseLong(m.group(1));
            long upperBound = Long.parseLong(m.group(2));
            return Range.closed(lowerBound, upperBound);
        }
        throw new IllegalArgumentException(String.format("Expression '%s' does not match pattern '%s", rangeExpr, PATTERN));
    }

}
