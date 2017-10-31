package dk.danskespil.gradle.test.spock.helpers

import dk.danskespil.gradle.task.helpers.BadPathException
import spock.lang.Specification
import spock.lang.Unroll

class PathSlicerTest extends Specification {
    @Unroll
    def "The path '#badInput' should be rejected as input to PathSlicer"() {
        when:
        new PathSlicer(badInput)

        then:
        thrown(BadPathException)

        where:
        badInput << ['', '/', '\\', '/must-not-start-with-slash']
    }

    @Unroll
    def "The path #path should be interpreted as #nofDirs directories"() {
        when:
        PathSlicer cut = new PathSlicer(path)

        then:
        cut.dirNames.size() == nofDirs

        // http://spockframework.org/spock/docs/1.1/data_driven_testing.html
        where:
        path    | nofDirs
        'a/'    | 1
        'a/b/'  | 2
        'a/b/c' | 2
    }

    @Unroll
    def "If #path does not end with '/', its interpreted as #fileName"() {
        when:
        PathSlicer cut = new PathSlicer(path)

        then:
        cut.fileName == fileName

        // http://spockframework.org/spock/docs/1.1/data_driven_testing.html
        where:
        path    | fileName
        'a'     | 'a'
        'a/gg'  | 'gg'
        'a/b.c' | 'b.c'
    }

    def "When creating a nested file, I need to access the full path originally given to pathSlicer"() {
        when:
        PathSlicer cut = new PathSlicer('a/b/c')

        then:
        cut.path == 'a/b/c'
    }

    def "If there is only dirs in the path, filename is null"() {
        when:
        PathSlicer cut = new PathSlicer('a/b/')

        then:
        !cut.fileName
    }

    def "To make it easier to operate TemporaryFolder, I want to access the dirs as a list of Strings"() {
        when:
        PathSlicer cut = new PathSlicer('a/b/')

        then:
        cut.dirNames instanceof String[]
    }
}
