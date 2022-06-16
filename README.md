# kogibot

![Kogibot - Java 11](https://github.com/spolti/kogibot/actions/workflows/main-merge-java-11.yml/badge.svg) 
![Kogibot - Java 16](https://github.com/spolti/kogibot/actions/workflows/main-merge-java-16.yml/badge.svg)
![Kogibot - Java 17](https://github.com/spolti/kogibot/actions/workflows/main-merge-java-17.yml/badge.svg)


This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Labels

Kogibot will by default create default labels if not present, default labels can be seen [here](src/main/java/org/kiegroup/kogibot/util/Labels.java)
The bot will also add any label flagged as required on every new pull request, by default, the only label required is "needs review".


## Running Kogitbot locally

Create e .env file on the project's root directory with the following environment variables:

```
QUARKUS_GITHUB_APP_APP_ID=<the numeric app id>
QUARKUS_GITHUB_APP_APP_NAME=<the name of your app>
QUARKUS_GITHUB_APP_WEBHOOK_PROXY_URL=<your Smee.io channel URL>
QUARKUS_GITHUB_APP_WEBHOOK_SECRET=<your webhook secret>
QUARKUS_GITHUB_APP_PRIVATE_KEY=-----BEGIN RSA PRIVATE KEY-----\
<your private key>                          \
        -----END RSA PRIVATE KEY-----
```

This bot has a Web Console to see the payloads received by the bot.
Can be accessed using the URL http://localhost:8080/replay/

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
$ mvn compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Code Style

This project uses a similar Java code style than https://github.com/kiegroup/droolsjbpm-build-bootstrap/tree/main/ide-configuration

TODO
 - add license header and configure maven plugin to check it
 - implement missing features
 - 