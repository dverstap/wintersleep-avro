/*-
 * #%L
 * org.wintersleep.avro:wintersleep-avro-codegen
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
package org.wintersleep.avro.codegen

import com.google.common.base.Preconditions
import org.apache.avro.Schema

val Schema.isNullableSchema: Boolean
    get() {
        if (this.type == Schema.Type.UNION) {
            if (this.types.size == 2) {
                for (childType in this.types) {
                    if (childType.type == Schema.Type.NULL) {
                        return true
                    }
                }
            }
        }
        return false
    }

val Schema.nonNullSchema: Schema
    get() {
        Preconditions.checkArgument(this.isNullableSchema)
        for (childType in this.types) {
            if (childType.type != Schema.Type.NULL) {
                return childType
            }
        }
        throw IllegalArgumentException()
    }
