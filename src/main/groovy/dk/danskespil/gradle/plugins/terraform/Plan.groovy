package dk.danskespil.gradle.plugins.terraform

import dk.danskespil.gradle.plugins.helpers.dscommandlineexecutor.DSCommandLineExecutorFactory
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*
import org.gradle.process.ExecSpec

class Plan extends TerraformTask
{
    // These inputfiles are the same for Validate and Plan
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
    File out

    @TaskAction
    action() {
        commandLine.addToEnd('terraform', 'plan')

        if (tfNativeArgOut) {
            commandLine.addToEnd("-out=${tfNativeArgOut.name}")
        }
        OutputStream echoOutputHereToo = new EchoOutputStream(new ByteArrayOutputStream(), System.out)
        if (out) {
            out.createNewFile()
            echoOutputHereToo = new EchoOutputStream(echoOutputHereToo, new PrintStream(out))
        }

        echoOutputHereToo.withStream { os ->
            DSCommandLineExecutorFactory.createExecutor(project).executeExecSpec(this, { ExecSpec e ->
                e.commandLine this.commandLine
                e.standardOutput = os
            })
        }
    }

    @Override
    String getDescription() {
        return """wraps terraform plan. You can set -out='filename' with and get textual output into a file with out
    task(type: dk.danskespil.gradle.plugins.terraform.Plan) {
      tfNativeArgOut=file('output.bin')
      out=file('textversion.txt')
    }
    """
    }
}
