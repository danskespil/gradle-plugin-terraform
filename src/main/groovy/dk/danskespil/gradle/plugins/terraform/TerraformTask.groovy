package dk.danskespil.gradle.plugins.terraform

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal

abstract class TerraformTask extends DefaultTask {
    abstract String getDescription()
    String group = "Terraform"

    @Internal
    CommandLine commandLine = new CommandLine()

}
