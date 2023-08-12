# Blockchain Lab Results Certification

This project demonstrates how to notarize one lab result certification on the blockchain with GenoBank.io. This is written in Java and is portable to Apache Tomcat, command line and other web environments.

```mermaid
sequenceDiagram
    Java->>+api.genobank.io: {"claim":..., "signature":..., "permitteeSerial":...}
    api.genobank.io->>+Java: {"txHash":..., "timestamp":..., "genobankSignature":...}
```



## Prerequisites

1. Supported operating systems are macOS, Linux and Windows
2. Install Java (version 11 or later supported version) and Apache Maven
   - On macOS
     1. [Do not use](https://stackoverflow.com/a/28635465/300224) the official installer from Oracle, that approach is painful, nobody uses it
     2. [Install Homebrew](https://brew.sh)
     3. `brew install java`
     4. `brew install maven`
     5. `brew install openjdk`

## Downloading

Get the latest source code from GitHub

```sh
cd ~
mkdir -p Developer
cd Developer
git clone https://github.com/FranciscoTun2/peternity-certificatej.git
cd genobankj
```

## Building and testing

Prepare your Maven project build state

```sh
./mvnw initialize
```

Build the product

```sh
./mvnw clean compile assembly:single
```

A build which passes all tests will be indicated by:

> [INFO] BUILD SUCCESS

at the bottom of your build.

After you have made any changes, run the build command above.

Run the product without arguments to see instructions

```sh
java -jar target/*.jar
```

Or include all required parameters to notarize a certificate

```sh
java -jar target/*.jar --test 'wrong outside clever wagon father insane boy junk punch duck drift cupboard' 41 'Bob' '1234' '1' 'N' '' 1614069145429 '13,13,10,13,28,30.2,28,31,11,12,11,12,11,11,11,11,12.2,13.2,12.2,14.2,17,12,17,14,12,12,12,8,11,19,15,19,15,19,15,17,9,7,9,6,10,10,10,10,10,12,10,12,16,23,16,20,11,11,11,12,24,22,24,22,x,y,x,x'


java -jar target/*.jar '41' 'wrong outside clever wagon father insane boy junk punch duck drift cupboard' '{"marcadores":[{"lp":"1","marcador":"D3S1358"},{"lp":"2","marcador":"vWA       "},{"lp":"3","marcador":"D16S539"},{"lp":"4","marcador":"CSF1PO"},{"lp":"5","marcador":"TPOX"},{"lp":"6","marcador":"Y indel"},{"lp":"7","marcador":"Amelogenin"},{"lp":"8","marcador":"D8S1179"},{"lp":"9","marcador":"D21S11"},{"lp":"10","marcador":"D18S51"},{"lp":"11","marcador":"Penta E"},{"lp":"12","marcador":"D2S441"},{"lp":"13","marcador":"D19S433"},{"lp":"14","marcador":"TH01"},{"lp":"15","marcador":"FGA"},{"lp":"16","marcador":"D22S1045"},{"lp":"17","marcador":"D5S818"},{"lp":"18","marcador":"D13S317"},{"lp":"19","marcador":"D7S820"},{"lp":"20","marcador":"D6S1043"},{"lp":"21","marcador":"D10S1248"},{"lp":"22","marcador":"D1S1656"},{"lp":"23","marcador":"D12S391"},{"lp":"24","marcador":"D2S1338"},{"lp":"25","marcador":"Penta D"}],"indice_paternidad_combinado":"123,974,357","muestras":[{"tipo":"PADRE","nombre":"LUIS","genotipo":[{"x":"10","y":"34"},{"x":"11","y":"33"},{"x":"12","y":"32"},{"x":"13","y":"31"},{"x":"14","y":"30"},{"x":"15","y":"29"},{"x":"16","y":"28"},{"x":"16","y":"27"},{"x":"17","y":"26"},{"x":"18","y":"25"},{"x":"19","y":"24"},{"x":"20","y":"23"},{"x":"21","y":"22"},{"x":"22","y":"21"},{"x":"23","y":"20"},{"x":"24","y":"19"},{"x":"25","y":"28"},{"x":"26","y":"17"},{"x":"27","y":"16"},{"x":"28","y":"15"},{"x":"29","y":"14"},{"x":"30","y":"13"},{"x":"31","y":"12"},{"x":"32","y":"11"},{"x":"33","y":"10"}]},{"tipo":"HIJO","nombre":"RAUL","genotipo":[{"x":"10","y":"34"},{"x":"11","y":"33"},{"x":"12","y":"32"},{"x":"13","y":"31"},{"x":"14","y":"30"},{"x":"15","y":"29"},{"x":"16","y":"28"},{"x":"16","y":"27"},{"x":"17","y":"26"},{"x":"18","y":"25"},{"x":"19","y":"24"},{"x":"20","y":"23"},{"x":"21","y":"22"},{"x":"22","y":"21"},{"x":"23","y":"20"},{"x":"24","y":"19"},{"x":"25","y":"28"},{"x":"26","y":"17"},{"x":"27","y":"16"},{"x":"28","y":"15"},{"x":"29","y":"14"},{"x":"30","y":"13"},{"x":"31","y":"12"},{"x":"32","y":"11"},{"x":"33","y":"10"}]}]}'
```

(Test account at https://github.com/Genobank/genobank.io/wiki/Test-Accounts-and-Certificates)

## Overview

* `LaboratoryProcedure`, `LaboratoryProcedureResult`, `Network` are simple records.
* `PermitteeRepresentations` stores everything that the permittee (laboratory) will attest to.
* `PermitteeSigner` performs cryptographic signing on behalf of the permittee.
* `Platform` notarizes the certificate onto the blockchain using GenoBank.io.
* `NotarizedCertificate` is the notarized artifact.
* `Main` is the program entry point.

## Extending

You are welcome to extend the functionality of this example, for example to connect to an ERP system or add additional information (like birthday) into the name field.

## References

* Style, comments
  * Baseline code style guide from https://google.github.io/styleguide/javaguide.html
  * Oracle style guide for doc comments https://www.oracle.com/technetwork/java/javase/documentation/index-137868.html#styleguide
    * What words to capitalize (refer examples), when to use periods and full sentences
    * ["Include tags in the following order"](https://www.oracle.com/technetwork/java/javase/documentation/index-137868.html#orderoftags)
    * Align text to columns
* Which version of dependencies we should support
  * Java versions supported by vendor https://www.oracle.com/technetwork/java/java-se-support-roadmap.html

- Setting up a Java + Maven + JUnit project layout
  - Best practice for Java+Maven+JUnit project layout https://github.com/junit-team/junit5-samples/tree/master/junit5-jupiter-starter-maven
  - How to remove Maven initialization errors https://stackoverflow.com/questions/4123044/maven-3-warnings-about-build-plugins-plugin-version
  - Gitignore for Maven projects ("should I include jars?") https://github.com/fulldecent/aion-aip040/issues/25
  - How to package everything into one Jar file https://stackoverflow.com/a/574650/300224
- Gitignore from https://github.com/github/gitignore/blob/master/Maven.gitignore
- Editorconfig is included and some rules are referenced to Google Java Style Guide

## Maintenance

- Periodically update to the most recent version, to obtain the latest bug fixes and new features:

  ```sh
  ./mvnw versions:use-latest-versions -Dincludes="org.checkerframework:*"
  ```

  

* Periodically update mvnw if necessary. We recognize upstream as https://github.com/takari/maven-wrapper

## License

This project is assigned copyright to GenoBank.io. All rights reserved.
