package dk.danskespil.gradle.plugins.terraform.tasks

import dk.danskespil.gradle.plugins.helpers.BaseSpecification
import org.gradle.testkit.runner.TaskOutcome

class GetTest extends BaseSpecification {
    def "when no modules are present, task is called"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          
          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Get)
        """

        when:
        def result = buildWithTasks('cut')

        then:
        result.output.contains('terraform get')
    }

    def "only when modules are removed, task is executed"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          
          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Get) {
            doLast {
              // Since terraform is not executed during test, I am faking creation of the outputfiles
              project.file('.terraform/').mkdir()
              project.file('.terraform/modules/').mkdir()
              project.file('.terraform/modules/a-module').createNewFile()
            }
          }
        """
        def build1 = buildWithTasks('cut')
        def build2 = buildWithTasks('cut')

        when:
        findPathInTemporaryFolder("/.terraform/modules").deleteDir()
        def build3 = buildWithTasks('cut')

        then:
        build1.output.contains('terraform get')
        build1.task(':cut').outcome == TaskOutcome.SUCCESS
        build2.task(':cut').outcome == TaskOutcome.UP_TO_DATE
        build3.task(':cut').outcome == TaskOutcome.SUCCESS
    }
}
