package dk.danskespil.gradle.plugins.terraform.tasks

import dk.danskespil.gradle.test.spock.helpers.TemporaryFolderSpecification

class ApplyTest extends TemporaryFolderSpecification {
    def "When calling custom terraform apply task, the executed commandline looks as expected"() {
        given:
        buildFile << """
          plugins {
              id 'dk.danskespil.gradle.plugins.terraform'
          }
  
          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Apply)
        """

        when:
        def build = buildWithTasks(':cut')

        then:
        build.output.contains('terraform apply')
    }

    def "When calling the terraform apply task that is provided by the plugin, the executed commandline looks as expected"() {
        given:
        buildFile << """
          plugins {
              id 'dk.danskespil.gradle.plugins.terraform'
          }
        """

        when:
        def build = buildWithTasks(':tfApply')

        then:
        build.output.contains('terraform apply')
    }

    def "When calling custom terraform apply task, you can configure which plan file you want to read from "() {
        given:
        buildFile << """
          plugins {
            id 'dk.danskespil.gradle.plugins.terraform'

          }
          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Apply) {
            plan = file('plan.bin')
          }
        """
        createPathInTemporaryFolder('plan.bin') << "binary-content"

        when:
        def build = buildWithTasks(':cut')

        then:
        build.output.contains('terraform apply plan.bin')
    }

}
