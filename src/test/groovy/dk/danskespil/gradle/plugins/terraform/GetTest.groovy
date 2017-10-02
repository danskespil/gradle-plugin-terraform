package dk.danskespil.gradle.plugins.terraform

import dk.danskespil.gradle.plugins.helpers.DSSpecification

class GetTest extends DSSpecification {
    def "when no modules are present, task is called"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          
          task cut(type: dk.danskespil.gradle.plugins.terraform.Get)
        """

        when:
        def result = buildWithTasks('cut')

        then:
        result.output.contains('terraform get')
    }

}
