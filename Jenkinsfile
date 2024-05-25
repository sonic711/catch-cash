#!groovy

pipeline {
    agent any
    options {
        ansiColor('xterm')
        timeout(time: 1, unit: 'HOURS')
    }

    tools {
        jdk 'java'
        gradle 'Gradle8.7'
    }

    environment {
        JOB_TIME = sh(returnStdout: true, script: "date '+%A %W %Y %X'").trim()
    }

    stages {
        stage('Checkout Source') {
            steps {
                sh 'echo $JOB_TIME'
                echo "--------------------------env--------------------------------"
                sh "printenv"
                echo "--------------------------env--------------------------------"
                checkout([
                        $class                           : 'GitSCM',
                        branches                         : scm.branches,
                        doGenerateSubmoduleConfigurations: true,
                        extensions                       : scm.extensions + [[$class: 'SubmoduleOption', parentCredentials: true]],
                        userRemoteConfigs                : scm.userRemoteConfigs
                ])
                // get latest git message
                script {
                    env.GIT_COMMIT_MSG = sh(script: 'git log -1 --pretty=%B ${GIT_COMMIT}', returnStdout: true).trim()
                }
            }
        }

        stage('Build') {
            steps {
                withGradle {
                    sh 'gradle clean bootJar -Pprofile=local --refresh-dependencies -x check --stacktrace --console=plain'
                }
            }
        }

        stage('Deploy') {
            steps {
                    sh '''
                    cd /var/jenkins_home/workspace/Catch-Cash/web/build/libs
                    nohup java -jar web-server.jar &
                    '''
//                 sh '''ssh -tt $ssh_server << remotessh
//                     cd $ssh_dir
//                     sh ./stop.sh
//                     exit
//                     remotessh'''
//                 sh 'scp /var/jenkins_home/workspace/Catch-Cash/web/build/libs/web-server.jar $ssh_server:$ssh_dir/web-server.jar'
//                 sh '''ssh -tt $ssh_server << remotessh
//                     cd $ssh_dir
//                     sh ./run.sh
//                     exit
//                     remotessh'''
            }
        }
    }

    post {
        always {
            script {
                BUILD_USER = currentBuild.rawBuild.getCause(Cause.UserIdCause)?.getUserId()
                if (!BUILD_USER) BUILD_USER = "Job Scheduler"
                COLOR_MAP = ['SUCCESS': 'good', 'FAILURE': 'danger',]
            }
            slackSend channel: '#ci-jenkins',
                    color: COLOR_MAP[currentBuild.currentResult],
                    message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} by ${BUILD_USER}\n More info at: ${env.BUILD_URL}"
        }

        success {
            echo "Only when we haven't failed running the first stage"
        }

        failure {
            echo "Only when we fail running the first stage."
        }

        fixed {
            echo "Only when we fixed the previous failed stage."
        }
    }

}
