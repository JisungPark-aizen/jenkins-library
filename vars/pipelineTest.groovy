#!/usr/bin/env groovy

def call() {
    pipeline {
        agent any

        stages {
            stage("lib") {
                steps {
                    echo "test"
                }
            }
        }
    }
}