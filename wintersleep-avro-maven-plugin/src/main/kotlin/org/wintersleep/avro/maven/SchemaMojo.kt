package org.wintersleep.avro.maven

/*-
 * #%L
 * org.wintersleep.avro:wintersleep-avro-maven-plugin
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

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.slf4j.LoggerFactory
import org.wintersleep.avro.codegen.CodeGenerator
import java.io.File

@Mojo(name = "schema", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
class SchemaMojo : AbstractMojo() {

    @Parameter(defaultValue = "\${basedir}/src/main/avro")
    private lateinit var sourceDirectory: File

    @Parameter(defaultValue = "\${project.build.directory}/generated-sources/avro")
    private lateinit var outputDirectory: File

    @Parameter
    protected lateinit var imports: List<File>

    @Parameter(defaultValue = "false")
    private var force: Boolean = false



    override fun execute() {
        log.info("AVRO: " + sourceDirectory)
        val generator = CodeGenerator(outputDirectory,
                imports,
                listOf(sourceDirectory),
                force)
        generator.execute()

    }

    companion object {

        private val log = LoggerFactory.getLogger(SchemaMojo::class.java)
    }

}
