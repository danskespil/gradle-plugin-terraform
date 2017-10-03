package dk.danskespil.gradle.plugins.terraform

import dk.danskespil.gradle.plugins.helpers.dscommandlineexecutor.DSCommandLineExecutorFactory
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec
/**
 * Wraps cli terraform validate
 */

class Validate extends TerraformBaseTask {
    // These inputfiles are the same for Validate and Plan
    @OutputFiles
    FileCollection oTerraformFiles = project.fileTree('.').include('*.tf').include('*.tpl')
    @InputFiles
    FileCollection iTerraformFiles = project.fileTree('.').include('*.tf').include('*.tpl')

    @TaskAction
    action() {
        commandLine.addToEnd('terraform', 'validate')

        DSCommandLineExecutorFactory.createExecutor(project).executeExecSpec(this, { ExecSpec e ->
            e.commandLine this.commandLine
        })
    }

    @Override
    String getDescription() {
        return "Wraps cli terraform validate"
    }
}
