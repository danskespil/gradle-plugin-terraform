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
 
# Versions

## 0.0.3-SNAPSHOT
* Validate

## 0.0.2
* Has apply

## 0.0.2-SNAPSHOT
* make it easy to distinguish between flags and options provided by terraform and 
those provided by the plugin. Example --out is provided by terraform, and it saves the output of
plan to a binary file for later use. However we want to keep the textual output in a file
as well, so a plugin-specific flag --outAsText is provided. To make it easy to distinguish,
terraform _args_ (such as --out=aFileName.bin) are prefixed with tfNativeArg and becomes --tfNativeArgOut.
Yes, its verbose. Yes its easier to figure out what it means without having to read documentation.

## 0.0.1
* With focus on "terraform plan" and the corresponding custom task Plan a test environment is set up
