package dk.danskespil.gradle.task.helpers

interface GradleServiceThatIsTestable {
    void setIsUnderTest(boolean isUnderTest)
    boolean isUnderTest()
}