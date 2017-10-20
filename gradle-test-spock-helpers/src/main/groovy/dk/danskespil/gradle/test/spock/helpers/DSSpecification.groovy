package dk.danskespil.gradle.test.spock.helpers

import dk.danskespil.gradle.task.helpers.GradleServiceCommandlineExecutor
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class DSSpecification extends Specification {
    @Delegate
    static TestHelper testHelper
    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile
    PathMaker pathMaker = new PathMaker()

    // run before the first feature method
    def setupSpec() {
        testHelper = new TestHelper()
    }

    // run before every feature method
    def setup() {
        buildFile = testProjectDir.newFile('build.gradle')
        testHelper.testProjectDir = testProjectDir
        setupTestStubsByAddingMarkerFilesToAConventionNamedDirectory()
    }

    // Easy creation of deep paths with or without files 'at the end'
    File createNewPath(String path) {
        pathMaker.createNewPath(testProjectDir, path)
    }

    private void setupTestStubsByAddingMarkerFilesToAConventionNamedDirectory() {
        GradleServiceCommandlineExecutor.setIsUnderTest(true)
    }

    boolean exists(String path) {
        if (!path.startsWith(File.separator)) {
            path += File.pathSeparator + path
        }
        return new File(testProjectDir.root.absolutePath + "${path}").exists()
    }

    File file(String path) {
        if (!path.startsWith(File.separator)) {
            path += File.pathSeparator + path
        }
        return new File(testProjectDir.root.getAbsolutePath() + path)
    }
}
