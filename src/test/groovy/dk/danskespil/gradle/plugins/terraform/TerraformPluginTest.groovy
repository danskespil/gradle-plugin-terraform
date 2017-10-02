package dk.danskespil.gradle.plugins.terraform

import dk.danskespil.gradle.plugins.helpers.DSSpecification
import org.gradle.testkit.runner.TaskOutcome

class TerraformPluginTest extends DSSpecification {
    def "I can apply the plugin"() {
        given:
        buildFile << """
            plugins {
                id 'dk.danskespil.gradle.plugins.terraform'
            }
        """

        when:
        def result = buildWithTasks('tasks')

        then:
        result
    }

    def "I have plan task in plugin"() {
        given:
        buildFile << """
        plugins {
            id 'dk.danskespil.gradle.plugins.terraform'
        }
        """

        when:
        def result = buildWithTasks('tfPlan')

        then:
        result
        result.task(':tfPlan').outcome == TaskOutcome.SUCCESS
    }
}
