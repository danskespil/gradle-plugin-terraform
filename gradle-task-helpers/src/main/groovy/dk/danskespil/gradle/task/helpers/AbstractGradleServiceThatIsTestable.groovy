package dk.danskespil.gradle.task.helpers

import org.gradle.api.Project

abstract class AbstractGradleServiceThatIsTestable<S> implements IGradleServiceThatIsTestable {
    abstract S createService(Project project)

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
