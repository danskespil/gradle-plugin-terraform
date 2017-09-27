package dk.danskespil.gradle.plugins.terraform

import dk.danskespil.gradle.plugins.terraform.dk.danskespil.gradle.plugins.dk.danskespil.gradle.plugins.helpers.DSSpecification
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Ignore
import spock.lang.Unroll

class PlanTest extends DSSpecification {
    def "I can write plan output to a file"() {
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

    def "build is performed again if inputfile is changed after first build"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          
          task cut(type: dk.danskespil.gradle.plugins.terraform.Plan)
        """

        // Simulate input file
        File simulatedInputFile = createNewPath('terraform.tf')
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
    }

    @Unroll
    def "build is performed again if file '#outputfile'  is deleted by user after first build"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          
          task cut(type: dk.danskespil.gradle.plugins.terraform.Plan)
        """

        // Simulate input file
        File simulatedInputFile = createNewPath('terraform.tf')
        simulatedInputFile << "simulated content"
        File simulatedOutputFile = createNewPath('plan-output')
        simulatedOutputFile << "simulated content"

        when:
        def build1 = buildWithTasks('cut')

        def build2 = buildWithTasks('cut')
        simulatedOutputFile << 'new content'
        //simulatedOutputFile.delete()

        def build3 = buildWithTasks('cut')

        //def build4 = buildWithTasks('cut')

        then:
        simulatedOutputFile.exists()
        build1.task(':cut').outcome == TaskOutcome.SUCCESS
        simulatedOutputFile.text.contains('new content')
        build2.task(':cut').outcome == TaskOutcome.UP_TO_DATE
        build3.task(':cut').outcome == TaskOutcome.SUCCESS
        //build4.task(':cut').outcome == TaskOutcome.UP_TO_DATE

        where:
        outputfile << ['plan-output']
    }
}
