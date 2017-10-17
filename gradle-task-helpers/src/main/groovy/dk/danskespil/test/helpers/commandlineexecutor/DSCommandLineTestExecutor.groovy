package dk.danskespil.test.helpers.commandlineexecutor

import org.gradle.api.DefaultTask
import org.gradle.api.Project

class DSCommandLineTestExecutor extends AbstractDSCommandLineExecutor {
    DSCommandLineTestExecutor(Project project) {
        super(project)
    }

    def executeExecSpec(DefaultTask task, Closure e) {
        task.commandLine.prefix 'echo'
        task.project.exec e
    }
}
