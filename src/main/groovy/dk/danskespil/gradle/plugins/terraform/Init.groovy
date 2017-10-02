package dk.danskespil.gradle.plugins.terraform

import dk.danskespil.gradle.plugins.helpers.dscommandlineexecutor.DSCommandLineExecutorFactory
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec

class Init extends TerraformTask {
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
}
