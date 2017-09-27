package dk.danskespil.gradle.plugins.terraform

import dk.danskespil.gradle.plugins.helpers.DSSpecification
import org.gradle.testkit.runner.TaskOutcome

class GenerateTest extends DSSpecification {
    def "basic test"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          import dk.danskespil.gradle.plugins.terraform.Generate

          task generateVersionFile(type: Generate) {
              version = '2.0'
              outputFile = file("\$project.buildDir/version.txt")
          }
           
          task showContents {
            doLast {
              println generateVersionFile.outputFile.text
            }  
          }
          showContents.dependsOn generateVersionFile
"""

        when:
        def build1 = buildWithTasks(':showContents')
        def build2 = buildWithTasks(':showContents')

        then:
        build1.task(':generateVersionFile').outcome == TaskOutcome.SUCCESS
        build2.task(':generateVersionFile').outcome == TaskOutcome.UP_TO_DATE
    }
}
