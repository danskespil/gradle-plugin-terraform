package dk.danskespil.gradle.plugins.helpers.dscommandlineexecutor

import dk.danskespil.gradle.plugins.terraform.TerraformTask
import org.gradle.api.Project

class DSCommandLineExecutor extends AbstractDSCommandLineExecutor {
    DSCommandLineExecutor(Project project) {
        super(project)
    }

    def executeExecSpec(TerraformTask task, Closure e) {
        task.project.exec e
    }
}
