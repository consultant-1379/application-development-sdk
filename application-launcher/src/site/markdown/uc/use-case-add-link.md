# Adding a link for an internal or external URL

This section will guide through the steps necessary to  add a link to a local or external application in the ENM
Application Launcher.

### Step 1 - Create the configuration file

The configuration file is a JSON file. The below samples does have a minimal set of attributes.

For internal applications

```
    {
      "id": "myApp",
      "name": "My Application",
      "path": "#myApp",
      "type": "web",
      "groups": [ { "id": "Monitoring", "name": "Monitoring" } ]
    }
```

For external applications

```
    {
      "id": "ericssonNews",
      "name": "Ericsson News",
      "path": "/en/news-and-events/latest-news",
      "externalHost": "www.ericsson.com",
      "openInNewWindow": true,
      "type": "web",
      "groups": [ {  "id": "Documentation", "name": "Documentation" } ]
    }
```

It is possible to add links to an application that is running inside the ENM infrastructure and for external applications
(running anywhere) as well.

#### JSON Properties

Properties with an '*' are mandatory.

__Name__ | __Description__ | __Example__
---: | --- | --- |
**id*** | The unique identifier of the application | myApp
**type*** | The type of the application. The available types are: web and citrix (deprecated)| web
**host** | This refers to the variable name on global.properties containing the host address to your application. This is only required in cases where the application URL points to a different host than the ENM default. Most ENM applications don't require this. If property externalHost is declared, it will override the the host property. | esmon
**externalHost** | This allows you directly input the host address for an external host. This is used when you wish to link to an application outside enm. When declared, this property overrides the host property. | www.ericsson.com
**path*** | The path to be appended to the host or externalHost properties. For applications deployed within the ENM infraestructure the path usually starts with a hash tag '#', and the application will be loaded using JavaScript.| #networkexplorer or /news (external)
**groups*** | The Launcher category in which the application will be available | Documentation ([See available categories](categories.html))
**name** | The name that should be displayed on the Launcher for this application | Network Explorer
**version** | Numeric sequential value to represent the configuration version | 1
**shortInfo** | A brief description of your application that will be shown on the application tooltip | Use Network Explorer to search and retrieve all Network Configuration Data. The returned data can be grouped into Collections or Saved searches to facilitate sharing and reuse
**acronym** | The application acronym | SHM
**protocol** | The protocol used by your application. This is only required when your application uses a different host. The valid values are: secure for HTTPS protocol and unsecure for HTTP protocol | secure
**port** | Port number where your application is listening. This is only required in cases where the application URL points to a different port than the ENM default. Most ENM applications don't require this.| 8080
**openInNewWindow** | Boolean value indicating if this application should be opened in a new window on the browser. If not provided the default value is false. | true
**hidden** | Boolean value indicating if this application should be visible in the applications list. If not provided the default value is false. | false
**resources** | All the resources allowed to view the application should be declared here. If the user does not have any of the resources declared here, the link will not be shown on Launcher. If no resource is declared, the application will be visible to any user. | searchExecutor

> Use any JSON online validation tool to verify that your JSON is well formed. A quick search of the internet will bring you many options.

### Step 2 - Preparing for delivery

The JSON file created in the step above should be located inside a directory.
The directory name must have the value given for the **id** attribute in the JSON file. The JSON file name must be the **id** along with '.json' as file extension.
For the samples provided in the step 1, we must have:

```
 - myApp
    myApp.json
```

```
 - ericssonNews
    ericssonNews.json
```

### Step 3 - Testing

If not done yet, please, follow the steps described in the [setup test environment section](../setup-test-env.html).

Once you have the test environment up and running and your configuration files, then paste them inside the *AddLinkVM/apps* folder. This folder was created when you extracted the vagrant artifacts
to a directory into your file system.

**It's not necessary to restart the VM in order to add or update applications.**
Wait at least two minutes to make sure the system recognizes the new or updated application.

Access http://localhost and check if your changes are there. If your link is not appearing in the launcher, please
read the [troubleshooting section](../troubleshooting.html).

> Notice that if you put your application in a group not in the [available categories](categories.html), it will not
appear in the main landing page, however your application will be available in the search or in the alphabetical list of all applications. 
