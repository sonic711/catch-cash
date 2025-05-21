#!groovy

pipeline {
    agent any
    options {
        timestamps()
        ansiColor('xterm')
        timeout(time: 1, unit: 'HOURS')
    }

    tools {
        jdk params.JENKINS_JDK_TOOL ?: Jenkins.instance.getDescriptorByType(hudson.model.JDK.DescriptorImpl).getInstallations().first().name
    }

    environment {
        JOB_TIME = sh(returnStdout: true, script: "date +'%A %Y-%m-%d %H:%M:%S'").trim()
    }

    stages {
        stage('Apply Parameters') {
            steps {
                script {
                    if (params.size()>0) {
                        env.is_param_from_UI = 'true'
                        echo "Using params from jenkins UI"
                    } else {
                        env.is_param_from_UI = 'false'
                        echo "Using params from pipeline"
                        properties([
                            parameters([
                                choice(name: 'env', choices: ['dev', 'sit', 'uat'], description: '部署環境。可參考 gradle/deploy/deploy.gradle'),
                                string(name: 'gradle_project', defaultValue: '', description: '要建置的 gradle 子 project，不填代表 run all project，填 rootProject 則代表 only run rootProject。'),
                                string(name: 'exclude_remotes', defaultValue: '', description: '要排除的 remotes (以逗號分隔)。可參考 gradle/deploy/deploy.gradle'),
                                string(name: 'include_remotes', defaultValue: '', description: '只包含的 remotes (以逗號分隔)。可參考 gradle/deploy/deploy.gradle'),
                                booleanParam(name: 'is_assemble', defaultValue: true, description: '是否 Assemble Artifact ?'),
                                booleanParam(name: 'is_test_report', defaultValue: true, description: '是否 Test Report ?'),
                                booleanParam(name: 'is_code_analysis', defaultValue: true, description: '是否 Code Analysis ?'),
                                booleanParam(name: 'is_owasp_analysis', defaultValue: true, description: '是否 OWASP Analysis ?'),
                                booleanParam(name: 'is_sonar_analysis', defaultValue: true, description: '是否 Sonar Analysis ?'),
                                booleanParam(name: 'is_put_files', defaultValue: true, description: '是否 Put Files ?'),
                                booleanParam(name: 'is_remote_start', defaultValue: true, description: '是否 Remote Start ?'),
                                choice(name: 'remote_start_mode', choices: ['Apply_New_Version', 'Restart', 'Rollback'], description: '[ Remote Start ] > 啟動模式'),
                                string(name: 'remote_start_rollback_version', defaultValue: '', description: '[ Remote Start ] > Rollback 版本'),
                                booleanParam(name: 'is_remote_stop', defaultValue: false, description: '是否 Remote Stop ?'),
                                booleanParam(name: 'is_offline', defaultValue: false, description: '是否 離線包版 ?'),
                                choice(name: 'JENKINS_JDK_TOOL', choices: Jenkins.instance.getDescriptorByType(hudson.model.JDK.DescriptorImpl).getInstallations().collect { it.name }),
                                string(name: 'JENKINS_SONAR_ENV', defaultValue: 'sonar'),
                            ])
                        ])
                    }
                }
            }
        }

        stage('Checkout Source') {
            when {
                expression { return env.is_param_from_UI.toBoolean() }
            }
            steps {
                script {
                    try {
                        scm
                        is_jenkins_file_from_scm = true
                        echo 'jenkinsfile from SCM: Do Checkout'
                    } catch (Exception e) {
                        is_jenkins_file_from_scm = false
                        echo 'jenkinsfile from local load: Skip Checkout'
                    }
                    if (is_jenkins_file_from_scm) {
                        checkout([
                                $class                           : 'GitSCM',
                                branches                         : scm.branches,
                                doGenerateSubmoduleConfigurations: true,
                                extensions                       : scm.extensions + [[$class: 'SubmoduleOption', parentCredentials: true]],
                                userRemoteConfigs                : scm.userRemoteConfigs
                        ])
                        env.GIT_COMMIT_MSG = sh (script: 'git log -1 --pretty=%B ${GIT_COMMIT}', returnStdout: true).trim()
                    }
                }
                echo "--------------------------env--------------------------------"
                sh "printenv | sort"
                echo "-------------------------------------------------------------"
                echo "JOB_TIME          =  ${env.JOB_TIME ?: ''}"
                echo "JAVA_HOME         =  ${env.JAVA_HOME ?: ''}"
                echo "GRADLE_USER_HOME  =  ${env.GRADLE_USER_HOME ?: ''}"
                echo "GIT_COMMIT        =  ${env.GIT_COMMIT ?: ''}"
                echo "GIT_COMMIT_MSG    =  ${env.GIT_COMMIT_MSG ?: ''}"
                echo "--------------------------env--------------------------------"
                // give gradlew Permission
                sh 'chmod +x gradlew'
            }
        }

        stage('Assemble Artifact') {
            when {
                expression { return env.is_param_from_UI.toBoolean() && params.is_assemble }
            }
            steps {
                script {
                    def str_project = "${gradle_project}" == "rootProject" ? ":" : "${gradle_project}".length() > 0 ? "${gradle_project}:" : ""
                    def path_project = "${gradle_project}" == "rootProject" ? "" : "${gradle_project}".length() > 0 ? "${gradle_project.replace(':', '/')}/" : "**/"
                    if (params.is_offline) {
                        sh "./gradlew ${str_project}clean ${str_project}build -Denv=${params.env} --refresh-dependencies -x check --stacktrace --console=plain --offline"
                    } else {
                        sh "./gradlew ${str_project}clean ${str_project}build -Denv=${params.env} --refresh-dependencies -x check --stacktrace --console=plain"
                    }
                    archiveArtifacts artifacts: "${path_project}build/libs/*.jar", allowEmptyArchive: true
                    archiveArtifacts artifacts: "${path_project}build/libs/*.war", allowEmptyArchive: true
                    archiveArtifacts artifacts: 'build/reports/problems/problems-report.html', allowEmptyArchive: true
                    publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'build/reports/problems', reportFiles: 'problems-report.html', reportName: 'Problems Report'])

                    // 設定檔處理邏輯
                    def config_env = params.env == 'uat' ? 'prod' : params.env
                    def config_path = "${path_project}src/main/resources/config/${config_env}/*"

                    archiveArtifacts artifacts: config_path, allowEmptyArchive: true
                }
            }
        }

        stage('Put Files') {
            when {
                expression { return env.is_param_from_UI.toBoolean() && params.is_put_files }
            }
            steps {
                script {
                    def str_project = "${gradle_project}" == "rootProject" ? ":" : "${gradle_project}".length() > 0 ? "${gradle_project}:" : ""
                    if (params.is_offline) {
                        sh "./gradlew ${str_project}put_files -Denv=${params.env} -Dexclude_remotes=${params.exclude_remotes} -Dinclude_remotes=${params.include_remotes} --stacktrace --console=plain --offline"
                    } else {
                        sh "./gradlew ${str_project}put_files -Denv=${params.env} -Dexclude_remotes=${params.exclude_remotes} -Dinclude_remotes=${params.include_remotes} --stacktrace --console=plain"
                    }
                }
            }
        }

        stage('Remote Start') {
            when {
                expression { return env.is_param_from_UI.toBoolean() && params.is_remote_start }
            }
            steps {
                script {
                    def str_project = "${gradle_project}" == "rootProject" ? ":" : "${gradle_project}".length() > 0 ? "${gradle_project}:" : ""
                    if (params.is_offline) {
                        sh "./gradlew ${str_project}remote_start -Denv=${params.env} -Dexclude_remotes=${params.exclude_remotes} -Dinclude_remotes=${params.include_remotes} -Dremote_start_mode=${params.remote_start_mode} -Dremote_start_rollback_version=${params.remote_start_rollback_version} --stacktrace --console=plain --offline"
                    } else {
                        sh "./gradlew ${str_project}remote_start -Denv=${params.env} -Dexclude_remotes=${params.exclude_remotes} -Dinclude_remotes=${params.include_remotes} -Dremote_start_mode=${params.remote_start_mode} -Dremote_start_rollback_version=${params.remote_start_rollback_version} --stacktrace --console=plain"
                    }
                }
            }
        }

	    stage('Remote Stop') {
            when {
                expression { return env.is_param_from_UI.toBoolean() && params.is_remote_stop }
            }
            steps {
                script {
                    if (!params.env.endsWith('_JMH')) {
                        def str_project = "${gradle_project}" == "rootProject" ? ":" : "${gradle_project}".length() > 0 ? "${gradle_project}:" : ""
                        def task_name = "remote_stop"
                        if (params.is_offline) {
                            sh "./gradlew ${str_project}${task_name} -Denv=${params.env} -Dexclude_remotes=${params.exclude_remotes} -Dinclude_remotes=${params.include_remotes} --stacktrace --console=plain --offline"
                        } else {
                            sh "./gradlew ${str_project}${task_name} -Denv=${params.env} -Dexclude_remotes=${params.exclude_remotes} -Dinclude_remotes=${params.include_remotes} --stacktrace --console=plain"
                        }
                    } else {
                        echo "Remote Stop skipped when JMH"
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                BUILD_USER = currentBuild.rawBuild.getCause(Cause.UserIdCause)?.getUserId()
                if (!BUILD_USER) BUILD_USER = "Job Scheduler"
                COLOR_MAP = ['SUCCESS': 'good', 'FAILURE': 'danger',]
                echo "【${currentBuild.currentResult}】 Job ${env.JOB_NAME} build #${env.BUILD_NUMBER} by ${BUILD_USER}"
            }
            //slackSend channel: '#ci-jenkins',
            //        color: COLOR_MAP[currentBuild.currentResult],
            //        message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} by ${BUILD_USER}\n More info at: ${env.BUILD_URL}"
        }

        success {
            //echo 'This runs if the pipeline succeeds'
            echo "More info at: ${env.BUILD_URL}"
        }

        failure {
            //echo 'This runs if the pipeline fails'
            echo "More info at: ${env.BUILD_URL}"
        }
    }
}
