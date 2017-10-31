package dk.danskespil.gradle.test.spock.helpers

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class TemporaryFolderSpecification extends Specification {
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
    }

    // Easy creation of deep paths with or without files 'at the end'
    File createPathInTemporaryFolder(String path) {
        pathMaker.createNewPath(testProjectDir, path)
    }

    boolean existsInTemporaryFolder(String path) {
        if (!path.startsWith(File.separator)) {
            path += File.pathSeparator + path
        }
        return new File(testProjectDir.root.absolutePath + "${path}").exists()
    }

    File fileInTemporaryFolder(String path) {
        if (!path.startsWith(File.separator)) {
            path += File.pathSeparator + path
        }
        return new File(testProjectDir.root.getAbsolutePath() + path)
    }
}
