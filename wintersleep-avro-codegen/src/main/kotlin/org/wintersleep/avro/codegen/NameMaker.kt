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

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import org.apache.avro.Schema
import org.apache.avro.compiler.specific.SpecificCompiler
import org.wintersleep.avro.model.*
import java.nio.ByteBuffer

class NameMaker(private val compiler: SpecificCompiler) {

    fun makeMetaDataTypeName(schema: Schema): TypeName {
        return when (schema.type) {
            Schema.Type.ARRAY -> {
                val elementTypeName = makeTypeName(schema.elementType)
                ParameterizedTypeName.get(ClassName.get(AvroArrayParameter::class.java), elementTypeName)
            }
            Schema.Type.ENUM -> {
                val enumClassName = makeClassName(schema)
                ParameterizedTypeName.get(ClassName.get(AvroEnumParameter::class.java), enumClassName)
            }
            Schema.Type.UNION -> makeUnionMetaDataTypeName(schema)
            else -> {
                return makeMetaDataClassName(schema)
            }
        }
    }

    fun makeMetaDataClassName(schema: Schema): ClassName {
        return when (schema.type!!) {
            Schema.Type.RECORD -> {
                val className = makeClassName(schema)
                return ClassName.get(className.packageName(), className.simpleName() + "MetaData")
            }
            Schema.Type.ENUM -> TODO()
            Schema.Type.ARRAY -> TODO()
            Schema.Type.MAP -> TODO()
            Schema.Type.UNION -> TODO() // makeUnionMetaDataTypeName(schema)
            Schema.Type.FIXED -> TODO()
            Schema.Type.STRING -> ClassName.get(AvroStringParameter::class.java)
            Schema.Type.BYTES -> ClassName.get(AvroBytesParameter::class.java)
            Schema.Type.INT -> ClassName.get(AvroIntegerParameter::class.java)
            Schema.Type.LONG -> ClassName.get(AvroLongParameter::class.java)
            Schema.Type.FLOAT -> ClassName.get(AvroFloatParameter::class.java)
            Schema.Type.DOUBLE -> ClassName.get(AvroDoubleParameter::class.java)
            Schema.Type.BOOLEAN -> ClassName.get(AvroBooleanParameter::class.java)
            Schema.Type.NULL -> TODO()
        }
    }

    private fun makeUnionMetaDataTypeName(schema: Schema): TypeName {
        if (schema.types.size != 2) {
            throw IllegalArgumentException("Only supports unions of two types.")
        }
        val type = findNonNullSchema(schema.types)
        return makeMetaDataTypeName(type)
    }

    private fun makeUnionTypeName(schema: Schema): TypeName {
        if (schema.types.size != 2) {
            throw IllegalArgumentException("Only supports unions of two types.")
        }
        val type = findNonNullSchema(schema.types)
        return makeTypeName(type)
    }

    private fun findNonNullSchema(schemas: List<Schema>): Schema {
        for (schema in schemas) {
            if (schema.type != Schema.Type.NULL) {
                return schema
            }
        }
        throw IllegalArgumentException("Only supports unions of null and something else.")
    }

    private fun makeTypeName(schema: Schema): TypeName {
        return when (schema.type) {
            Schema.Type.RECORD -> TODO()
            Schema.Type.ENUM -> TODO()
            Schema.Type.ARRAY -> TODO()
            Schema.Type.MAP -> TODO()
            Schema.Type.UNION -> makeUnionTypeName(schema)
            Schema.Type.FIXED -> TODO()
            Schema.Type.STRING -> TypeName.get(String::class.java)
            Schema.Type.BYTES -> TypeName.get(ByteBuffer::class.java)
            Schema.Type.INT -> TypeName.get(Int::class.java)
            Schema.Type.LONG -> TypeName.get(Long::class.java)
            Schema.Type.FLOAT -> TypeName.get(Float::class.java)
            Schema.Type.DOUBLE -> TypeName.get(Double::class.java)
            Schema.Type.BOOLEAN -> TypeName.get(Boolean::class.java)
            Schema.Type.NULL -> TypeName.get(Nothing::class.java)
            else -> {
                val beanFullClassName = compiler.javaType(schema)
                val parts = beanFullClassName.split(".")
                return ClassName.get(parts.subList(0, parts.lastIndex).joinToString("."), parts[parts.lastIndex])
            }
        }

    }

    fun makeClassName(schema: Schema): ClassName {
        return when (schema.type) {
        //Schema.Type.RECORD -> TODO()
        //Schema.Type.ENUM -> TODO()
            Schema.Type.ARRAY -> TODO()
            Schema.Type.MAP -> TODO()
            Schema.Type.UNION -> TODO()
            Schema.Type.FIXED -> TODO()
            Schema.Type.STRING -> ClassName.get(String::class.java)
            Schema.Type.BYTES -> ClassName.get(ByteBuffer::class.java)
            Schema.Type.INT -> ClassName.get(Int::class.java)
            Schema.Type.LONG -> ClassName.get(Long::class.java)
            Schema.Type.FLOAT -> ClassName.get(Float::class.java)
            Schema.Type.DOUBLE -> ClassName.get(Double::class.java)
            Schema.Type.BOOLEAN -> ClassName.get(Boolean::class.java)
            Schema.Type.NULL -> ClassName.get(Nothing::class.java)
            else -> {
                val beanFullClassName = compiler.javaType(schema)
                val parts = beanFullClassName.split(".")
                return ClassName.get(parts.subList(0, parts.lastIndex).joinToString("."), parts[parts.lastIndex])
            }
        }
    }

    fun makeFieldName(field: Schema.Field): String {
        return SpecificCompiler.mangle(field.name()).replace('$', '_')
    }

}
