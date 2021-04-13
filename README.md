# IFSC iCalendar Generator ![GitHub release (latest by date)](https://img.shields.io/github/v/release/jrmcdonald/ifsc-ical-generator) ![build](https://github.com/jrmcdonald/ifsc-ical-generator/workflows/release/badge.svg) ![Sonar Quality Gate](https://img.shields.io/sonar/quality_gate/jrmcdonald_ifsc-ical-generator?server=https%3A%2F%2Fsonarcloud.io)
The IFSC iCalendar Generator is a utility that allows anyone to generate an iCalendar file containing the IFSC competition schedule for importing into calendar applications.

The utility fetches JSON data from the [IFSC ranking endpoint](https://components.ifsc-climbing.org/results-api.php?api=season_leagues_calendar&league=388) and converts it into the iCalendar format.

## Developer Notes
### Prerequisites
* Docker
* Java 11
* Node 12 / Yarn (commit hooks - run `yarn` to install dependencies)
* Helm
* Skaffold (for developing against a Kubernetes cluster)

### IDE
To run in an IDE ensure that the `local` profile is set.

### Local Build
To build and run the tests:
> `./gradlew clean build`

To build the docker image:
> `./gradlew bootBuildImage`

To run the service locally:
> `ENV=local SPRING_PROFILES_ACTIVE=local ./gradlew bootRun`

To run the service in a kubernetes cluster (with hot reloading):
> `skaffold dev --force=false`

### Sample Requests
To make a request for all competitions:
```shell script
curl -X GET -H "Content-Type: application/json" localhost:8080/calendar
```

## License
This project uses the following license: [MIT](LICENSE.md).
