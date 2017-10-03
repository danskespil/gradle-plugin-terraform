package dk.danskespil.gradle.plugins.terraform.tasks

import dk.danskespil.gradle.plugins.helpers.DSSpecification
import org.gradle.testkit.runner.TaskOutcome

class PlanOutputTest extends DSSpecification {

    def "Can save output from plan in a text file"() {
        given:
        buildFile << """
          plugins {
              id 'dk.danskespil.gradle.plugins.terraform'
          }

          task cut(type: dk.danskespil.gradle.plugins.terraform.Plan) {
             out=file('plan-output')
          }
        """

        when:
        def result = buildWithTasks('cut')

        then:
        result
        result.task(':cut').outcome == TaskOutcome.SUCCESS
        new File(testProjectDir.root.getAbsolutePath() + "/plan-output").exists()
    }

    def "When saving output from plan to a text file, its also echoed to stdout, so the user can see it"() {
        given:
        buildFile << """
          plugins {
              id 'dk.danskespil.gradle.plugins.terraform'
          }

          task cut(type: dk.danskespil.gradle.plugins.terraform.Plan) {
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

    def "When saving output from plan to a text file, that text file contains the expected output"() {
        given:
        buildFile << """
          plugins {
              id 'dk.danskespil.gradle.plugins.terraform'
          }

          task cut(type: dk.danskespil.gradle.plugins.terraform.Plan) {
             out=file('plan-output')
          }
        """

        when:
        def result = buildWithTasks('cut')

        then:
        result
        result.task(':cut').outcome == TaskOutcome.SUCCESS
        new File(testProjectDir.root.getAbsolutePath() + "/plan-output").exists()
        new File(testProjectDir.root.getAbsolutePath() + "/plan-output").text.contains('terraform plan')
    }

    def "only when file with textual output is deleted, its rebuild"() {
        given:
        buildFile << """
          plugins {
              id 'dk.danskespil.gradle.plugins.terraform'
          }

          task cut(type: dk.danskespil.gradle.plugins.terraform.Plan) {
             out=file('plan-output')
          }
        """

        when:
        def build1 = buildWithTasks('cut')
        def build2 = buildWithTasks('cut')
        new File(testProjectDir.root.getAbsolutePath() + '/plan-output').delete()
        def build3 = buildWithTasks('cut')

        then:
        build1.task(':cut').outcome == TaskOutcome.SUCCESS
        build2.task(':cut').outcome == TaskOutcome.UP_TO_DATE
        build3.task(':cut').outcome == TaskOutcome.SUCCESS
    }
}