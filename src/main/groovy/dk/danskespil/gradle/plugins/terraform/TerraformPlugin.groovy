package dk.danskespil.gradle.plugins.terraform

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

class TerraformPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        applyJavaPluginSoWeHaveDanskeSpilDefaultTasksAtHand(project)
    }

    private applyJavaPluginSoWeHaveDanskeSpilDefaultTasksAtHand(Project project) {
        project.apply(plugin: JavaPlugin)
    }
}
