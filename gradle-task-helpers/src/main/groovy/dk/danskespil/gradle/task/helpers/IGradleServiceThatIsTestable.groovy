package dk.danskespil.gradle.task.helpers

interface IGradleServiceThatIsTestable {
    void setIsUnderTest(boolean isUnderTest)
    boolean isUnderTest()
}