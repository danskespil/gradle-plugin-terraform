package dk.danskespil.gradle.plugins.helpers.dscommandlineexecutor

import dk.danskespil.gradle.plugins.terraform.TerraformBaseTask
import org.gradle.api.Project

abstract class AbstractDSCommandLineExecutor {
    Project project

    AbstractDSCommandLineExecutor(Project project) {
        this.project = project
    }

    abstract def executeExecSpec(TerraformBaseTask task, Closure e)
}
