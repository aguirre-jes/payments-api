
# 1st stage, build the app
FROM container-registry.oracle.com/java/jdk-no-fee-term:25 AS build

# Install maven with integrity verification
ADD https://archive.apache.org/dist/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz /usr/share/
ADD https://archive.apache.org/dist/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz.sha512 /usr/share/

RUN set -x && \
    cd /usr/share && \
    echo "$(cat apache-maven-3.9.9-bin.tar.gz.sha512) apache-maven-3.9.9-bin.tar.gz" | sha512sum -c - && \
    tar -xvf apache-maven-3.9.9-bin.tar.gz && \
    rm apache-maven-3.9.9-bin.tar.gz apache-maven-3.9.9-bin.tar.gz.sha512 && \
    mv apache-maven-* maven && \
    ln -s /usr/share/maven/bin/mvn /bin/

WORKDIR /helidon

# Create a first layer to cache the "Maven World" in the local repository.
# Incremental docker builds will always resume after that, unless you update
# the pom
COPY pom.xml .
RUN mvn package -Dmaven.test.skip -Declipselink.weave.skip -Declipselink.weave.skip -DskipOpenApiGenerate

# Do the Maven build!
# Incremental docker builds will resume here when you change sources
COPY src src
RUN mvn package -DskipTests

RUN echo "done!"

# 2nd stage, build the runtime image
FROM container-registry.oracle.com/java/jdk-no-fee-term:25
WORKDIR /helidon

# Copy the binary built in the 1st stage
COPY --from=build /helidon/target/payments-api-mp.jar ./
COPY --from=build /helidon/target/libs ./libs

CMD ["java", "-jar", "payments-api-mp.jar"]

EXPOSE 8080
