package dk.danskespil.gradle.plugins.terraform.plugin

import dk.danskespil.gradle.plugins.helpers.DSSpecification
import dk.danskespil.gradle.plugins.terraform.Get
import dk.danskespil.gradle.plugins.terraform.Plan
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Unroll

class TerraformPluginTest extends DSSpecification {
    def "I can apply the plugin"() {
        given:
        buildFile << """
            plugins {
                id 'dk.danskespil.gradle.plugins.terraform'
            }
        """

        when:
        def result = buildWithTasks('tasks')

        then:
        result
    }

    @Unroll
    def "I have #task in plugin"() {
        given:
        buildFile << """
        plugins {
            id 'dk.danskespil.gradle.plugins.terraform'
        }
        """

        when:
        def result = buildWithTasks(task)

        then:
        result
        result.task(":${task}").outcome == TaskOutcome.SUCCESS

        where:
        task << ['tfPlan', 'tfGet', 'tfInit']
    }

    def "output from Get is input to Plan"() {
        given:
        Project project = ProjectBuilder.builder()
                .withProjectDir(testProjectDir.root)
                .build()

        when:
        project.plugins.apply(dk.danskespil.gradle.plugins.terraform.plugin.TerraformPlugin)
        Plan plan = project.tasks.getByName('tfPlan')
        Get get = project.tasks.getByName('tfGet')

        then:
        plan
        plan.taskDependencies.getDependencies(plan).contains(get)
    }

    @Unroll
    def "task #caller depends on #callee so #reason"() {
        given:
        Project project = ProjectBuilder.builder()
                .withProjectDir(testProjectDir.root)
                .build()

        when:
        project.plugins.apply(dk.danskespil.gradle.plugins.terraform.plugin.TerraformPlugin)
        Task callerTask = project.tasks.getByName(caller)
        Task calleeTask = project.tasks.getByName(callee)

        then:
        callerTask
        calleeTask
        callerTask.taskDependencies.getDependencies(callerTask).contains(calleeTask)

        where:
        caller    | callee        | reason
        'tfPlan'  | 'tfValidate'  | "so validation is done explicitly before applying"
        'tfPlan'  | 'tfInit'      | "sa user can call plan on a fresh clone, initialization is done automatically"
        'tfApply' | 'tfPlan'      | "so user can call apply directly and plan is done automatically"
    }

    @Unroll
    def "Main task '#task' is described when running gradle tasks"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
          
        """

        when:
        def build = buildWithTasks('tasks')

        then:
        build.output.contains(task)

        where:
        task << ['tfPlan', 'tfGet', 'tfInit', 'tfApply', 'tfValidate']
    }
}
