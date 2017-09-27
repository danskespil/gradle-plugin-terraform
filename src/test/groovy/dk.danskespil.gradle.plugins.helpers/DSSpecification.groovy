package dk.danskespil.gradle.plugins.terraform.dk.danskespil.gradle.plugins.dk.danskespil.gradle.plugins.helpers

import dk.danskespil.gradle.plugins.terraform.dk.danskespil.gradle.plugins.helpers.dscommandlineexecutor.DSCommandLineExecutorFactory
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
        setupTestStubsByAddingMarkerFilesToAConventionallyNamedDirectory()
    }

    // Easy creation of deep paths with or without files 'at the end'
    File createNewPath(String path) {
        pathMaker.createNewPath(testProjectDir, path)
    }

    private void setupTestStubsByAddingMarkerFilesToAConventionallyNamedDirectory() {
        createNewPath("stubTheseFactories/${DSCommandLineExecutorFactory.getClass().getName()}")
    }
}
