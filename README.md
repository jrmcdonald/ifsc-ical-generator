# IFSC iCalendar Generator ![GitHub release (latest by date)](https://img.shields.io/github/v/release/jrmcdonald/ifsc-ical-generator) ![build](https://github.com/jrmcdonald/ifsc-ical-generator/workflows/release/badge.svg) ![Sonar Quality Gate](https://img.shields.io/sonar/quality_gate/jrmcdonald_ifsc-ical-generator?server=https%3A%2F%2Fsonarcloud.io)

The IFSC iCalendar Generator is a utility that allows anyone to generate an iCalendar file containing the IFSC competition schedule for importing into calendar applications.

The utility fetches JSON data from the [IFSC ranking endpoint](http://egw.ifsc-climbing.org/egw/ranking/json.php) and converts it into the iCalendar format.

## Prerequisites

In order to get started you need either of the following installed locally:

* Docker
* Java 11

## Running

To run the utility, follow these steps:

Docker:
```shell script
docker run --rm --name ifsc-ical-generator -e SPRING_PROFILES_ACTIVE=local -p 8080:8080 jrmcdonald/ifsc-ical-generator:<version>
```

Java:
```shell script
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

## Making requests

To make a request for all competitions:
```shell script
curl -X GET -H "Content-Type: application/json" localhost:8080/calendar
```

To make a request for competitions in specific categories a JSON array of category numbers should be passed in the request body:
```shell script
curl -X POST -H "Content-Type: application/json" -d '["69","68","262","256","70"]' localhost:8080/calendar
```

## Local Development

To develop locally you need the following software installed

* Docker
* Java 11
* Node 12
* Yarn
* Helm
* Skaffold

To create a local Kubernetes cluster, correctly configured for ingress:
```shell script
./scripts/kind-setup.sh
```

To deploy the application to the local cluster and watch for changes:
```shell script
skaffold dev --profile=local
```

## License

This project uses the following license: [MIT](LICENSE.md).