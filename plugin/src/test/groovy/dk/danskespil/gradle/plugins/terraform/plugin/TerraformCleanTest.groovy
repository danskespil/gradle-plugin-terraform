package dk.danskespil.gradle.plugins.terraform.plugin

import dk.danskespil.gradle.plugins.helpers.DSSpecification
import org.junit.Assert
import org.spockframework.runtime.SpockAssertionError
import spock.lang.FailsWith

class TerraformCleanTest extends DSSpecification {
    def "Clean is only executed when there are files to delete"() {
    }

    @FailsWith(RuntimeException)
    def "Clean can automatically find the output files from Plan, if present"() {
    }
}
