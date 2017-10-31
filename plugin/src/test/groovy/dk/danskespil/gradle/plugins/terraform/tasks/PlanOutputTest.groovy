package dk.danskespil.gradle.plugins.terraform.tasks

import dk.danskespil.gradle.test.spock.helpers.TemporaryFolderSpecification
import org.gradle.testkit.runner.TaskOutcome

class PlanOutputTest extends TemporaryFolderSpecification {
    def "plan task provided by plugin creates binary files as output by default"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
        """

        when:
        def result = buildWithTasks('tfPlan')

        then:
        result.task(':tfPlan').outcome == TaskOutcome.SUCCESS
        result.output.contains('terraform plan')
        result.output.contains('-out')
    }

    def "plan task provided by plugin creates text files as output by default"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
        """

        when:
        def result = buildWithTasks('tfPlan')

        then:
        existsInTemporaryFolder('/plan-output')
    }

    def "plan task provided by plugin writes to stdout by default"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
        """

        when:
        def result = buildWithTasks('tfPlan')

        then:
        result.output.contains('terraform plan')
    }

    def "custom plan task can write plan output to a file"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          
          import dk.danskespil.gradle.plugins.terraform.tasks.Plan
          task cut(type: Plan)
        """

        when:
        def result = buildWithTasks('cut')

        then:
        result.output.contains('terraform plan')
    }

    def "custom plan task can save output from plan in a text file"() {
        given:
        buildFile << """
          plugins {
              id 'dk.danskespil.gradle.plugins.terraform'
          }

          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Plan) {
             outAsText=file('plan-output')
          }
        """

        when:
        def result = buildWithTasks('cut')

        then:
        result
        result.task(':cut').outcome == TaskOutcome.SUCCESS
        new File(testProjectDir.root.getAbsolutePath() + "/plan-output").exists()
    }

    def "custom plan task writes to stdout by default"() {
        given:
        buildFile << """
          plugins {
              id 'dk.danskespil.gradle.plugins.terraform'
          }

          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Plan) {
             out=file('plan-output')
          }
        """

        when:
        def result = buildWithTasks('cut')

        then:
        result
        result.task(':cut').outcome == TaskOutcome.SUCCESS
        result.output.contains('terraform plan')
    }

    def "When saving output from custom plan task to a text file, that text file contains the expected output"() {
        given:
        buildFile << """
          plugins {
              id 'dk.danskespil.gradle.plugins.terraform'
          }

          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Plan) {
             outAsText=file('plan-output')
          }
        """

        when:
        def result = buildWithTasks('cut')

        then:
        result
        result.task(':cut').outcome == TaskOutcome.SUCCESS
        existsInTemporaryFolder("/plan-output")
        file("/plan-output").text.contains('terraform plan')
    }

    def "only when file with textual output is deleted, custom plan task its rebuild"() {
        given:
        buildFile << """
          plugins {
              id 'dk.danskespil.gradle.plugins.terraform'
          }

          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Plan) {
             outAsText=file('plan-output')
          }
        """

        when:
        def build1 = buildWithTasks('cut')
        def build2 = buildWithTasks('cut')
        file('/plan-output').delete()
        def build3 = buildWithTasks('cut')

        then:
        build1.task(':cut').outcome == TaskOutcome.SUCCESS
        build2.task(':cut').outcome == TaskOutcome.UP_TO_DATE
        build3.task(':cut').outcome == TaskOutcome.SUCCESS
    }
}
