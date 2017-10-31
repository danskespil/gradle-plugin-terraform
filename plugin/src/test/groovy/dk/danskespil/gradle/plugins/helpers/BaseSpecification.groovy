package dk.danskespil.gradle.plugins.helpers

import dk.danskespil.gradle.task.helpers.GradleServiceExecuteOnOSFactory
import dk.danskespil.gradle.test.spock.helpers.TemporaryFolderSpecification

class BaseSpecification extends TemporaryFolderSpecification {
    def setup() {
        setupTestStubsByAddingMarkerFilesToAConventionNamedDirectory()
    }

    void setupTestStubsByAddingMarkerFilesToAConventionNamedDirectory() {
        GradleServiceExecuteOnOSFactory.instance.enableStub()
    }
}
