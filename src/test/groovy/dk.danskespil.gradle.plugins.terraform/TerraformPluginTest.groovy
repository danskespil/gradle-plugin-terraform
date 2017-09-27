package dk.danskespil.gradle.plugins.terraform

import dk.danskespil.gradle.plugins.terraform.dk.danskespil.gradle.plugins.dk.danskespil.gradle.plugins.helpers.DSSpecification

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
}
