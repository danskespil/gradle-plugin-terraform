package dk.danskespil.gradle.plugins.helpers.dscommandlineexecutor

import dk.danskespil.gradle.plugins.terraform.TerraformBaseTask
import org.gradle.api.Project

class DSCommandLineExecutor extends AbstractDSCommandLineExecutor {
    DSCommandLineExecutor(Project project) {
        super(project)
    }

    def executeExecSpec(TerraformBaseTask task, Closure e) {
        task.project.exec e
    }
}
