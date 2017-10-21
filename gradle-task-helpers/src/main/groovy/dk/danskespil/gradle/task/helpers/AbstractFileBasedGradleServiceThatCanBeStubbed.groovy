package dk.danskespil.gradle.task.helpers

import org.gradle.api.Project

/*
 * Allows creation of test stubs by examining the filesystem for files under
 *
 * PROJECT_ROOT/build/stubTheseFactories/
 *
 * For a file named
 *
 * PROJECT_ROOT/build/stubTheseFactories/FULLYQUALIFIEDNAME
 *
 * If present, the factory is stubbed. Stubbed means that it can be called in a controlled manner from a test.
 * In the case of this stub, calling it will echo out what would be have been executed.
 *
 */

abstract class  AbstractFileBasedGradleServiceThatCanBeStubbed<S> implements IGradleServiceThatCanBeStubbed {
    abstract S createService(Project project)

    void enableStub() {
        if (notCurrentlyUnderTest()) {
            new File("build/stubTheseFactories").mkdirs()
            boolean success = getMarkerFile().createNewFile()
            if (!success) {
                throw new RuntimeException("Unable to create test marker file '${markerFile.getAbsolutePath()}'.")
            }
        }
    }

    boolean isStubbed() {
        return getMarkerFile().exists()
    }

    private boolean notCurrentlyUnderTest() {
        return !isStubbed()
    }

    private getMarkerFile() {
        return new File("build/stubTheseFactories/${this.getClass().getName()}")
    }

}
