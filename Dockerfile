ARG BUILD_IMAGE=maven:3.5-jdk-11
ARG RUNTIME_IMAGE=openjdk:11-jdk-slim
#############################################################################################
###                Stage where Docker is pulling all maven dependencies                   ###
#############################################################################################
FROM ${BUILD_IMAGE} as dependencies
ARG PROXY_SET=false
ARG PROXY_HOST=
ARG PROXY_PORT=
COPY pom.xml ./
RUN mvn -B dependency:go-offline \
        -DproxySet=${PROXY_SET} \
        -DproxyHost=${PROXY_HOST} \
        -DproxyPort=${PROXY_PORT}
#############################################################################################
#############################################################################################
###              Stage where Docker is building spring boot app using maven               ###
#############################################################################################
FROM dependencies as build
ARG PROXY_SET=false
ARG PROXY_HOST=
ARG PROXY_PORT=
COPY src ./src
RUN mvn -B clean package \
        -DproxySet=${PROXY_SET} \
        -DproxyHost=${PROXY_HOST} \
        -DproxyPort=${PROXY_PORT}
#############################################################################################
#############################################################################################
###                Optional stage where Docker is running Sonar analysis                  ###
#############################################################################################
FROM build
ARG SONAR_ENABLED=true
ARG SONAR_URL=https://sonarqube.poscloud.co.zw
# ARG SONAR_ORGANIZATION=
ARG SONAR_USERNAME=admin
ARG SONAR_PASSWORD=admin
# ARG SONAR_BRANCH=
RUN if [ "$SONAR_ENABLED" = "true" ] ; \
    then mvn -B sonar:sonar \
        -Dsonar.host.url=${SONAR_URL} \
 #       -Dsonar.organization=${SONAR_ORGANIZATION} \
 #       -Dsonar.branch.name=${SONAR_BRANCH} \
        -Dsonar.login=${SONAR_USERNAME} \
        -Dsonar.password=${SONAR_PASSWORD}; \
    fi
#############################################################################################
#############################################################################################
### Stage where Docker is running a java process to run a service built in previous stage ###
#############################################################################################
FROM ${RUNTIME_IMAGE}
COPY --from=build target/*.jar /app/service.jar
CMD ["java", "-jar", "/app/service.jar"]
#############################################################################################
