package dk.danskespil.gradle.plugins.terraform

import dk.danskespil.test.helpers.dscommandlineexecutor.DSCommandLineExecutorFactory
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec

class Init extends TerraformBaseTask {
    @OutputFiles
    FileCollection outputFilesSoGradleOnlyBuildsWhenItChanges = project.fileTree('.terraform/terraform.tfstate')

    @TaskAction
    action() {
        commandLine.addToEnd('terraform', 'init')

        // Never run terraform init if a state file is present
        if (aStateFileIsPresent()) {
            return
        }

        DSCommandLineExecutorFactory.createExecutor(project).executeExecSpec(this, { ExecSpec e ->
            e.commandLine this.commandLine
        })
    }

    private boolean aStateFileIsPresent() {
        return project.file('.terraform/terraform.tfstate').exists()
    }

    @Override
    String getDescription() {
        return """Wraps terraform init. Will only execute if no .terraform/terraform.state file is present. Designed to initialize automatically in a fresh clone"""
    }

}
