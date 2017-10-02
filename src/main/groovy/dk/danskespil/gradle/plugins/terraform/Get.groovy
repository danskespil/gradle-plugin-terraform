package dk.danskespil.gradle.plugins.terraform

import dk.danskespil.gradle.plugins.helpers.dscommandlineexecutor.DSCommandLineExecutorFactory
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec

class Get extends TerraformTask {
    @InputFiles
    FileCollection moduleFiles = project.fileTree('.terraform/modules')

    @TaskAction
    action() {
        commandLine.addToEnd('terraform', 'get')

        DSCommandLineExecutorFactory.createExecutor(project).executeExecSpec(this, { ExecSpec e ->
            e.commandLine this.commandLine
        })
    }
}
