package dk.danskespil.gradle.plugins.terraform

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec

/**
 * Do whatever terraform init does
 */
class TerraformInit extends DefaultTask {
    @Internal
    String group = 'Terraform'
    @Internal
    String description = 'calls terraform init'
    @Internal
    String subCommand = 'init'
    boolean letTestsForceAFailureBySettingThisToTrue = false


    @Input
    boolean getLetTestsForceAFailureBySettingThisToTrue() {
        return letTestsForceAFailureBySettingThisToTrue
    }

    @TaskAction
    void action() {
        if (letTestsForceAFailureBySettingThisToTrue) {
            subCommand = "bogus"
        }

        new ByteArrayOutputStream().withStream { os ->
            project.exec { ExecSpec e ->
                e.commandLine 'terraform', subCommand
            }
        }
    }
}
