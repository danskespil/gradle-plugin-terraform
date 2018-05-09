package dk.danskespil.gradle.plugins.terraform.tasks

import dk.danskespil.gradle.plugins.helpers.BaseSpecification
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Unroll

class ValidateTest extends BaseSpecification {
    def "When calling custom terraform task, the executed commandline looks as expected"() {
        given:
        buildFile << """
          plugins {
              id 'dk.danskespil.gradle.plugins.terraform'
          }

          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Validate)
        """

        when:
        def build = buildWithTasks(':cut')

        then:
        build.output.contains('terraform validate')
    }

    @Unroll
    def "Only when files with extension #extensionExample change, task is executed"() {
        given:
        buildFile << """
          plugins {
              id 'dk.danskespil.gradle.plugins.terraform'
          }

          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Validate)
        """
        File monitoredFile = createPathInTemporaryFolder(extensionExample) << "content"
        def build1 = buildWithTasks(':cut')

        when:
        def build2 = buildWithTasks(':cut')
        monitoredFile.delete()
        def build3 = buildWithTasks(':cut')

        then:
        build1.task(':cut').outcome == TaskOutcome.SUCCESS
        build2.task(':cut').outcome == TaskOutcome.UP_TO_DATE
        build3.task(':cut').outcome == TaskOutcome.SUCCESS

        where:
        extensionExample << ['file.tf', 'file.tpl']
    }

    def "Validate variables are included"() {
        given:
        buildFile << """
          plugins {
              id 'dk.danskespil.gradle.plugins.terraform'
          }

          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Validate) {
              var('domain', 'example.com')
          }
        """

        when:
        def build = buildWithTasks(':cut')

        then:
        build.output.contains('terraform validate -var domain=example.com')
    }

    def "Validate variable files are included"() {
        given:
        buildFile << """
          plugins {
              id 'dk.danskespil.gradle.plugins.terraform'
          }

          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Validate) {
              varfile file('common.tfvars')
          }
        """

        when:
        def build = buildWithTasks(':cut')

        then:
        build.output.find(/terraform validate -var-file=.*\/common\.tfvars/)
    }

    def "Validate variable file change causes task to run again"() {
        given:
        File simulatedVariableFile = createPathInTemporaryFolder('vars.tfvars')
        simulatedVariableFile << "domain = example.com"

        buildFile << """
          plugins {
              id 'dk.danskespil.gradle.plugins.terraform'
          }

          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Validate) {
              varfile file("${simulatedVariableFile}")
          }
        """

        when:
        def build1 = buildWithTasks('cut')

        def build2 = buildWithTasks('cut')
        simulatedVariableFile << "email = dev@example.com"

        def build3 = buildWithTasks('cut')

        def build4 = buildWithTasks('cut')

        then:
        build1.task(':cut').outcome == TaskOutcome.SUCCESS
        build2.task(':cut').outcome == TaskOutcome.UP_TO_DATE
        build3.task(':cut').outcome == TaskOutcome.SUCCESS
        build4.task(':cut').outcome == TaskOutcome.UP_TO_DATE
    }

    def "Validate variable files and independent variables are included"() {
        given:
        buildFile << """
          plugins {
              id 'dk.danskespil.gradle.plugins.terraform'
          }

          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Validate) {
              varfile file('common.tfvars')
              varfile file('dev.tfvars')
              var('domain', 'example.com')
              var('email', 'dev@example.com')
          }
        """

        when:
        def build = buildWithTasks(':cut')

        then:
        build.output.find(/terraform validate -var-file=.*?\/common\.tfvars -var-file=.*?\/dev\.tfvars -var domain=example.com -var email=dev@example.com/)
    }
}
