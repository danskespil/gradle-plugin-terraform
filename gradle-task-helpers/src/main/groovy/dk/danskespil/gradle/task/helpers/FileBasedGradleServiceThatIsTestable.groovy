package dk.danskespil.gradle.task.helpers

import org.gradle.api.Project

/**
 * Allows creation of test services/mocks/stubs by examining the filesystem for files under
 *
 * PROJECT_ROOT/build/stubTheseFactories/
 *
 * For a file named
 *
 * PROJECT_ROOT/build/stubTheseFactories/FULLYQUALIFIEDNAME
 *
 * If present, the factory is mocked. Mocked means that
 *
 */
@Singleton
class FileBasedGradleServiceThatIsTestable extends AbstractGradleServiceThatIsTestable {
    private Class executorClass

    AbstractGradleServiceCommandlineExecutor createService(Project project) {
        if (!executorClass) {
            if (isUnderTest()) {
                executorClass = CommandLineTestExecutor
            } else {
                executorClass = CommandLineExecutor
            }
        }
        executorClass.newInstance([project: project])
    }
}
