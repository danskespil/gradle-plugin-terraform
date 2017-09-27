package dk.danskespil.gradle.plugins.terraform

import dk.danskespil.gradle.plugins.terraform.dk.danskespil.gradle.plugins.helpers.dscommandlineexecutor.DSCommandLineExecutorFactory
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class Plan extends DefaultTask {
    // -out=path           Write a plan file to the given path. This can be used as input to the "apply" command.
    String out = 'plan-output'
    FileCollection terraformFiles = project.fileTree('.').include('*.tf')
    FileCollection templateFiles = project.fileTree('.').include('*.tpl')
    File planOutput = project.file(out)

    @OutputFile
    File getPlanOutput() {
        return this.planOutput
    }

    @Input
    String getOut() {
        return this.out
    }

    @InputFiles
    FileCollection getTemplateFiles() {
        return this.templateFiles
    }

    @InputFiles
    FileCollection getTerraformFiles() {
        return this.terraformFiles
    }

    @TaskAction
    action() {
        DSCommandLineExecutorFactory.createExecutor(project).execute("terraform plan ${outAsParam()}")
    }

    String outAsParam() {
        def rv = ""
        if (out) {
            rv = "out=${out}"
        }
        rv
    }
}
