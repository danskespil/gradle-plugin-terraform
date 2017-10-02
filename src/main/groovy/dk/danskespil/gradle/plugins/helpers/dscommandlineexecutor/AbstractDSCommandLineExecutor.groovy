package dk.danskespil.gradle.plugins.helpers.dscommandlineexecutor

import dk.danskespil.gradle.plugins.terraform.Plan
import org.gradle.api.Project

abstract class AbstractDSCommandLineExecutor {
    Project project

    AbstractDSCommandLineExecutor(Project project) {
        this.project = project
    }

    abstract def executeExecSpec(Plan task, Closure e)
}
