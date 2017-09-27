package dk.danskespil.gradle.plugins.terraform

import dk.danskespil.gradle.plugins.terraform.dk.danskespil.gradle.plugins.dk.danskespil.gradle.plugins.helpers.DSSpecification
import org.gradle.testkit.runner.TaskOutcome

class AutoInitTest extends DSSpecification {
    def "When user adds this task to a directory, terraform remote state is initialized automatically if its initialized"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          
          import dk.danskespil.gradle.plugins.terraform.AutoInit
          task autoInit(type: AutoInit)
          
        """
        // START Hack to mock custom test classess
        buildFile = testProjectDir.newFile(AutoInit.testFlagFilePath())
        // END Hack to mock custom test classess

        when:
        def result = buildWithTasks('autoInit')

        then:
        result
        result.task(':autoInit').outcome == TaskOutcome.SUCCESS
        result.output.contains('executing action')
    }


    def "When user adds this task to a directory, terraform remote state is not initialized automatically if its not initialized"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          
          import dk.danskespil.gradle.plugins.terraform.AutoInit
          task autoInit(type: AutoInit)
        """
        runClassUnderTestInTestMode()
        def build1 = buildWithTasks('autoInit')

        when:
        simulateTerraformInitIsBeingRun()
        def build2 = buildWithTasks('autoInit')

        then:
        build1
        build1.task(':autoInit').outcome == TaskOutcome.SUCCESS
        build1.output.contains('executing action')

        build2
        build2.task(':autoInit').outcome == TaskOutcome.UP_TO_DATE
        !build2.output.contains('executing action')
    }

    private void simulateTerraformInitIsBeingRun() {
        createNewPath('.terraform/terraform.tfstate')
    }

    private void runClassUnderTestInTestMode() {
        buildFile = testProjectDir.newFile(AutoInit.testFlagFilePath())
    }
}
