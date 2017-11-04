package dk.danskespil.gradle.plugins.terraform.plugin

import dk.danskespil.gradle.plugins.terraform.tasks.Apply
import dk.danskespil.gradle.plugins.terraform.tasks.Get
import dk.danskespil.gradle.plugins.terraform.tasks.Init
import dk.danskespil.gradle.plugins.terraform.tasks.Plan
import dk.danskespil.gradle.plugins.terraform.tasks.Validate
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.Delete

class TerraformPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        applyJavaPluginSoWeHaveCommonTasksSuchAsCleanAtHand(project)

        Get tfGet = project.task(type:Get, 'tfGet')
        Init tfInit = project.task(type:Init, 'tfInit')
        Validate tfValidate = project.task(type:Validate, 'tfValidate')

        Plan tfPlan = project.task(type:Plan, 'tfPlan', dependsOn: tfValidate) {
            inputs.files tfGet.outputs.files
            inputs.files tfInit.outputs.files
            out = project.file('plan-output.bin')
            outAsText = project.file('plan-output')
        }

        Apply tfApply = project.task(type:Apply, 'tfApply') {
            inputs.files tfPlan.outputs.files
        }

        cleanAlsoCleansFilesCreatedByTerraform(project)
    }

    private void cleanAlsoCleansFilesCreatedByTerraform(Project project) {
        Task tfClean = project.task(type: Delete, 'tfClean') {
            delete project.tasks.findByName('tfPlan').getOutputs()
        }
        project.tasks.findByName('clean').dependsOn(tfClean)
    }

    private applyJavaPluginSoWeHaveCommonTasksSuchAsCleanAtHand(Project project) {
        project.apply(plugin: JavaPlugin)
    }
}
