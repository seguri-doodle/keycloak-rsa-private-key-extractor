# keycloak-rsa-private-key-extractor

Use this utility to extract the private key used by Keycloak to sign JWTs.

You can use the output of this program on [jwt.io][1] to forge valid JWTs.

## Prerequisites

### Access to the remote database

You will need to connect via VPN to the target environment and port-forward the remote server with:

    kubectl port-forward postgresql-postgresql-0 5432

### Configuration

Read the remote db username/password with:

    vault read secret/doodle/ENVIRONMENT/keycloak/database

and update `Postgres.java` accordingly.

## Execution

Run `App.java`. In the output you will find the key that you can copy and paste into [jwt.io][1].

[1]: https://jwt.io