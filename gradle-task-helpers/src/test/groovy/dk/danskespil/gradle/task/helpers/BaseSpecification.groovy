package dk.danskespil.gradle.task.helpers

import dk.danskespil.gradle.test.spock.helpers.TemporaryFolderSpecification

class BaseSpecification extends TemporaryFolderSpecification {
    def setup() {
        setupTestStubsByAddingMarkerFilesToAConventionNamedDirectory()
    }

    void setupTestStubsByAddingMarkerFilesToAConventionNamedDirectory() {
        GradleServiceExecuteOnOSFactory.instance.enableStub()
    }
}
