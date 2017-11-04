package dk.danskespil.gradle.plugins.terraform.plugin

import dk.danskespil.gradle.plugins.helpers.BaseSpecification
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Ignore

class MultiProjectTest extends BaseSpecification {
    def "Apply plugin to sub project"() {
        given:
        buildFile << """
        plugins { 
          id 'dk.danskespil.gradle.plugins.terraform' apply false
        }        
        """
        createPathInTemporaryFolder('settings.gradle')
        createSubProjectAndApplyPluginThere('project1')

        when:
        def build = buildWithTasks('project1:tfPlan')

        then:
        build.task(':project1:tfPlan').outcome == TaskOutcome.SUCCESS
    }

    def "Apply plugin to sub projects"() {
        given:
        buildFile << """
        plugins { 
          id 'dk.danskespil.gradle.plugins.terraform' apply false
        }        
        """

        createPathInTemporaryFolder('settings.gradle')
        createSubProjectAndApplyPluginThere('project1')
        createSubProjectAndApplyPluginThere('project2')

        when:
        def build1 = buildWithTasks('project1:tfPlan')
        def build2 = buildWithTasks('project2:tfPlan')

        then:
        build1.task(':project1:tfPlan').outcome == TaskOutcome.SUCCESS
        build2.task(':project2:tfPlan').outcome == TaskOutcome.SUCCESS
    }

    private void createSubProjectAndApplyPluginThere(String subProjectName) {
        fileInTemporaryFolder('settings.gradle') << "include '${subProjectName}'" << "${String.format("%n")}"
        createPathInTemporaryFolder("${subProjectName}/build.gradle") << "apply plugin:'dk.danskespil.gradle.plugins.terraform'"
    }
}
