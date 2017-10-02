package dk.danskespil.gradle.plugins.helpers.dscommandlineexecutor

import dk.danskespil.gradle.plugins.terraform.TerraformTask
import org.gradle.api.Project

class DSCommandLineTestExecutor extends AbstractDSCommandLineExecutor {
    DSCommandLineTestExecutor(Project project) {
        super(project)
    }

    def executeExecSpec(TerraformTask task, Closure e) {
        task.commandLine.prefix 'echo'
        task.project.exec e
    }
}
