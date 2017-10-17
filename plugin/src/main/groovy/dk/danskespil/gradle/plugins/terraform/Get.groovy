package dk.danskespil.gradle.plugins.terraform

import dk.danskespil.test.helpers.commandlineexecutor.CommandLineExecutorFactory
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec

class Get extends TerraformBaseTask {
    @OutputFiles
    FileCollection outputFiles = project.fileTree('.terraform/modules')

    @TaskAction
    action() {
        commandLine.addToEnd('terraform', 'get')

        CommandLineExecutorFactory.createExecutor(project).executeExecSpec(this, { ExecSpec e ->
            e.commandLine this.commandLine
        })
    }
    @Override
    String getDescription() {
        return """wraps terraform get. When using the plugin, its called automatically when you call tfPlan"""
    }
}
