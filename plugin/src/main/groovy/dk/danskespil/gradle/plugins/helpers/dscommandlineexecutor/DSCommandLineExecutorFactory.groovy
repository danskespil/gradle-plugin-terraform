package dk.danskespil.gradle.plugins.helpers.dscommandlineexecutor

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
class DSCommandLineExecutorFactory {
    private static Class executorClass

    static AbstractDSCommandLineExecutor createExecutor(Project project) {
        if (!executorClass) {
            if (markerFileExistsInConventionallyNamedDir(project)) {
                executorClass = DSCommandLineTestExecutor
            } else {
                executorClass = DSCommandLineExecutor
            }
        }
        executorClass.newInstance([project:project])
    }

    private static boolean markerFileExistsInConventionallyNamedDir(Project project) {
        return project.file("stubTheseFactories/${getClass().getName()}").exists()
    }
}
