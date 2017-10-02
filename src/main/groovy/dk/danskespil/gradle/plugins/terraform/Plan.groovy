package dk.danskespil.gradle.plugins.terraform

import dk.danskespil.gradle.plugins.helpers.dscommandlineexecutor.DSCommandLineTestExecutor
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*
import org.gradle.process.ExecSpec

class Plan extends DefaultTask {
    @InputFiles
    FileCollection terraformFiles = project.fileTree('.').include('*.tf')
    @InputFiles
    FileCollection templateFiles = project.fileTree('.').include('*.tpl')
    @Optional
    @OutputFile
    // -out=path           Write a plan file to the given path. This can be used as input to the "apply" command.
    File tfNativeArgOut
    @Optional
    @OutputFile
    File outAsText
    @Internal
    CommandLine commandLine = new CommandLine()

    @TaskAction
    action() {
        commandLine.addToEnd('terraform')
        commandLine.addToEnd('plan')
        if (tfNativeArgOut) {
            commandLine.addToEnd("out=${tfNativeArgOut.name}")
        }
        //DSCommandLineExecutorFactory.createExecutor(project).execute("terraform plan ${outAsParam()}")
        new DSCommandLineTestExecutor(project).executeExecSpec(this, { ExecSpec e ->

            e.commandLine this.commandLine
        })
    }
}
