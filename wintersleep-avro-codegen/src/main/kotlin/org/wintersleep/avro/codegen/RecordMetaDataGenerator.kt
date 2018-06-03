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

import com.squareup.javapoet.*
import org.apache.avro.Schema
import org.slf4j.LoggerFactory
import org.wintersleep.avro.model.AvroGenericRecordParameter
import org.wintersleep.avro.model.RangeParser
import java.io.File
import javax.lang.model.element.Modifier


class RecordMetaDataGenerator(private val nameMaker: NameMaker, private val schema: Schema, private val outputDir: File) {

    companion object {
        val log = LoggerFactory.getLogger(RecordMetaDataGenerator::class.java)!!
    }

    fun generate() {
        val className = nameMaker.makeMetaDataClassName(schema)
        log.info("Generating {}", className)

        val constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String::class.java, "fieldName")
                .addStatement("super(\$N, \$T.getClassSchema())", "fieldName", nameMaker.makeClassName(schema))
                .build()
        val classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .superclass(TypeName.get(AvroGenericRecordParameter::class.java))
                .addMethod(constructor)
        for (field in schema.fields) {
            addField(classBuilder, field)
        }
        val file = JavaFile.builder(className.packageName(), classBuilder.build()).build()
        file.writeTo(outputDir)
    }

    private fun addField(classBuilder: TypeSpec.Builder, field: Schema.Field) {
        val schema = field.schema()
        val rangeExpr = field.getProp("range")
        if (rangeExpr != null) {
            validateRange(field, rangeExpr)
        }

        val fieldTypeName = nameMaker.makeMetaDataTypeName(schema)
        val fieldBuilder = FieldSpec.builder(fieldTypeName,
                nameMaker.makeFieldName(field))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        if (rangeExpr != null) {
            fieldBuilder.initializer("new \$T(\$S, \$S)", fieldTypeName, field.name(), rangeExpr)
        } else {
            fieldBuilder.initializer("new \$T(\$S)", fieldTypeName, field.name())
        }
        classBuilder.addField(fieldBuilder.build())
    }
}

private fun validateRange(field: Schema.Field, rangeExpr: String) {
    val name = field.name()
    val schema = field.schema()
    if (schema.type == Schema.Type.INT || schema.type == Schema.Type.LONG) {
        validateIntLongRange(name, schema, rangeExpr)
    } else {
        if (schema.isNullableSchema) {
            validateIntLongRange(name, schema.nonNullSchema, rangeExpr)
        } else {
            throw IllegalArgumentException("Field '$name' of type '$schema.type' does not support range expressions.")
        }
    }
}

private fun validateIntLongRange(name: String, schema: Schema, rangeExpr: String) {
    try {
        when (schema.type) {
            Schema.Type.INT -> RangeParser.parseInt(rangeExpr)
            Schema.Type.LONG -> RangeParser.parseLong(rangeExpr)
            else -> {
                throw IllegalArgumentException("Field '$name' of type '$schema.type' does not support range expressions.")
            }
        }
    } catch (e: IllegalArgumentException) {
        throw IllegalArgumentException("Field '$name' has invalid range: ${e.message}", e)
    }
}
