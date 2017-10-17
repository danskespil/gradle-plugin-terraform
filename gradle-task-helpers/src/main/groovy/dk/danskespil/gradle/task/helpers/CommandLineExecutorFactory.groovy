package dk.danskespil.gradle.task.helpers

import org.gradle.api.Project
/**
 * Allows creation of test services/mocks/stubs by examining the filesystem for files under
 *
 * PROJECT_ROOT/stubTheseFactories/
 *
 * For a file named
 *
 * PROJECT_ROOT/stubTheseFactories/ExecutorFactory
 *
 * If present, the factory is mocked`
 *
 */
class CommandLineExecutorFactory {
    private static Class executorClass

    static AbstractCommandLineExecutor createExecutor(Project project) {
        if (!executorClass) {
            if (markerFileExistsInConventionallyNamedDir(project)) {
                executorClass = CommandLineTestExecutor
            } else {
                executorClass = CommandLineExecutor
            }
        }
        executorClass.newInstance([project:project])
    }

    private static boolean markerFileExistsInConventionallyNamedDir(Project project) {
        return project.file("stubTheseFactories/${getClass().getName()}").exists()
    }
}
