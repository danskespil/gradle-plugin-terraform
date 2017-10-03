package dk.danskespil.gradle.plugins.terraform

import dk.danskespil.gradle.plugins.helpers.dscommandlineexecutor.DSCommandLineExecutorFactory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec

/**
 * Wraps cli command: terraform apply
 */
class Apply extends TerraformTask {
    @Optional
    @InputFile
    File plan

    @Override
    String getDescription() {
        return """Wraps cli command: terraform apply"""
    }

    @TaskAction
    action() {
        commandLine.addToEnd('terraform', 'apply')

        if (plan) {
            commandLine.addToEnd(plan.name)
        }

        DSCommandLineExecutorFactory.createExecutor(project).executeExecSpec(this, { ExecSpec e ->
            e.commandLine this.commandLine
        })
    }
}
