package dk.danskespil.gradle.plugins.terraform.tasks

import dk.danskespil.gradle.task.helpers.CommandLine

trait TerraformVariables {
    Map<String, String> vars = [:]
    List<File> varfiles = []

    void var(String key, String value) {
        vars[key] = value
        if (this.hasProperty('inputs')) {
            inputs.property(key, value)
        }
    }

    void varfile(File file) {
        varfiles << file
        if (this.hasProperty('inputs')) {
            inputs.file(file)
        }
    }

    void addVariablesToEnd(CommandLine commandLine) {
        varfiles.each {
            commandLine.addToEnd("-var-file=${it}")
        }
        vars.each { key, value ->
            commandLine.addToEnd('-var', "${key}=${value}")
        }
    }
}
