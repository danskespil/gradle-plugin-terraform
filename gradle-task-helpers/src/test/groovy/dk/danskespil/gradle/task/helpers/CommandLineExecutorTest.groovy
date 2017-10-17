package dk.danskespil.gradle.task.helpers

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll

class CommandLineExecutorTest extends Specification {
    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile

    def setup() {
        buildFile = testProjectDir.newFile('build.gradle')
    }

    @Unroll
    def "As Custom Task I can use TestExecutor to fake execution of '#command' on the system I live on"() {
        given:
        buildFile << """
        // Minimal build file with something in it. We only need the project object
        apply plugin:'java' 
        """

        Project project = ProjectBuilder.builder()
                .withProjectDir(testProjectDir.root)
                .build()

        when:
        def ex = CommandLineExecutorFactory.createExecutor(project)

        then:
        ex

        where:
        command << ["terraform --version"]
    }
}
