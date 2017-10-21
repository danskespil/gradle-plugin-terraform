package dk.danskespil.gradle.task.helpers

import org.gradle.api.Project

@Singleton
class GradleServiceExecuteOnOSFactory extends AbstractFileBasedGradleServiceThatCanBeStubbed<GradleServiceExecuteOnOS> {
    private Class executorClass

    GradleServiceExecuteOnOS createService(Project project) {
        if (!executorClass) {
            if (isStubbed()) {
                executorClass = ExecuteOnOSStub
            } else {
                executorClass = ExecuteOnOS
            }
        }
        executorClass.newInstance([project: project])
    }
}
