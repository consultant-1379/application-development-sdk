# Test environment

### Prerequisites

* Basic knowledge of [Vagrant](https://www.vagrantup.com/)
* Basic knowledge of [Docker](https://www.docker.com/)
* At least 3 GB RAM free
* At least 20GB HDD free

## Setup

The test environment is based on Docker images.

A tool called Vagrant is used to create a VM with a fresh Docker installation and all prerequisites to run and access the
application from your host machine, no matter which operating system you are using.

The big picture can be seen in the below figure, where the components involved in the environment setup are shown.

<img src="images/concept.png" border="1"/>

__* A subset of available images in ENM__
 
Please, follow the steps below ir order to setup the environment.

1. Install [VirtualBox] (https://www.virtualbox.org/wiki/Downloads)
2. Install [Vagrant] (https://releases.hashicorp.com/vagrant/1.9.6/) - version 1.9.6
3. Download and extract the [Vagrant artifacts](artifacts/${project.artifactId}-${project.version}.zip) zip file
4. Open a shell/cmd inside the directory *AddLinkVM* (created in step 3 above)
5. Run the command: *vagrant up*

*Note that it may take up to 20 minutes or more for vagrant to prepare the VM and install ENM docker images when you run it for the
first time. Subsequent runs will be significantly quicker.*

Please, see [troubleshooting](troubleshooting.html) section if you are having trouble setting up the environment.

### Making sure that the setup succeeded
Open a web browser in your own machine and go to http://localhost.

You should see the Application Launcher home page and a sample application in the 'Documentation'
category. This sample application is provided by the Application Launcher SDK and it's available in the *AddLinkVM/apps*
directory.

<a href="images/app-sdk-laucher2.png" target="_new"><img src="images/app-sdk-laucher2.png" width="200" height="120"/></a>

If you are not seeing it, revisit the documentation or see the [troubleshooting](troubleshooting.html) section.

To safely shutdown the VM run *vagrant halt*.
