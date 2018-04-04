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

import java.io.File

fun main(args: Array<String>) {
    val outputDir = File("wintersleep-avro-integration-test/target/generated-sources/avro/")
    val sourceDir = File("wintersleep-avro-integration-test/src/main/avro")
    val importedFiles = listOf(File(sourceDir, "types.avsc"))

    val generator = CodeGenerator(outputDir, importedFiles, listOf(sourceDir), true)
    generator.execute()
}
