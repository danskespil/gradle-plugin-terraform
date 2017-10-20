package dk.danskespil.gradle.task.helpers

import org.gradle.api.Project

/**
 * Allows creation of test services/mocks/stubs by examining the filesystem for files under
 *
 * PROJECT_ROOT/stubTheseFactories/
 *
 * For a file named
 *
 * PROJECT_ROOT/stubTheseFactories/FULLYQUALIFIEDNAME
 *
 * If present, the factory is mocked`
 *
 */
class GradleServiceCommandlineExecutor implements GradleServiceThatIsTestable {
    private Class executorClass

    AbstractGradleServiceCommandlineExecutor createService(Project project) {
        if (!executorClass) {
            if (isUnderTest()) {
                executorClass = CommandLineTestExecutor
            } else {
                executorClass = CommandLineExecutor
            }
        }
        executorClass.newInstance([project: project])
    }

    void setIsUnderTest(boolean isUnderTest) {
        if (notCurrentlyUnderTest()) {
            new File("build/stubTheseFactories").mkdirs()
            boolean success = getMarkerFile().createNewFile()
            if (!success) {
                throw new RuntimeException("Unable to create test marker file '${markerFile.getAbsolutePath()}'.")
            }
        }
    }

    boolean isUnderTest() {
        return getMarkerFile().exists()
    }

    private boolean notCurrentlyUnderTest() {
        return !isUnderTest()
    }

    private getMarkerFile() {
        return new File("build/stubTheseFactories/${this.getClass().getName()}")
    }
}
