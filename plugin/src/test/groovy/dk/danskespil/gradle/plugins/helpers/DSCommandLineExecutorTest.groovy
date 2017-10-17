package dk.danskespil.gradle.plugins.helpers

import dk.danskespil.test.helpers.commandlineexecutor.CommandLineExecutorFactory
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Unroll

class DSCommandLineExecutorTest extends DSSpecification {

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
