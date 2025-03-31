# Payments API MP

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=aguirre-jes_payments-api&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=aguirre-jes_payments-api) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=aguirre-jes_payments-api&metric=coverage)](https://sonarcloud.io/summary/new_code?id=aguirre-jes_payments-api) [![Tests Java](https://github.com/aguirre-jes/payments-api/actions/workflows/tests.yaml/badge.svg)](https://github.com/aguirre-jes/payments-api/actions/workflows/tests.yaml) [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=aguirre-jes_payments-api&metric=bugs)](https://sonarcloud.io/summary/new_code?id=aguirre-jes_payments-api) [![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=aguirre-jes_payments-api&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=aguirre-jes_payments-api)

## Overview

A Helidon MP application for managing payments, utilizing a hexagonal architecture and clean architecture principles. This project includes RESTful APIs, domain-driven design.

[![Helidon Payments API MP](https://mermaid.ink/img/pako:eNqllE2P2jAQhv-K5b20UqAkAQJRtVKCqYSEtAgWVWrTg0kcsOrYke20yyL-e50PAqHd9lAfosx45vGbmYlPMBYJgT7cS5wfwDOKODBLFbvaEcH5iyaSYwY-C8mSCNYBd0FBnjMaY00Fv424i0Iiw5SDJT4SeR9WLrT8GsGwUJQTpcCca6opUR938sPjTEgClmJP48pcF4yoCH7rMghPuo6utVqsDH8laYblEayE1DX73YLnhX5vcKDX6xkV3bTNU5m2IbHgyV3iU6Hfyuxoub6tAnSjIUhwbqrb0GaCaykYMw4LrOebZxCsFqqhP5byrxwjqnJuKt5VXJe4JrlQVAtpymiBtpMX7J8EbheGt1VEggU34SmOmw602TNGCdeqlRWgG1kBqpwoNBSENd5hdZ-_IfIHjW_a15aqfsYMK4VICpJ6XFLKmP_ghMOZN7eUqdB34j-k1WrM3k-a6IPv5C9WLJiQl-07Xl42rsGNxt48DP-Ks_-Bw02pG-JkEgyQ-19EcilRTQw_IXs-aolutd4k1tu_FdIMZVPIW6eZJascoaoknY0AWWUTLx93u7ddWChsRULL3Bk0gb6WBbFgRqQ5xJjwVOZEUB9IRiLom9eEpLhguvzlzyYtx_yLENklU4pif4B-ipkyVpEnWBNEsbk0stYrzYwQORMF19CfuBUD-if4An1n4vS96XA0sB3X88YTx4JH6NveoO96w-nYHrr2aGiPnLMFX6tTB_2RCbfHrjue2t7E85zzL_egiMs?type=png)](https://mermaid.live/edit#pako:eNqllE2P2jAQhv-K5b20UqAkAQJRtVKCqYSEtAgWVWrTg0kcsOrYke20yyL-e50PAqHd9lAfosx45vGbmYlPMBYJgT7cS5wfwDOKODBLFbvaEcH5iyaSYwY-C8mSCNYBd0FBnjMaY00Fv424i0Iiw5SDJT4SeR9WLrT8GsGwUJQTpcCca6opUR938sPjTEgClmJP48pcF4yoCH7rMghPuo6utVqsDH8laYblEayE1DX73YLnhX5vcKDX6xkV3bTNU5m2IbHgyV3iU6Hfyuxoub6tAnSjIUhwbqrb0GaCaykYMw4LrOebZxCsFqqhP5byrxwjqnJuKt5VXJe4JrlQVAtpymiBtpMX7J8EbheGt1VEggU34SmOmw602TNGCdeqlRWgG1kBqpwoNBSENd5hdZ-_IfIHjW_a15aqfsYMK4VICpJ6XFLKmP_ghMOZN7eUqdB34j-k1WrM3k-a6IPv5C9WLJiQl-07Xl42rsGNxt48DP-Ks_-Bw02pG-JkEgyQ-19EcilRTQw_IXs-aolutd4k1tu_FdIMZVPIW6eZJascoaoknY0AWWUTLx93u7ddWChsRULL3Bk0gb6WBbFgRqQ5xJjwVOZEUB9IRiLom9eEpLhguvzlzyYtx_yLENklU4pif4B-ipkyVpEnWBNEsbk0stYrzYwQORMF19CfuBUD-if4An1n4vS96XA0sB3X88YTx4JH6NveoO96w-nYHrr2aGiPnLMFX6tTB_2RCbfHrjue2t7E85zzL_egiMs)

## Build and run

With JDK23

```bash
mvn package
java -jar target/payments-api-mp.jar
```

## Try health

```bash
curl -s -X GET http://localhost:8080/health
{"outcome":"UP",...

```

## Building a Native Image

The generation of native binaries requires an installation of GraalVM 22.1.0+.

You can build a native binary using Maven as follows:

```bash
mvn -Pnative-image install -DskipTests
```

The generation of the executable binary may take a few minutes to complete depending on
your hardware and operating system. When completed, the executable file will be available
under the `target` directory and be named after the artifact ID you have chosen during the
project generation phase.

## Try metrics

```bash
# Prometheus Format
curl -s -X GET http://localhost:8080/metrics
# TYPE base:gc_g1_young_generation_count gauge
. . .

# JSON Format
curl -H 'Accept: application/json' -X GET http://localhost:8080/metrics
{"base":...
. . .
```

### Database Setup

Start your database before running this example.

Example docker commands to start databases in temporary containers:

Oracle:

```bash
docker run --rm --name xe -p 1521:1521 -p 8888:8080 wnameless/oracle-xe-11g-r2
```

For details on an Oracle Docker image, see [Oracle Images Container](https://github.com/oracle/docker-images/tree/master/OracleDatabase/SingleInstance)

## Building the Docker Image

```bash
docker build -t payments-api-mp .
```

## Running the Docker Image

```bash
docker run --rm -p 8080:8080 payments-api-mp:latest
```

Exercise the application as described above.

## Run the application in Kubernetes

If you don’t have access to a Kubernetes cluster, you can [install one](https://helidon.io/docs/latest/#/about/kubernetes) on your desktop.

### Verify connectivity to cluster

```bash
kubectl cluster-info                        # Verify which cluster
kubectl get pods                            # Verify connectivity to cluster
```

### Deploy the application to Kubernetes

```bash
kubectl create -f app.yaml                              # Deploy application
kubectl get pods                                        # Wait for quickstart pod to be RUNNING
kubectl get service  payments-api-mp                     # Get service info
kubectl port-forward service/payments-api-mp 8081:8080   # Forward service port to 8081
```

You can now exercise the application as you did before but use the port number 8081.

After you’re done, cleanup.

```bash
kubectl delete -f app.yaml
```

## Building a Custom Runtime Image

Build the custom runtime image using the jlink image profile:

```bash
mvn package -Pjlink-image
```

This uses the helidon-maven-plugin to perform the custom image generation.
After the build completes it will report some statistics about the build including the reduction in image size.

The target/payments-api-mp-jri directory is a self contained custom image of your application. It contains your application,
its runtime dependencies and the JDK modules it depends on. You can start your application using the provide start script:

```bash
./target/payments-api-mp-jri/bin/start
```

Class Data Sharing (CDS) Archive
Also included in the custom image is a Class Data Sharing (CDS) archive that improves your application’s startup
performance and in-memory footprint. You can learn more about Class Data Sharing in the JDK documentation.

The CDS archive increases your image size to get these performance optimizations. It can be of significant size (tens of MB).
The size of the CDS archive is reported at the end of the build output.

If you’d rather have a smaller image size (with a slightly increased startup time) you can skip the creation of the CDS
archive by executing your build like this:

```bash
mvn package -Pjlink-image -Djlink.image.addClassDataSharingArchive=false
```

For more information on available configuration options see the helidon-maven-plugin documentation.
