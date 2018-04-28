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
import java.io.File
import javax.lang.model.element.Modifier


class RecordMetaDataGenerator(private val nameMaker: NameMaker, private val schema: Schema, private val outputDir: File) {

    companion object {
        val log = LoggerFactory.getLogger(RecordMetaDataGenerator::class.java)
    }

    fun generate() {
        val className = nameMaker.makeMetaDataClassName(schema)
        log.info("Generating {}", className)

        val constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String::class.java, "fieldName")
                .addStatement("super(\$N, \$T.getClassSchema())", "fieldName", nameMaker.makeClassName(schema))
                .build()

        val builder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .superclass(TypeName.get(AvroGenericRecordParameter::class.java))
                .addMethod(constructor);

//                .primaryConstructor(FunSpec.constructorBuilder()
//                        .addParameter(ParameterSpec.builder("fieldName", String::class).build())
//                        .build())
//                .addSuperclassConstructorParameter("fieldName")
//                .addSuperclassConstructorParameter("%T.getClassSchema()", nameMaker.makeClassName(schema))
        for (field in schema.fields) {
            val fieldTypeName = nameMaker.makeMetaDataTypeName(field.schema())
            builder.addField(FieldSpec.builder(fieldTypeName,
                    nameMaker.makeFieldName(field))
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .initializer("new \$T(\$S)", fieldTypeName, field.name())
                    .build())

//            var initializer = "$fieldTypeName()"
//            if (field.schema().type == Schema.Type.ENUM) {
//                initializer = "$fieldTypeName()"
//            }
//            builder.addProperty(PropertySpec.builder(nameMaker.makeFieldName(field),
//                    fieldTypeName)
//                    .initializer("%T(%S)", fieldTypeName, field.name())
//                    .addAnnotation(JvmField::class)
//                    .build())
        }
        val file = JavaFile.builder(className.packageName(), builder.build())
//                .addType(builder.build())
                .build()
        file.writeTo(outputDir)
    }

}
