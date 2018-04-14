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

import com.squareup.kotlinpoet.*
import org.apache.avro.Schema
import org.apache.avro.compiler.specific.SpecificCompiler
import org.wintersleep.avro.model.*
import java.nio.ByteBuffer

class NameMaker(private val compiler: SpecificCompiler) {

    fun makeMetaDataTypeName(schema: Schema): TypeName {
        return when (schema.type) {
            Schema.Type.ARRAY -> {
                val elementTypeName = makeTypeName(schema.elementType)
                ParameterizedTypeName.get(AvroArrayParameter::class.asClassName(), elementTypeName)
            }
            Schema.Type.ENUM -> {
                val enumClassName = makeClassName(schema)
                ParameterizedTypeName.get(AvroEnumParameter::class.asClassName(), enumClassName)
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
                return ClassName(className.packageName(), className.simpleName() + "MetaData")
            }
            Schema.Type.ENUM -> TODO()
            Schema.Type.ARRAY -> TODO()
            Schema.Type.MAP -> TODO()
            Schema.Type.UNION -> TODO() // makeUnionMetaDataTypeName(schema)
            Schema.Type.FIXED -> TODO()
            Schema.Type.STRING -> AvroStringParameter::class.asClassName()
            Schema.Type.BYTES -> AvroBytesParameter::class.asClassName()
            Schema.Type.INT -> AvroIntegerParameter::class.asClassName()
            Schema.Type.LONG -> AvroLongParameter::class.asClassName()
            Schema.Type.FLOAT -> AvroFloatParameter::class.asClassName()
            Schema.Type.DOUBLE -> AvroDoubleParameter::class.asClassName()
            Schema.Type.BOOLEAN -> AvroBooleanParameter::class.asClassName()
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
            Schema.Type.STRING -> String::class.asTypeName()
            Schema.Type.BYTES -> ByteBuffer::class.asTypeName()
            Schema.Type.INT -> Int::class.asTypeName()
            Schema.Type.LONG -> Long::class.asTypeName()
            Schema.Type.FLOAT -> Float::class.asTypeName()
            Schema.Type.DOUBLE -> Double::class.asTypeName()
            Schema.Type.BOOLEAN -> Boolean::class.asTypeName()
            Schema.Type.NULL -> Nothing::class.asTypeName()
            else -> {
                val beanFullClassName = compiler.javaType(schema)
                val parts = beanFullClassName.split(".")
                return ClassName(parts.subList(0, parts.lastIndex).joinToString("."), parts[parts.lastIndex])
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
            Schema.Type.STRING -> String::class.asClassName()
            Schema.Type.BYTES -> ByteBuffer::class.asClassName()
            Schema.Type.INT -> Int::class.asClassName()
            Schema.Type.LONG -> Long::class.asClassName()
            Schema.Type.FLOAT -> Float::class.asClassName()
            Schema.Type.DOUBLE -> Double::class.asClassName()
            Schema.Type.BOOLEAN -> Boolean::class.asClassName()
            Schema.Type.NULL -> Nothing::class.asClassName()
            else -> {
                val beanFullClassName = compiler.javaType(schema)
                val parts = beanFullClassName.split(".")
                return ClassName(parts.subList(0, parts.lastIndex).joinToString("."), parts[parts.lastIndex])
            }
        }
    }

    fun makeFieldName(field: Schema.Field): String {
        return SpecificCompiler.mangle(field.name()).replace('$', '_')
    }

}
