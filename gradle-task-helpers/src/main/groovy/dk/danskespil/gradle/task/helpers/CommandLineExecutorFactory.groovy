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
            if (isUnderTest()) {
                executorClass = CommandLineTestExecutor
            } else {
                executorClass = CommandLineExecutor
            }
        }
        executorClass.newInstance([project: project])
    }

    static void setIsUnderTest(boolean isUnderTest) {
        if (notCurrentlyUnderTest()) {
            new File("stubTheseFactories").mkdir()
            boolean success = getMarkerFile().createNewFile()
            if (!success) {
                throw new RuntimeException("Unable to create test marker file '${markerFile.getAbsolutePath()}'.")
            }
        }
    }

    static boolean isUnderTest() {
        return getMarkerFile().exists()
    }

    static private boolean notCurrentlyUnderTest() {
        return !isUnderTest()
    }

    static private getMarkerFile() {
        return new File("stubTheseFactories/${this.name}")
    }
}
