package dk.danskespil.gradle.plugins.terraform

import dk.danskespil.gradle.plugins.terraform.plugin.CommandLine
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal

abstract class TerraformBaseTask extends DefaultTask {
    abstract String getDescription()
    String group = "Terraform"

    @Internal
    CommandLine commandLine = new CommandLine()

}
