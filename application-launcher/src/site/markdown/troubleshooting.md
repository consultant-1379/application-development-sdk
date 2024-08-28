# Troubleshooting

## Production environment

For general troubleshooting on ENM please refer to the [ENM Troubleshooting Guide](https://erilink.ericsson.se/eridoc/erl/objectId/09004cff8c39f0fa?docno=1/15901-AOM901151Uen&format=pdf).

## Test environment

[Accessing log files from the test environment](#logfiles)

[ENM Application Launcher is not loading](#ap_not_loading)

[Vagrant fails to create or start the VM](#vagrant_fails_vm)

[Accessing localhost does not show the ENM Application Launcher](#landing_not_showing)

[ENM Application Launcher loads but my application is not there](#link_not_showing)


## Accessing log files from the test environment <a name="logfiles"></a>

**VirtualBox log files**

The first level of logging we have are the logs from the VirtualBox. To view the VirtualBox log files, open the Oracle VirtualBox console, 
right click in the VM which the name starts with AddLinkVM, and the select the select the __Show log...__ menu item (or CTRL L).

<a href="images/vbox.png" target="_new"><img src="images/vbox.png" width="200" height="120"/></a> 

**Vagrant log files**

When you type _vagrant up_ it is possible to see some useful log files. If the logs in the console are not enough, please
refer to the [Vagrant debugging documentation page](https://www.vagrantup.com/docs/other/debugging.html).

**Docker**

To view the logs from Docker, which will contain the logs from each container we are using, do the following:

* Open a shell/cmd inside the directory **AddLinkVM** (downloaded zip file)
* Execute: _vagrant shh_
* Once logged, go to the directory **/vagrant/docker**
* Run _docker-compose logs -f_

The procedure above will show the current logs from all the servers in the environment. Is possible to show the JBoss AS server only, use:
 
```
docker-compose logs -f jboss
```

In order to check which containers are being used at the moment, execute _docker ps_. This command will show all the 
running containers. To view all container, stopped and running, add the _-a_ option to the command.


## ENM Application Launcher is not loading <a name="ap_not_loading"></a>
Make sure that you allow enough time for the environment get ready. If it is the first time you are
running the test environment, it may take up to 20 minutes or more to complete the setup. If it is not the first time,
check the JBoss logs to make sure it is started.

Also, make sure that you have the minimal hardware requirements to run the environment as described in the [setup test environment page](setup-test-env.html). 

## Vagrant fails to create or start the VM <a name="vagrant_fails_vm"></a>
If you are running windows and have PowerShell installed, update it to the latest version.

## Accessing localhost does not show the ENM Application Launcher <a name="landing_not_showing"></a>
Make sure that you don't have any software running on ports 80, 8080, 9990, 9999 and 8787 in your machine.
If you do have and want to keep it, change the port forwarding in the Vagrantfile inside the *AddLinkVM*
directory.

*Note that this will most likely occur if your host is linux based. By default port forwarding, defined in the Vagrantfile, will attempt to use port 80 which will only be allowed if you are running as root user. Simplest fix is modify your Vagrantfile and use port 8008 on the host instead of port 80.*

## ENM Application Launcher loads but my application is not there <a name="link_not_showing"></a>
Check if your JSON file is valid and has all the mandatory attributes declared.
