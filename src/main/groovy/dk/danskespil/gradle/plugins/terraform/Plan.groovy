package dk.danskespil.gradle.plugins.terraform

import dk.danskespil.gradle.plugins.helpers.dscommandlineexecutor.DSCommandLineExecutorFactory
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*
import org.gradle.process.ExecSpec

class Plan extends TerraformTask
{
    @InputFiles
    FileCollection terraformFiles = project.fileTree('.').include('*.tf')
    @InputFiles
    FileCollection templateFiles = project.fileTree('.').include('*.tpl')
    @Optional
    @OutputFile
    // From terraform --help plan
    // -out=path           Write a plan file to the given path. This can be used as input to the "apply" command.
    File tfNativeArgOut
    @Optional
    @OutputFile
    File outAsText

    @TaskAction
    action() {
        commandLine.addToEnd('terraform', 'plan')

        if (tfNativeArgOut) {
            commandLine.addToEnd("out=${tfNativeArgOut.name}")
        }
        OutputStream echoOutputHereToo = new EchoOutputStream(new ByteArrayOutputStream(), System.out)
        if (outAsText) {
            outAsText.createNewFile()
            echoOutputHereToo = new EchoOutputStream(echoOutputHereToo, new PrintStream(outAsText))
        }

        echoOutputHereToo.withStream { os ->
            DSCommandLineExecutorFactory.createExecutor(project).executeExecSpec(this, { ExecSpec e ->
                e.commandLine this.commandLine
                e.standardOutput = os
            })
        }
    }
}
