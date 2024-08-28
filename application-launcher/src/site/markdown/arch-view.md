# Architectural view

## Concepts

New entries in the ENM Application Launcher can be added by dropping a JSON file inside a pre defined directory the 
ENM file system. The new link will be displayed automatically, after some short period of time, if the format and 
content are correct.

**Extension point overview**

<img src="images/ps-extension-point.png" border="1"/>


## Testing

The SDK comes with a ready to use testing environment where it is possible to quickly verify if the configuration files meet 
the requirements for the new entry to appear in the ENM Application Launcher.

An small subset of ENM is available though docker images and the environment is provisioned by Vagrant.

### Docker
[Docker](https://www.docker.com/) is an open source tool designed to make it easier to create, deploy, and run
applications by using containers. Containers allow a developer to package up an application with all of the parts it
needs, such as libraries and other dependencies, and ship it all out as one package.

The Application Launcher SDK uses Docker to package an image with a slice of ENM with the necessary resources
(ENM production software and 3PP sw) to demonstrate and test adding a link to ENM Launcher.

### Vagrant
[Vagrant](https://www.vagrantup.com/) is an open-source software product for building and maintaining portable virtual
software development environments, such as Docker.

It provides a simple and easy to use command-line client for managing the virtual software development environments,
and an interpreter for the text-based definitions of what each environment looks like, called Vagrantfiles.

The Application Launcher SDK uses Vagrant to make it easier to deploy a standard and consistent environment across different
platforms like Windows, Linux and MacOS. Also, it provides an easy way to run Docker in a Windows 7 environment.

**A big picture of the testing environment.**

<img src="images/concept.png" border="1"/>

__* A subset of available images in ENM__
    