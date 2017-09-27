package dk.danskespil.gradle.plugins.terraform

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec

/**
 * Monitors a directory that has a remote representation and calls terraform init in that directory if no
 *
 *   .terraform/terraform.tfstate
 *
 * file is present
 */
class AutoInit extends DefaultTask {
    @Internal
    //                         v--- file can not be null according to api
    File stateFile = project.file('.terraform/terraform.tfstate')

    AutoInit() {
        outputs.upToDateWhen {
            stateFile.exists()
        }
    }

    @TaskAction
    void action() {
        if (classIsBeingRunDuringTest()) {
            println "executing action"
        } else {
            project.exec { ExecSpec e ->
                e.commandLine 'terraform', 'init'
            }
        }
    }

    // START Hack to mock custom test classes
    private boolean classIsBeingRunDuringTest() {
        return project.file(testFlagFilePath()).exists()
    }

    static String testFlagFilePath() {
        "${AutoInit.class.name}.testing"
    }
    // END Hack to mock custom test classess
}
