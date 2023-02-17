FROM ubuntu:20.04

# Install tzdata
RUN apt-get update &&\
    DEBIAN_FRONTEND=noninteractive TZ=Etc/UTC apt-get -y install tzdata

# Install necessary packages
RUN apt-get update --fix-missing && apt-get install -y \
    openjdk-17-jdk \
    curl \
    git \
    unzip \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Install Gradle 8.0
ENV GRADLE_VERSION 8.0
RUN curl -L https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -o gradle.zip \
    && unzip gradle.zip \
    && rm gradle.zip \
    && mv gradle-${GRADLE_VERSION} /opt/gradle \
    && ln -s /opt/gradle/bin/gradle /usr/bin/gradle

# Set environment variables
ENV JAVA_HOME /usr/lib/jvm/java-17-openjdk-amd64/
ENV PATH $PATH:/opt/gradle/bin

# Clone the Spring project from GitHub
RUN git clone https://github.com/StormVMSC/BachelorApp.git /app

# Build the Spring project with Gradle
WORKDIR /app
RUN gradle build

# Expose port 8080
EXPOSE 8080

# Start the Spring project
CMD java -jar /app/build/libs/*-SNAPSHOT.jar