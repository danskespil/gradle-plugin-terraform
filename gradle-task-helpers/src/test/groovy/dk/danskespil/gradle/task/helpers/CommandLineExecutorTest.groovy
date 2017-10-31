package dk.danskespil.gradle.task.helpers

import dk.danskespil.gradle.test.spock.helpers.TemporaryFolderSpecification
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Unroll

class CommandLineExecutorTest extends TemporaryFolderSpecification {
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
        def ex = GradleServiceExecuteOnOSFactory.instance.createService(project)

        then:
        ex

        where:
        command << ["terraform --version"]
    }
}
