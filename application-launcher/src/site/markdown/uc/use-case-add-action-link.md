# Adding of Custom Actions to ENM Application

To customize ENM by adding UI actions to ENM Applications (Topology Browser, Network Explorer and FM Alarm monitor). The actions are added as a post-deployment activity by the customization engineer who configures the new action by specifying the details in the input file.
A custom UI action may launch Launch a web application (URL) or 
execute a script on scripting vm which is written and uploaded to scripting vm by customization engineer.
The customization engineer configures the new action by specifying the details in the input file

*   To add custom actions, user should have root access to scripting VM
*   User should have access to an ENM applications to verify custom actions.

### Step 1 : About application sdk tools:

a. The application sdk tools package is already installed in scripting VM at the following location

```
[root@scp-2-scripting(enmHost) cloud-user]$ ls -ltrh /opt/ericsson/custom_app/application-sdk-tools-1.0.26/
total 12K
drwxr-xr-x. 2 root root 4.0K Mar  5 13:08 bin
drwxr-xr-x. 2 root root 4.0K Mar  5 13:08 config
drwxr-xr-x. 2 root root 4.0K Mar  5 13:08 lib
```

b. The bash script will be at the location: /opt/ericsson/custom_app/application-sdk-tools-version/bin/custom_app_integration.sh

```
[root@scp-2-scripting(enmHost) cloud-user]$ ls -ltrh /opt/ericsson/custom_app/application-sdk-tools-1.0.26/bin/
total 20K
-rwxr--r--. 1 root root 15K Apr 13  2018 custom_app_integration.sh
-rwxr--r--. 1 root root 498 Apr 13  2018 constants
```

c. The input configuration file for the bash script will be present at: /opt/ericsson/custom_app/application-sdk-tools-version/config/

```
[root@scp-2-scripting(enmHost) cloud-user]$ ls -ltrh /opt/ericsson/custom_app/application-sdk-tools-1.0.26/config/
total 8.0K
-rwxr--r--. 1 root root 4.6K Apr 13  2018 customAppConfig.json
```

d. The input file is a json file which is an array of items. Each item represents an application (One of Topology Browser, Network Explorer and FM Alarm monitor Only). Each application contains number of custom actions which need to be added to that application. There are two types of custom actions which can be added to an ENM application.

urlDetails - This action represents opening URL when a particular custom action is triggered.

executableDetail - Execute a custom script which is present in the scripting VM.

Once the script is executed it will take around one minute of time for it to get displayed in the application GUI.

### Step 2:  update input file with custom actions

a. The input file is an array of items which represents ENM applications and custom actions which should be applied to them.

b. Customization engineer can give custom actions for multiple applications in a single input file.

c. Each application item contains custom actions, and the condition which should get satisfied to be shown on the GUI.

d. Navigate to customAppConfig.json file

```
[root@scp-1-scripting(enmapache) cloud-user]$ cd application-sdk-tools-1.0.27/config/
```

Update customAppconfig.json file with custom action details. Refer below table to know more about the attributes in customAppconfig.json


|Property|Description|Example|Acceptable Values|Mandatory|
|--- |--- |--- |--- |--- |
|appName|The application name to where the custom action is to be added.|"appName": "topologybrowser"|topologybrowser, alarmviewer, networkexplorer|Yes|
|guiLabel|Action name to be displayed in the GUI.|"guiLabel": "Launch_Google"|Any meaning full name is acceptable|Yes|
|actionEnable|actionEnable: true represents the action will be visible in GUI.actionEnable: false represents the action will not be visible in GUI|"actionEnable": "true"|true, false|Yes|
|customAction|It is used to differentiate between custom action and ENM standard action.customAction: True represents the action is a custom actioncustomAction: false represents the action is a standard action|"customAction": "True"|True|Yes|
|category|The category the actions that will go into.|"category": "Configuration Management"|Fault Management ActionsMonitoring & Troubleshooting ActionsConfiguration ManagementLegacy ActionsPerformance ManagementSecurity Management|No|
|protocol|The protocol used to launch a URL.|"protocol": "https"|https,http,ftp|Yes|
|domainOrIPAddress|The domain name of the website or an IP address|"domainOrIPAddress": ["www.google.com"](https://www.google.com/)|URL or an IP address|Yes|
|path|The path to be appended to the domainOrIPAddress. For applications deployed within the ENM infrastructure the path usually starts with a hash tag #|"path": "search"|NA|No|
|port|Port number where your application is listening. This is only required in cases where the application URL points to a different port.|"port": "8080"|NA|No|
|queryparams|queryparams are used to send extra parameters in the URLFor example ipaddress of the nodes.|"queryparams":"poid=%id%&neType=%neType%","|FDN, ipaddress, nodeid, nodename, moType, neType|No|
|queryDelimiter|queryDelimiter is used for separating multiple queryparams|"queryDelimiter": "&",|exclamation mark(!), semi colon(;), colon(:), comma(,)and ampersand(&).|No|
|commandToExecute|Script or jar or commands to be executed in the scripting VM.|"commandToExecute": "java -jar /home/shared/common/custom_app/Alarmdetails.jar""commandToExecute": "bash /home/shared/common/custom_app/Alarmdetails.sh"|Java -jarbash|Yes|
|context|Additional data of the alarm / node, which will be appended to commandToExecute|"context": "%eventPoIdAsString%"|FDN, ipaddress, nodeid, nodename, moType, neType|No|
|actionRule|Action rules are used to decide if a given action is applicable to the given selection. A condition composes the rules.Condition : The condition contains the dataType applicable to the given rule and some properties that needs to match with the selected objects.dataType : Indicates the dataType applicable to this rule.|"actionRule": { "condition": { "dataType": "ManagedObject" } }|NA|Yes|
|Properties|The properties that are mentioned should match the properties from the selected objects for this rule to be evaluated|"properties": [ { "name": "type", "value": "MeContext" },{ "name": "type", "value": "ERBS" }]|NA|No|


**Steps to update the input file for launching URL as a custom action:**

1. Update input file (customAppconfig.json) with urlDetails. Refer [Examples for Launching URLs for more information](launching_url.html).

**Steps to update the input file for Launch Executables as a custom action:**

1. Update customAppconfig.json file with ExecutableDetails. Refer [Examples for Triggering an Executable for more information](triggering_executable.html).  

2. Add custom scripts to be executed by custom action to /home/shared/common/custom_app/

a. FTP the custom scripts that are to be executed through the custom action to the following directory

 ```
 [root@scp-1-scripting(enmapache) config]$ cd /home/shared/common/custom_app/
 ```
 
b.You may create sub-directories under /home/shared/common/custom_app  based on the customization and assign executable permissions for all the users

3. **Run custom_app_integration.sh script**

 a. Navigate to below path

 ```
 [root@scp-1-scripting(enmapache) bin]$ /opt/ericsson/custom_app/application-sdk-tools-1.0.27/bin
 ```

 b. Run custom_app_integration.sh script by providing the absolute path of application-sdk-tools path as an argument.

 ```
 ./custom_app_integration.sh (absolute path for the application-sdk-tools-"version" directory)
Eg:
[root@scp-1-scripting(enmapache) bin]$ ./custom_app_integration.sh /opt/ericsson/custom_app/application-sdk-tools-1.0.27
```

 **Command Result:**

```
Starting the script to add custom actions
Parsing the input file:  /opt/ericsson/custom_app/application-sdk-tools-1.0.27/config/customAppConfig.json
Starting the customization for the application topologybrowser
No new actions to be updated.
Script executed successfully.
Starting the customization for the application alarmviewer
alarmviewer application json file is successfully updated
Script is successfully executed
```

4. **Verify custom actions in the respective applications**

a. Logon to ENM Application Launcher and navigate to the application that was mentioned in the customAppConfig.json file.

b. Select an item that matching the applicable conditions for added action in input file(e.g. NE Type)

c. Custom actions will be displayed in the top section or on right click

d. Click custom action

 **Result:**

The expected URL will be launched or script is executed in new tab.

## Multiple Selection:  ###

To add a custom action for multiple selection, we need to configure the action with "multipleSelection": "true" in the input file.
For topology browser and Network Explorer, the condition will be same as for the single selection.

**For Alarm viewer**, we need to specify the condition as: **"actionRule": { "condition": { "dataType": "Alarm" }** }

```
"executableDetail": [{
"guiLabel": "Execute_Custom_Script_Multiple",
"commandToExecute": "bash /home/shared/common/custom_app/execute_multiple.sh",
"context": "%eventPoId%",
"actionEnable": "true",
"customAction": "true",
"multipleSelection": "true",
"actionRule": {
"condition": {
"dataType": "Alarm"
}
}
}, {
"guiLabel": "Execute_Custom_Script_Single",
"commandToExecute": "bash /home/shared/common/custom_app/execute_single.sh",
"context": "%eventPoId%",
"actionEnable": "true",
"customAction": "true",
"multipleSelection": "false",
"actionRule": {
"condition": {
"dataType": "ManagedObject"
}
}
}]
```

**For topology browser:**

```
"executableDetail": [{
"guiLabel": "Execute Command",
"commandToExecute": "ls -ltrh /tmp",
"context": "%id%",
"actionEnable": "true",
"customAction": "true",
"multipleSelection": "true",
"actionRule": {
"condition": {
"dataType": "ManagedObject"
}
}
}]
```
### Roles (Not supported currently) ###
Right now custom actions will be displayed for all the users regardless of the role they belong to. So anybody who has access to Alarm Monitor, Network Explorer and Topology Browser will have access to the custom action. All the custom scripts will be executed with the logged in user. Currently it is mandatory to give the executable permissions to all the users as we are not sure which user executes the custom action. It is planned in future to support these roles - to restrict custom actions getting displayed in the GUI and also documentation for giving proper permissions for the scripts

|Application|Resource|Operation|Description|
|--- |--- |--- |--- |
|Adaptation CM NB integration|adaptation_cm_nb_integration|execute|Execute access for adaptation_cm_nb_integration|
|Adaptation element manager|adaptation_element_manager|execute|Allows all operations for an Element Manager which is available as a customer adaptation|
|Adaptation FM NB integration|adaptation_fm_nb_integration|execute|Execute access for adaptation_fm_nb_integration|
|Adaptation healthcheck|adaptation_healthcheck|execute|Execute adaptation Node HealthCheck|
|Adaptation Installer|adaptation_installer|execute|Allowes all actions (including install and remove) for custom adaptation actions and scripts|
|Adaptation inventory synch|adaptation_inventorysynch|execute|Allows executing of supported adaptation Node actions such as Upgrade, Backup, Restore and Delete Backup|
|Adaptation launch help|adaptation_launch_help|execute|Allows access to non standard help to support an adaptation|
|Adaptation Node CLI|adaptation_nodecli|execute|Launch Adaptation Node CLI|
|Adaptation PM NB integration|adaptation_pm_nb_integration|execute|Execute access for adaptation_pm_nb_integration|
|Adaptation Solution 1|adaptation_solution_1|execute|Execute access for adaptation_solution_1|
|Adaptation Solution 2|adaptation_solution_2|execute|Execute access for adaptation_solution_2|
|Adaptation Solution 3|adaptation_solution_3|execute|Execute access for adaptation_solution_3|
|Adaptation Solution 4|adaptation_solution_4|execute|Execute access for adaptation_solution_4|
|Adaptation Solution 5|adaptation_solution_5|execute|Execute access for adaptation_solution_5|
|Adaptation subscription|adaptation_subscription|execute|Allows access to adaptation PM subscription actions for Performance Monitoring on the Network|
|Adaptation trouble ticketing|adaptation_trouble_ticketing|execute|Execute access for adaptation to sync with an external trouble ticketing system|


> * The customization engineer is responsible for the behavior of the custom executables and must consider the resources required.
* There is no additional capacity for customization executables. The resource needs will be met by the standard allocation for user scripts. The customization engineer must analyse this and discuss with customer.
* Opening URL is not supported in combination with multiple selection.
* The custom script should have execute permission for all the users.

To verify the expected behavior of the script follow the below steps

> 1. Create a custom role with all the Adaptation roles, Scripting_Operator and respective application role(Eg: Topology browser, Alaram Viewer etc.,)
2. Create a user with the custom role which was created in step1. Refer ENM Security System Administration Guide section 2.1.3 for creating a user
3. Click custom action to execute the script
