#!/usr/bin/env groovy

// CI Message

def call(String channel="jenkins-slack", String title="jenkins", String status="failure", Map body=[:], Map fields=[:]) {
    current_time = sh(script: "date", returnStdout: true).trim()
    repository_name = env.GIT_URL.split('/').last()

    start_color   = "#0DADEA" // blue
    failure_color = "#FF0000" // red
    success_color = "#18be52" // green
    
    if (status == "start") {
        
    }
    
    def attachments = [
        [
            "fallback": title,
            "color": "#FFFFFF",
            "blocks": [
                [
                    "type": "header",
                    "text": [
                        "type": "plain_text",
                        "text": title,
                        "emoji": ture
                    ]
                ],
                [
                    "type": "section",
                    "fields": [
                        [
                            "type": "mrkdwn",
                            "text": "*Branch:*\n${env.GIT_BRANCH}"
                        ],
                        [
                            "type": "mrkdwn",
                            "text": "*Date:*\n${current_time}"
                        ],
                        [
                            "type": "mrkdwn",
                            "text": "*Job URL:*\n<${env.RUN_DISPLAY_URL}|Jenkins Job>"
                        ]
                    ]
                ]
            ]
        ]
    ]
    if(env.GIT_URL.contains("codecommit")) {
        GIT_RUL = "https://ap-northeast-2.console.aws.amazon.com/codesuite/codecommit/repositories/${repository_name}/commit/${GIT_COMMIT}"
    } else {
        GIT_URL = env.GIT_URL
    }
    
}

def slackMdFields(String message="") {
    return [
        "type": "mrkdwn",
        "text": message
    ]
}