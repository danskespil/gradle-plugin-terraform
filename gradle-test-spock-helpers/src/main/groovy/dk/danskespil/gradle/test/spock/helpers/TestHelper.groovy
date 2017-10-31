package dk.danskespil.gradle.test.spock.helpers

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.rules.TemporaryFolder

// Shorthands for building a gradle project when testing
class TestHelper {
    TemporaryFolder testProjectDir

    BuildResult buildWithTasks(String... tasks) {
        return base(testProjectDir.root, tasks).build()
    }

    BuildResult buildAndFailWithTasks(String... tasks) {
        return base(testProjectDir.root, tasks).buildAndFail()
    }

    private static base(File projectDir, String... tasks) {
        return GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(projectDir)
                .withArguments(tasks)
    }
}
