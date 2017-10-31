package dk.danskespil.gradle.plugins.terraform.plugin

import dk.danskespil.gradle.plugins.helpers.BaseSpecification
import spock.lang.FailsWith

class TerraformCleanTest extends BaseSpecification {
    def "Clean is only executed when there are files to delete"() {
    }

    @FailsWith(RuntimeException)
    def "Clean can automatically find the output files from Plan, if present"() {
    }
}
