package dk.danskespil.gradle.plugins.helpers.dscommandlineexecutor

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.process.ExecSpec

class DSCommandLineExecutor extends AbstractDSCommandLineExecutor {
    DSCommandLineExecutor(Project project) {
        super(project)
    }

    def execute(String command) {
        project.exec { ExecSpec e ->
            e.commandLine command.split(' ')
        }
    }
}
