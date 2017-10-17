package dk.danskespil.gradle.plugins.terraform.tasks

import dk.danskespil.gradle.plugins.helpers.DSSpecification
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Unroll

class ValidateTest extends DSSpecification {
    def "When calling custom terraform task, the executed commandline looks as expected"() {
        given:
        buildFile << """
          plugins {
              id 'dk.danskespil.gradle.plugins.terraform'
          }

          task cut(type: dk.danskespil.gradle.plugins.terraform.Validate)
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

          task cut(type: dk.danskespil.gradle.plugins.terraform.Validate)
        """
        File monitoredFile = createNewPath(extensionExample) << "content"
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
}
