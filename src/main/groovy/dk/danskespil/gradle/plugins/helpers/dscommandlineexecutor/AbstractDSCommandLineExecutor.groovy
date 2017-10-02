package dk.danskespil.gradle.plugins.helpers.dscommandlineexecutor

import org.gradle.api.Project
import org.gradle.process.ExecSpec

abstract class AbstractDSCommandLineExecutor {
    Project project

    AbstractDSCommandLineExecutor(Project project) {
        this.project = project
    }
    abstract def execute(String command)

}
