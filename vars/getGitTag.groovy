#!/usr/bin/env groovy

def call() {
    result = sh(script: "git tag --list --sort=-v:refname | cat | head -n 1", returnStdout: true).trim()
    return result
}