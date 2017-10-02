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
        File versionFile = new File(testProjectDir.root.getAbsolutePath() + '/build/version.txt')
        versionFile.delete()
        def build3 = buildWithTasks(':showContents')

        then:
        build1.task(':generateVersionFile').outcome == TaskOutcome.SUCCESS
        build2.task(':generateVersionFile').outcome == TaskOutcome.UP_TO_DATE
        build3.task(':generateVersionFile').outcome == TaskOutcome.SUCCESS
    }

    def "When inputfile changes build is triggered again"() {
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
        File inputFile = createNewPath('some.inputfile')
        def build1 = buildWithTasks(':showContents')
        def build2 = buildWithTasks(':showContents')


        when:
        inputFile << "new contennt"
        def build3 = buildWithTasks(':showContents')
        def build4 = buildWithTasks(':showContents')

        then:
        build1.task(':generateVersionFile').outcome == TaskOutcome.SUCCESS
        build2.task(':generateVersionFile').outcome == TaskOutcome.UP_TO_DATE
        build3.task(':generateVersionFile').outcome == TaskOutcome.SUCCESS
        build4.task(':generateVersionFile').outcome == TaskOutcome.UP_TO_DATE
    }

    def "When inputfile is deleted, build is triggered again"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          import dk.danskespil.gradle.plugins.terraform.Generate

          task generateVersionFile(type: Generate) {
              version = '2.0'
          }
           
          task showContents {
            doLast {
              println generateVersionFile.outputFile.text
            }  
          }
          showContents.dependsOn generateVersionFile
"""
        File inputFile = createNewPath('some.inputfile')
        inputFile << "new contennt"
        def build1 = buildWithTasks(':showContents')
        def build2 = buildWithTasks(':showContents')


        when:
        inputFile.delete()
        def build3 = buildWithTasks(':showContents')
        def build4 = buildWithTasks(':showContents')

        then:
        build1.task(':generateVersionFile').outcome == TaskOutcome.SUCCESS
        build2.task(':generateVersionFile').outcome == TaskOutcome.UP_TO_DATE
        build3.task(':generateVersionFile').outcome == TaskOutcome.SUCCESS
        build4.task(':generateVersionFile').outcome == TaskOutcome.UP_TO_DATE
    }
}
