#!/usr/bin/env/ groovy

def call(String choice_type="ChoiceParameter", String name="", String description="", String script="", String referenced_params="") {
    template = [
        $class: 'ChoiceParameter',
        choiceType: 'PT_SINGLE_SELECT', 
        description: description, 
        name: name,
        script: [
            $class: 'GroovyScript', 
            fallbackScript: [
                classpath: [], 
                sandbox: false, 
                script: 
                    "return['Could not get The environemnts']"
            ], 
            script: [
                classpath: [], 
                sandbox: false, 
                script: script
            ]
        ]
    ]

    if(choice_type == "ChoiceParameter") {
        template.put('filterLength', 1)
        template.put('filterable', false)
    } else if(choice_type == "CascadeChoiceParameter") {
        template.put('referencedParameters', referenced_params)
    }
    
    return template
}