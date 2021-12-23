properties([
        [$class              : 'ParametersDefinitionProperty',
         parameterDefinitions: [[$class      : 'StringParameterDefinition',
                                 defaultValue: '',
                                 description : '',
                                 name        : 'DOMAIN',
                                ]]],
        disableConcurrentBuilds(),
])

node("win10") {
    stage("Step 1 - checkout code") {
        var_of_git = checkout(scm)
        writeFile(file: 'Dockerfile', text: readFile(file: 'Dockerfile') + '\nENV GIT_COMMIT=' + var_of_git.get('GIT_COMMIT'))
    }
    stage("Step 2 - maven package") {
        if (params.DOMAIN?.trim()) {
            stage('replace ngrok domain') {
                env.PROJECT_OAUTH_GITHUB_REDIRECTURI = "https://" + params.DOMAIN?.trim() + "/auth/oauth/callback/github/"
                env.PROJECT_BLOG_CLIENT_PRE_ESTABLISHED_REDIRECT_URI = "https://" + params.DOMAIN?.trim() + "/blog/login"
                powershell('''
                    (Get-Content src/main/resources/application.properties).replace("@pom.project.oauth.github.redirectUri@", $env:PROJECT_OAUTH_GITHUB_REDIRECTURI) | Set-Content src/main/resources/application.properties
                    (Get-Content src/main/resources/application.properties).replace("@pom.project.blog.client.pre-established-redirect-uri@", $env:PROJECT_BLOG_CLIENT_PRE_ESTABLISHED_REDIRECT_URI) | Set-Content src/main/resources/application.properties
                ''')
            }
        }

        powershell('''
            mvn package -DskipTests=true -P=test
        ''')
    }
    stage('Step 3 - docker build') {
        powershell('''
            docker build -t local/auth-server .
        ''')
    }
    stage("Step 4 - deploy") {
        try {
            powershell('''
                docker rm -f auth-server
            ''')
        } catch (e) {

        }
        powershell('''
            docker run -itd --restart always -p 50010:20010 --name auth-server local/auth-server
        ''')
    }
}