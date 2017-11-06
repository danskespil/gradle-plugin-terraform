Build Status master branch [![Build Status master branch](https://travis-ci.org/jwermuth/gradle-plugin-terraform.svg?branch=master)](https://travis-ci.org/jwermuth/gradle-plugin-terraform)

[Versions](https://plugins.gradle.org/plugin/dk.danskespil.gradle.plugins.terraform) on Gradle Plugin Portal

** This repository is not fully operational yet. If you use the watch feature I will notify once its operational **
# What is this repository for? 

This plugin helps us, at Danske Spil and Lund&Bendsen, to use Hashicorps ```terraform``` for production and test systems.
When we started using terraform, we just used the cli. However, soon we found that there is a workflow when we use terraform.

One usecase is, that we save the textual output from ```terraform plan``` in a file, and commit 
it to git before we do an apply (NB: git committing is not part of this plugin). 
This is done for auditing reasons. To do that, you have to 
do _something like_ (the real flow is more complex, and the example below is pseudo-code) 

```
// run plan and save the output in a file
terraform plan > plan-output
// inspect output visually
// commit the file
git commit -m "plan-output" plan-output
// Perform the change
terraform apply
``` 

Even if you remember to do the above steps _every time_, 

* Every now and then, we have to do a ```terraform get``` to get ```terraform plan``` to run.
* After a fresh clone, you have to do a ```terraform init``` to get stuff initialized.
* ...

So, to make sure we follow the workflow we have defined to be the best, and to make sure we
remember to do the auditing we want, we codified it in this gradle plugin.

This plugin is written for terraform 0.10.4. It will most likely work with 0.10.x versions, but 
no guarantees are given. 

## How do I use it

Well. There are a number of tests in the repo you can read. Here is a brief overview, 
but remember that the source code is _always_ the truth. These words are merely shadows on the wall.

This is how you include and apply the plugin
```text
plugins {
  id "dk.danskespil.gradle.plugins.terraform" version "0.0.4"
}
```
this gives you a number of tasks, that represent a subset of terraforms cli commands, e.g.
* plan (called tfPlan)
* validate (called tfValidate)
* apply (called tfApply)
* init (called tfInit)
* get (called tfGet)

you call them like this
```text
./gradlew tfPlan
./gradlew tfApply
...
```
The tasks have inter-dependencies, e.g. tfApply uses the plan-output.bin from tfPlan to make its apply,
so it will call tfPlan for you if its not present.
Likewise tfPlan will call terraform plan for you and produce plan-output.bin and plan-output. 
If you call it again, it will only actually execute terraform plan if your *.tf or *.tpl files has changed
since last time it was called. This can save considerable amounts of time if you are building even mid-sized
infrastructures.

When applying the plugin itself, you get a configured version of the tasks that works in a reasonable way.
e.g. tfPlan writes textual output to plan-output by default, and the binary plan to plan-output.bin by default.

You can create custom version of the tasks like so
```text
task myPlan(type: dk.danskespil.gradle.plugins.terraform.tasks.Plan) {
    out=file('my-better-bin-name.bin'
    outAsText=file('my-better-text-name.text'
}
```
if you want to, but if you just want a workflow that works as our default, use the tasks 
provided by the plugin. They have been configured for you. If you create custom tasks you
have to configure them yourself.

## Examples
By simply applying the plugin, you can call tfPlan and expect that plan is called after all required
initialization has been performed. This requires that you have a working terraform setup, e.g. that you
are able to perform the terraform cli steps manually.

However, since the plugin can not know where you have put your modules, you have to tell it. 
You do that as done in the example below, assuming your module files are at the gradle root dir in directory 
_modules_

```text
// apply plugin
tfGet {
  inputs.files fileTree("$rootDir/modules")
  // the output is configured by default in the plugin, so you do not have to do that 
  // outputs.files fileTree("$projectDir/.terraform/modules")
}
```

# How do I contribute
Join the party - write a test, code the functionality.
## How do I get set up? 

* Build the code : ```./gradlew clean build```
* How to run tests
  * ./gradlew test # Simple version
  * ./gradlew --continuous --info test # Keep testing
  * ./gradlew --continuous --info --tests dk.danskespil.gradle.plugins.terraform.tasks.PlanTest test # Keep testing a single test
* Release, following ajoberstar plugin https://github.com/ajoberstar/gradle-git/wiki/Release%20Plugins
  * ./gradlew clean release -Prelease.scope=major_minor_OR_patch -Prelease.stage=final_OR_rc_OR_milestone_OR_dev
  * ./gradlew clean release # snapshot version
  * ./gradlew clean release -Prelease.scope=patch -Prelease.stage=dev # e.g. fiddling with readme
* Deployment
  * ./gradlew clean publishPlugins
  

## Contribution guidelines 

* This is a TDD project. Respect that by writing your test before your code, reuse the existing test classes and helpers and you will be fine

### Who do I talk to? ###

* Jesper Wermuth wermuth@lundogbendsen.dk
 
# Versions
## 0.0.5
* Internal refactoring

## 0.0.4-SNAPSHOT
* documentation on tfGet 

## 0.0.3-SNAPSHOT
* Validate
* terraform arguments, such as -out, are called 'out' when they are implemented in tasks, e.g. Plan. This undo'es what
  was done in 0.0.2-SNAPSHOT

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
