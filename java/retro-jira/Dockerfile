FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# instalação maven
RUN apk add --no-cache bash curl gzip tar

ARG MAVEN_VERSION=3.9.9
ARG BASE_URL=https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries

ADD ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz /tmp/

RUN tar -xzf /tmp/apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /opt \
    && ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven \
    && rm -f /tmp/apache-maven-${MAVEN_VERSION}-bin.tar.gz

ENV MAVEN_HOME /opt/maven
ENV PATH ${MAVEN_HOME}/bin:${PATH}

RUN mvn --version

# instalação da aplicação
COPY pom.xml ./
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/*.war app.war

EXPOSE 8080

CMD ["java", "-jar", "app.war"]
