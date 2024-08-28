# Remove of Custom Actions

User can remove the existing custom actions. Remove allows users to delete the existing custom actions.

### Create the configuration file

In the customAppConfig.json set the parameter "actionEnable" to false.

**Example:**

```
{
 "appName": "topologybrowser",
 "urlDetails": [
 {
 "guiLabel": "Launch_Google",
 "category": "Configuration Management Actions",
 "protocol": "https",
 "domainOrIPAddress": "www.google.com",
 "path": "search",
 "queryparams": "nodeId=%id%",
 "actionEnable": "false",
 "actionRule": {
 "condition": {
 "dataType": "ManagedObject"
 }
 }
 }
 ]
}
```

###  Run the script using the following command.

 /bin/custom_app_integration.sh (absolute path for the application-sdk-tools-"version") directory
Eg: /bin/custom_app_integration.sh/root/application-sdk-tools-1.0.25

### Result

 Action will not be visible in the UI.

**Result**

```
Starting the script to add custom actions
Parsing the input file: /root/application-sdk-tools-version/config/customAppConfig.json
Starting the customization for the application topologybrowser
topologybrowser application json file is successfully updated
topologybrowser application locale file is successfully updated
Starting the customization for the application alarmviewer
alarmviewer application json file is successfully updated
alarmviewer application locale file is successfully updated
Script is successfully executed
You have successfully removed a custom action from ENM application application.
```
