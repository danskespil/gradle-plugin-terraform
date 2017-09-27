package dk.danskespil.gradle.plugins.terraform

import dk.danskespil.gradle.plugins.helpers.DSSpecification
import org.gradle.testkit.runner.TaskOutcome

class TerraformInitTest extends DSSpecification {
    def "Whoever executes TerraformInit task can read output on stdout if the command fails"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          import dk.danskespil.gradle.plugins.terraform.TerraformInit
          task dummyInit(type: TerraformInit) {
            letTestsForceAFailureBySettingThisToTrue = true
          }
        """
        when:
        def result = buildAndFailWithTasks('dummyInit')

        then:
        result.task(":dummyInit").outcome == TaskOutcome.FAILED
        result.output.contains("Usage")

    }

    def "init task is available in scope"() {
        given:
        buildFile << """
            plugins {
                id 'dk.danskespil.gradle.plugins.terraform'
            }
            
            task dummyInit(type:dk.danskespil.gradle.plugins.terraform.TerraformInit)
            dummyInit.enabled=false
        """

        when:
        def result = buildWithTasks('dummyInit')

        then:
        result.task(':dummyInit').outcome == TaskOutcome.SKIPPED
    }
}
