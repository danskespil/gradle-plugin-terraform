package dk.danskespil.gradle.plugins.terraform.tasks

import dk.danskespil.gradle.plugins.helpers.BaseSpecification
import org.gradle.testkit.runner.TaskOutcome

class InitTest extends BaseSpecification {
    def "init can be called"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          
          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Init)
        """

        when:
        def build = buildWithTasks(':cut')

        then:
        build
        build.task(':cut').outcome == TaskOutcome.SUCCESS
    }

    def "Init is called once when .terraform/terraform.tfstate is not present"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          
          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Init) {
            doLast {
              mkdir('.terraform')
              file('.terraform/terraform.tfstate').createNewFile()
            }
          }
        """
        when:
        def build1 = buildWithTasks(':cut')
        def build2 = buildWithTasks(':cut')

        then:
        build1.task(':cut').outcome == TaskOutcome.SUCCESS
        build2.task(':cut').outcome == TaskOutcome.UP_TO_DATE

    }

    def "if state file is present, gradle should not keep executing the task"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          
          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Init) {
            doLast {
              mkdir('.terraform')
              file('.terraform/terraform.tfstate').createNewFile()
            }
          }
        """

        when:
        def build1 = buildWithTasks(':cut')
        def build2 = buildWithTasks(':cut')
        def build3 = buildWithTasks(':cut')

        then:
        build3.task(':cut').outcome == TaskOutcome.UP_TO_DATE
    }

    def "Init task may be called when .terraform/terraform.tfstate is not present, but terraform init is never called"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          
          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Init) {
            doLast {
              mkdir('.terraform')
              file('.terraform/terraform.tfstate').createNewFile()
            }
          }
        """

        when:
        File stateFile = createPathInTemporaryFolder('.terraform/terraform.tfstate')
        def build1 = buildWithTasks(':cut')
        addSomethingToTheFile(stateFile)
        def build2 = buildWithTasks(':cut')
        def build3 = buildWithTasks(':cut')
        def build4 = buildWithTasks(':cut')

        then:
        build1
        !build1.output.contains('terraform')
        build1.task(':cut').outcome == TaskOutcome.SUCCESS

        !build2.output.contains('terraform')
        !build3.output.contains('terraform')
        !build4.output.contains('terraform')
    }

    private def addSomethingToTheFile(File file) {
        file << "something"
    }
}
