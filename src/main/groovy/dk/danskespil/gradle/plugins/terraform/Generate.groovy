package dk.danskespil.gradle.plugins.terraform

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class Generate extends DefaultTask {
    @Input
    String version

    @OutputFile
    File outputFile

    @TaskAction
    void generate() {
        def file = getOutputFile()
        if (!file.isFile()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
        file.write "Version: ${getVersion()}"
    }
}
