package dk.danskespil.gradle.plugins.terraform.plugin

import dk.danskespil.gradle.plugins.helpers.DSSpecification
import dk.danskespil.gradle.plugins.terraform.tasks.Get
import dk.danskespil.gradle.plugins.terraform.tasks.Plan
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
    def "terraform task '#task' is provided by plugin"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
        """
        when:
        def build = buildWithTasks(task)

        then:
        build.task(":${task}")

        where:
        task << ['tfPlan', 'tfValidate', 'tfApply', 'tfGet', 'tfInit', 'tfClean']
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
        caller    | callee       | reason
        'tfPlan'  | 'tfValidate' | "so validation is done explicitly before applying"
        'tfPlan'  | 'tfInit'     | "sa user can call plan on a fresh clone, initialization is done automatically"
        'tfApply' | 'tfPlan'     | "so user can call apply directly and plan is done automatically"
    }

    @Unroll
    def "terraform task '#task' is described when running gradle tasks"() {
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

    def "When calling task 'clean', task 'tfClean' will also be called"() {
        given:
        buildFile << """
          plugins { 
              id 'dk.danskespil.gradle.plugins.terraform'
          }
        """
        buildWithTasks('tfPlan')
        def planOutputExistsAfterPlanTaskIsRun = exists('/plan-output')

        when:
        def build2 = buildWithTasks('clean')

        then:
        planOutputExistsAfterPlanTaskIsRun
        build2.task(':tfClean').outcome == TaskOutcome.SUCCESS
    }
}
