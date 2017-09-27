package dk.danskespil.gradle.plugins.helpers.dscommandlineexecutor

import org.gradle.api.Project

class DSCommandLineTestExecutor extends AbstractDSCommandLineExecutor {
    DSCommandLineTestExecutor(Project project) {
        super(project)
    }

    def execute(String command) {
        println command
    }
}