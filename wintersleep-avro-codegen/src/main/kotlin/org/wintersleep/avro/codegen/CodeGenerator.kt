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

import org.apache.avro.Schema
import org.apache.avro.compiler.specific.SpecificCompiler
import org.slf4j.LoggerFactory
import java.io.File

// TODO configurable String type
class CodeGenerator(private val outputDir: File,
                    private val importedFiles: List<File>,
                    private val inputPaths: List<File>,
                    private val force: Boolean) {

    companion object {
        private val log = LoggerFactory.getLogger(CodeGenerator::class.java)
    }

    fun execute() {
        val inputFiles = findInputFiles(inputPaths)
        if (isUpToDate(inputFiles)) {
            log.info("Generated code is up to date, skipping generation.")
        } else {
            val schema = buildSchema(inputFiles)
            val compiler = SpecificCompiler(schema)
            val nameMaker = NameMaker(compiler)
            generate(nameMaker, schema)
            log.info("Code generation finished.")
        }
    }

    private fun buildSchema(inputFiles: List<File>): Schema {
        assert(inputFiles.isNotEmpty())
        val parser = Schema.Parser()
        var schema: Schema? = null
        for (f in importedFiles) {
            schema = parser.parse(f)
        }
        for (f in inputFiles) {
            if (f !in importedFiles) {
                schema = parser.parse(f)
            }
        }
        return schema!!
    }

    private fun findInputFiles(inputPaths: List<File>): List<File> {
        val result = mutableListOf<File>()
        for (inputPath in inputPaths) {
            when {
                inputPath.isFile -> result.add(inputPath)
                inputPath.isDirectory -> result.addAll(findInputFiles(inputPath.listFiles().asList()))
                else -> log.debug("Ignoring unknown type of path: {}", inputPath)
            }
        }
        return result
    }

    private fun isUpToDate(inputFiles: List<File>): Boolean {
        if (force) {
            return false
        }
        // TODO: Make sure to check included files too!
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun generate(nameMaker: NameMaker, schema: Schema) {
        schema.generate(nameMaker, outputDir)
    }

    private fun Schema.generate(nameMaker: NameMaker, outputDir: File) {
        if (type == Schema.Type.RECORD) {
            RecordMetaDataGenerator(nameMaker, this, outputDir).generate()
            for (field in fields) {
                //println(field.name())
                field.schema().generate(nameMaker, outputDir)
            }
        } else if (type == Schema.Type.UNION) {
            for (fieldSchema in types) {
                fieldSchema.generate(nameMaker, outputDir)
            }

        }
    }

}
