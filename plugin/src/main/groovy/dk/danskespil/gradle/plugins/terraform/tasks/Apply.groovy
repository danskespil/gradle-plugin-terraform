package dk.danskespil.gradle.plugins.terraform.tasks

import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec

/**
 * Wraps cli command: terraform apply
 */
class Apply extends TerraformBaseTask {
    @Optional
    @InputFile
    File plan

    @TaskAction
    action() {
        commandLine.addToEnd('terraform', 'apply')

        if (plan) {
            commandLine.addToEnd(plan.name)
        }

        executor.executeExecSpec(this, { ExecSpec e ->
            e.commandLine this.commandLine
        })
    }

    @Override
    String getDescription() {
        return """Wraps cli command: terraform apply"""
    }
}
