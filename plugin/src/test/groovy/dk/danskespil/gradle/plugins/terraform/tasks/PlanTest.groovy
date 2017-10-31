package dk.danskespil.gradle.plugins.terraform.tasks

import dk.danskespil.gradle.test.spock.helpers.TemporaryFolderSpecification
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Unroll

class PlanTest extends TemporaryFolderSpecification {
// This is what we are coding against:
//    task plan {
//  Implemented      inputs.files fileTree("$projectDir").include('*.tf')
//  Implemented      inputs.files fileTree("$projectDir").include('*.tpl')
//  Implemented      inputs.files get.outputs.files
//  Not Implemented      inputs.files remoteConfig.outputs.files
//
//  Implemented      outputs.files file("${projectDir}/plan-output.bin")
//  Implemented      outputs.files file("${projectDir}/plan-output")
//
//        dependsOn validate, ":docker-certificates:build"
//
//        doLast {
//            TerraformStatic.plan(project)
//        }
//    }

    def "Plan picks up any .tf and .tpl files"() {
        given:
        createPathInTemporaryFolder('terraform1.tf')
        createPathInTemporaryFolder('terraform2.tpl')

        when:
        Project project = ProjectBuilder.builder()
                .withProjectDir(testProjectDir.root)
                .build()
        dk.danskespil.gradle.plugins.terraform.tasks.Plan cut = project.task('cut', type: dk.danskespil.gradle.plugins.terraform.tasks.Plan)

        then:
        project
        cut
        !cut.getTerraformFiles().empty
        cut.terraformFiles.files.size() == 2
    }

    @Unroll
    def "Build is performed when files with #extension file change"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          
          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Plan)
        """

        def fileWithMonitoredExtension = createPathInTemporaryFolder("terraform.${extension}")

        when:
        def build1 = buildWithTasks('cut')
        fileWithMonitoredExtension << "simulate new content"
        def build2 = buildWithTasks('cut')
        def build3 = buildWithTasks('cut')

        then:
        build1.task(':cut').outcome == TaskOutcome.SUCCESS
        build2.task(':cut').outcome == TaskOutcome.SUCCESS
        build3.task(':cut').outcome == TaskOutcome.UP_TO_DATE

        where:
        extension << ['.tf', '.tpl']

    }

    @Unroll
    def "build is performed again if file '#inputfile' is changed after first build"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          
          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Plan)
        """

        // Simulate input file
        File simulatedInputFile = createPathInTemporaryFolder(inputfile)
        simulatedInputFile << "simulated content"

        when:
        def build1 = buildWithTasks('cut')

        def build2 = buildWithTasks('cut')
        simulatedInputFile << 'new content'

        def build3 = buildWithTasks('cut')
        def build4 = buildWithTasks('cut')

        then:
        build1.task(':cut').outcome == TaskOutcome.SUCCESS
        build2.task(':cut').outcome == TaskOutcome.UP_TO_DATE
        build3.task(':cut').outcome == TaskOutcome.SUCCESS
        build4.task(':cut').outcome == TaskOutcome.UP_TO_DATE

        where:
        inputfile << ['terraform.tf']
    }

    @Unroll
    def "build is performed again if file '#outputfile' is deleted by user after first build"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          
          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Plan) {
            out = file('plan-output.bin')
            doLast {
              // Since terraform is not executed during test, I am faking creation of the outputfile   
              file('plan-output.bin').createNewFile()
            }
          }
        """

        // Simulate input file
        File simulatedInputFile = createPathInTemporaryFolder('terraform.tf')

        simulatedInputFile << "simulated content"

        when:
        def build1 = buildWithTasks('cut')

        def build2 = buildWithTasks('cut')
        new File(testProjectDir.root.getAbsolutePath() + '/plan-output.bin').delete()

        def build3 = buildWithTasks('cut')

        def build4 = buildWithTasks('cut')

        then:
        //simulatedOutputFile.exists()
        build1.task(':cut').outcome == TaskOutcome.SUCCESS
        //simulatedOutputFile.text.contains('new content')
        build2.task(':cut').outcome == TaskOutcome.UP_TO_DATE
        build3.task(':cut').outcome == TaskOutcome.SUCCESS
        build4.task(':cut').outcome == TaskOutcome.UP_TO_DATE

        where:
        outputfile << ['plan-output.bin']
    }

}
