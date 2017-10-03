package dk.danskespil.gradle.plugins.terraform

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

class TerraformPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        applyJavaPluginSoWeHaveDanskeSpilDefaultTasksAtHand(project)

        Get tfGet = project.task(type:Get, 'tfGet')
        Init tfInit = project.task(type:Init, 'tfInit')
        Validate tfValidate = project.task(type:Validate, 'tfValidate')

        Plan tfPlan = project.task(type:Plan, 'tfPlan', dependsOn: tfValidate) {
            inputs.files tfGet.outputs.files
            inputs.files tfInit.outputs.files
            tfNativeArgOut = project.file('plan-output.bin')
        }

        Apply tfApply = project.task(type:Apply, 'tfApply') {
            inputs.files tfPlan.outputs.files
        }

    }

    private applyJavaPluginSoWeHaveDanskeSpilDefaultTasksAtHand(Project project) {
        project.apply(plugin: JavaPlugin)
    }
}
