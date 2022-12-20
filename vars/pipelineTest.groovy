#!/usr/bin/env groovy

def call() {
    stage("lib") {
        steps {
            echo "test"
        }
    }
}