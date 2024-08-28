# Deploying to production environment

## How to deploy to production

### Before deploying

Make sure that the configuration files are verified in the provided test environment.

The configuration files need to be installed on the NAS, accessible by HTTP and
UIserv instances.

In order to put your changes in production you must have read and write access to NAS.

### Installation

The installation is a single step procedure.
As _root_ user place your files in the following directory:

```
/ericsson/tor/data/apps/<YOUR_APP_ID>/<YOUR_APP_ID>.json
```
After installing to this location you must change ownership of the files as follows:

```
chown -R jboss_user:jboss /ericsson/tor/data/apps/<YOUR_APP_ID>
```

### Validation

Allow up to two minutes for the system to perceive your changes, the same way you did while testing your solution.

## Dimensioning the resources

Dimensioning is not necessary for this SDK.

