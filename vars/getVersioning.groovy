#!/usr/bin/env groovy

def call(Map git_info = [:]) {
    regex_url_pattern = "(http|https)?:\\/\\/(\\S+)"

    // Pipeline 시작 시 최신 Tag가 확인되지 않아 명령어로 Tag Pull
    withCredentials([gitUsernamePassword(credentialsId: git_info.credentials, gitToolName: 'git-tool')]) {
        sh 'git fetch origin --tags'
    }

    changelog_file = 'CHANGELOG.md'
    if (fileExists(changelog_file)) {
        sh "rm ${changelog_file}"
    }

    // Version 관리 Git clone
    dir("version") {
        git branch: git_info.version_branch,
        credentialsId: git_info.credentials,
        url: git_info.url

        if (fileExists('CHANGELOG.md')) {
            // 이전 변경 이력에 이어서 이력을 남기기 위해 저장된 CHANGELOG파일 이동
            sh "mv CHANGELOG.md .."
        }
    }

    // standard-version을 사용 시 자동 Commit추가를 제외하기 위한 옵션 사용
    sh "standard-version --skip.commit=true --packageFiles 'manifest.json'"
    sh "mv CHANGELOG.md version/"

    // standard-version으로 생성된 현재 버전 가져오기
    release_version = getGitTag()

    // Version관리 repo에 push
    dir("version") {
        sh "git config user.email jenkins"
        sh "git config user.name jenkins@aizen.co"

        sh "git add CHANGELOG.md"
        sh "git commit -m ${release_version}"
        
        version_git_url = (git_info.url =~ regex_url_pattern)[0][2]
        withCredentials([gitUsernamePassword(credentialsId: git_info.credentials, gitToolName: 'git-tool')]) {
            sh "git push https://${GIT_USERNAME}:${GIT_PASSWORD}@${version_git_url} HEAD:${git_info.version_branch}"
        }
    }

    git_url = (env.GIT_URL =~ regex_url_pattern)[0][2]
    withCredentials([gitUsernamePassword(credentialsId: git_info.credentials, gitToolName: 'git-tool')]) {
        sh "git push https://${GIT_USERNAME}:${GIT_PASSWORD}@${git_url} --tags"
    }

    return release_version
}