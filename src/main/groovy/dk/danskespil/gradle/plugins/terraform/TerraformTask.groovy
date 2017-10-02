package dk.danskespil.gradle.plugins.terraform

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal

class TerraformTask extends DefaultTask {
    @Internal
    CommandLine commandLine = new CommandLine()

}
