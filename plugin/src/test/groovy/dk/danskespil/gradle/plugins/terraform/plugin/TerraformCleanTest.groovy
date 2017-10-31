package dk.danskespil.gradle.plugins.terraform.plugin

import dk.danskespil.gradle.test.spock.helpers.TemporaryFolderSpecification
import spock.lang.FailsWith

class TerraformCleanTest extends TemporaryFolderSpecification {
    def "Clean is only executed when there are files to delete"() {
    }

    @FailsWith(RuntimeException)
    def "Clean can automatically find the output files from Plan, if present"() {
    }
}
