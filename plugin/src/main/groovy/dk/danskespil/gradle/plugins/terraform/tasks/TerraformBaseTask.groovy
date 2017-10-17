package dk.danskespil.gradle.plugins.terraform.tasks

import dk.danskespil.gradle.plugins.terraform.plugin.CommandLine
import dk.danskespil.gradle.task.helpers.AbstractCommandLineExecutor
import dk.danskespil.gradle.task.helpers.CommandLineExecutorFactory
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal

abstract class TerraformBaseTask extends DefaultTask {
    @Internal
    abstract String getDescription()
    @Internal
    String group = "Terraform"

    @Internal
    CommandLine commandLine = new CommandLine()
    @Internal
    AbstractCommandLineExecutor executor = CommandLineExecutorFactory.createExecutor(project)
}
