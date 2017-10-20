package dk.danskespil.gradle.task.helpers

import org.gradle.api.DefaultTask
import org.gradle.api.Project

abstract class AbstractGradleServiceCommandlineExecutor {
    Project project

    AbstractGradleServiceCommandlineExecutor(Project project) {
        this.project = project
    }

    abstract def executeExecSpec(DefaultTask task, Closure e)
}
