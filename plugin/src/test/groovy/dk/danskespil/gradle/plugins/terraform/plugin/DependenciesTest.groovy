package dk.danskespil.gradle.plugins.terraform.plugin

import dk.danskespil.gradle.plugins.helpers.BaseSpecification
import dk.danskespil.gradle.plugins.terraform.tasks.Apply
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Unroll

class DependenciesTest extends BaseSpecification {
    @Unroll
    def "#taskA dependsOn #taskB so #reason"() {
        given:
        Project project = ProjectBuilder.builder()
                .withProjectDir(testProjectDir.root)
                .build()

        when:
        project.plugins.apply(TerraformPlugin)

        then:
        project.tasks.findByName(taskB)
        project.tasks.findByName(taskA)
        project.tasks.findByName(taskA).dependsOn.contains(project.tasks.findByName(taskB))

        where:
        taskA        | taskB        | reason
        'check'      | 'tfValidate' | 'the plugin provides provide basic validation'
        'tfValidate' | 'tfGet'      | 'modules are fetched before validation'
        'tfGet'      | 'tfInit'     | 'resources are fetched externally before looking for modules'
        'clean'      | 'tfClean'    | 'the plugin provides a lifecycle similar to java lifecycle'
        'build'      | 'tfPlan'     | 'the plugin provides a lifecycle similar to java lifecycle'
    }

    def "it should not be possible to depend on tfApply by mistake. Its too dangerous"() {
        given:
        Project project = ProjectBuilder.builder()
                .withProjectDir(testProjectDir.root)
                .build()

        when:
        project.plugins.apply(TerraformPlugin)
        Apply tfApply = project.tasks.findByName('tfApply')
        Set<Task> tasks = new HashSet<Task>()

        project.tasks.each { task ->
            if (task.name != 'tfApply') {
                tasks.addAll(task.dependsOn)
            }
        }

        then:
        !tasks.contains(tfApply)
    }
}
