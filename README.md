# README #

This README would normally document whatever steps are necessary to get your application up and running.

### What is this repository for? ###

* Quick summary
* Version
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)

### How do I get set up? ###

* Build the code : ```./gradlew clean build```
* How to run tests
** ./gradlew test # Simple version
** ./gradlew --continuous --info test # Keep testing
** ./gradlew --continuous --info --tests dk.danskespil.gradle.plugins.terraform.PlanTest test # Keep testing a single test
*  Deployment
** ./gradlew publish # SNAPSHOT
** ./gradlew clean release publish -Prelease.scope=major_minor_OR_patch -Prelease.stage=final -Partifactory_plugins_repo=plugins-release -Partifactory_user=gatekeeper -Partifactory_password=***REMOVED*** # RELEASE

### Contribution guidelines ###

* This is a TDD project. Respect that by writing your test before your code, reuse the existing test classes and helpers and you will be fine

### Who do I talk to? ###

* Jesper Wermuth wermuth@lundogbendsen.dk
* Mads Brouer, Danske
 spil