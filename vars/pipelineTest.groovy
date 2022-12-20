#!/usr/bin/env groovy

def call() {
    stage("lib") {
        echo "lib"
    }
    stage("build") {
        echo "build"
        env
    }
}