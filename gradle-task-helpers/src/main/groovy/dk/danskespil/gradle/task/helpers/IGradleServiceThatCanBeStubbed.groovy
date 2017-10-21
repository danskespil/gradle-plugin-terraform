package dk.danskespil.gradle.task.helpers

import org.gradle.api.Project

/*
 * Testing in gradle can be difficult due to the fact that a test invocation of a gradle build, it performed in
 * its own JVM (https://docs.gradle.org/current/dsl/org.gradle.api.tasks.testing.Test.html).
 *
 * The problem arises when you want your a service that your tests use, e.g. a call to an external party, to
 * behave differently when testing. You have to tell the service that you want it to behave differently somehow. You can not
 * not do this by calling a static method, or create a Singleton that will be instantiated at the test level, because a
 * new static variable, or a new Singleton will be instantiated in the JVM that the test is run under.
 *
 * It does not make sense to have a 'disableStub' method. Use cases:
 *
 * - When testing, you enable the test, by calling enableStub.
 *
 * - When not testing, you do nothing.
 */

interface IGradleServiceThatCanBeStubbed<S> {
    S createService(Project project)

    void enableStub()

    boolean isStubbed()
}