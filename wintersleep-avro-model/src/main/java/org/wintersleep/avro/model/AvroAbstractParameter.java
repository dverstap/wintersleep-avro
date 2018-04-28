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

import org.apache.avro.generic.GenericRecord;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static java.lang.String.format;

@ParametersAreNonnullByDefault
public abstract class AvroAbstractParameter<T> implements AvroParameter<T> {

    private final String fieldName;

    protected AvroAbstractParameter(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Nullable
    public T findValue(GenericRecord record) {
        return (T) record.get(fieldName);
    }

    @Nonnull
    public T getValue(GenericRecord record) {
        T result = findValue(record);
        if (result == null) {
            throw new IllegalArgumentException(format("Field %s does not have a value in %s", fieldName, record));
        }
        return result;
    }

    public void setValue(GenericRecord record, T value) {
        record.put(fieldName, value);
    }

}
