# Calculate Financial Eligibility - Crime (CFE-Crime)

This is an API that performs the means assessment for a crime case. (For civil cases, the equivalent is [CFE-Civil](https://github.com/ministryofjustice/cfe-civil)).

CFE-Crime is part of the LAA Eligibility Platform, and owned by the [Eligibility Platform team](https://dsdmoj.atlassian.net/wiki/spaces/EPT/overview?homepageId=4305552053)

The main client of this service is [CCQ](https://dsdmoj.atlassian.net/wiki/spaces/LE/overview).

[![CircleCI](https://dl.circleci.com/status-badge/img/gh/ministryofjustice/cfe-crime/tree/main.svg?style=shield)](https://dl.circleci.com/status-badge/redirect/gh/ministryofjustice/laa-crime-means-assessment/tree/main)
[![MIT license](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![API docs](https://img.shields.io/badge/API_docs_-view-85EA2D.svg?logo=swagger)](https://cfe-crime-dev.apps.live.cloud-platform.service.justice.gov.uk/open-api/swagger-ui/index.html)

## Architecture

CFE-Crime is a Java-based Spring Boot application, hosted on [MOJ Cloud Platform](https://user-guide.cloud-platform.service.justice.gov.uk/documentation/concepts/about-the-cloud-platform.html).

The main part of the eligibility logic is implemented in [Crime Means Assessment (CMA)](https://dsdmoj.atlassian.net/wiki/spaces/ASLST/pages/3917447206/Crime+Means+Assessment+Service+CMA), which is shared with MAAT. However CMA is missing some bits, which are yet to be pulled out of the MAAT PL/SQL. So CFE-Crime provides a wrapper around CMA, and filling in the gaps, and may well include related eligibility APIs. See: [Proposal for a shared crime eligibility calculator](https://docs.google.com/document/d/1XdFWnwkiGnwz2M4wEYIHpkAlQ7h0ze3ZI5L2neupkr4/edit#)

See also: [ADR15 "Blueprint" for an eligibility service](https://dsdmoj.atlassian.net/wiki/spaces/EPT/pages/4413194281/ADR15+Blueprint+for+an+eligibility+service)

## Running locally

Install Java - use the major version specified in the .circleci/config.yaml and Dockerfile:

```sh
brew install openjdk@11
export PATH="/opt/homebrew/Cellar/openjdk@11/11.0.19/bin:$PATH"
```

Clone this repository:

```sh
git clone git@github.com:ministryofjustice/cfe-crime.git
cd cfe-crime
```

To compile the code:

```sh
./gradlew build
```

To run the tests:
```sh
./gradlew test
```

Starting the app - TODO

## CI/CD

### Code coverage
The following variables in build.gradle.kts control the coverage:

* percentageCoverage - percentage of code coverage required 
* jacocoExclude - files excluded from code coverage


