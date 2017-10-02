package dk.danskespil.gradle.plugins.terraform

import dk.danskespil.gradle.plugins.helpers.DSSpecification
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Unroll

class PlanTest extends DSSpecification {
// This is what we are coding against:
//    task plan {
//        inputs.files fileTree("$projectDir").include('*.tf')
//        inputs.files fileTree("$projectDir").include('*.tpl')
//        inputs.files get.outputs.files
//        inputs.files remoteConfig.outputs.files
//
//        outputs.files file("${projectDir}/plan-output.bin")
//        outputs.files file("${projectDir}/plan-output")
//
//        dependsOn validate, ":docker-certificates:build"
//
//        doLast {
//            TerraformStatic.plan(project)
//        }
//    }

    def "Can write plan output to a file"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          
          import dk.danskespil.gradle.plugins.terraform.Plan
          task cut(type: Plan)
        """

        when:
        def result = buildWithTasks('cut')

        then:
        result.output.contains('terraform plan')
    }

    def "Plan picks up any .tf files"() {
        given:
        createNewPath('terraform1.tf')
        createNewPath('terraform2.tf')

        when:
        Project project = ProjectBuilder.builder()
                .withProjectDir(testProjectDir.root)
                .build()
        Plan cut = project.task('cut', type: Plan)

        then:
        project
        cut
        !cut.getTerraformFiles().empty
        cut.terraformFiles.files.size() == 2
    }

    def "Build is not performed when .tf files do not change"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          
          task cut(type: dk.danskespil.gradle.plugins.terraform.Plan)
        """

        createNewPath('terraform.tf')

        when:
        def build1 = buildWithTasks('cut')
        def build2 = buildWithTasks('cut')

        then:
        build1
        build2
        build1.task(':cut').outcome == TaskOutcome.SUCCESS
        build2.task(':cut').outcome == TaskOutcome.UP_TO_DATE
    }

    @Unroll
    def "Build is performed when files with #extension file change"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          
          task cut(type: dk.danskespil.gradle.plugins.terraform.Plan)
        """

        def fileWithMonitoredExtension = createNewPath("terraform.${extension}")

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
          
          task cut(type: dk.danskespil.gradle.plugins.terraform.Plan)
        """

        // Simulate input file
        File simulatedInputFile = createNewPath(inputfile)
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
          
          task cut(type: dk.danskespil.gradle.plugins.terraform.Plan) {
            tfNativeArgOut = file('plan-output.bin')
            doLast {
              // Since terraform is not executed during test, I am faking creation of the outputfile   
              file('plan-output.bin').createNewFile()
            }
          }
        """

        // Simulate input file
        File simulatedInputFile = createNewPath('terraform.tf')

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
