# Rollback of Custom Actions

User can rollback the existing custom actions. Rollback allows user to rollback all the custom actions that are added.

## Step1

To remove the custom actions from the ENM application, run the script by adding the rollback parameter in the path shown below.

```
/bin/custom_app_integration.sh --rollback (absolute path for the application-sdk-tools-"version" directory)
```
*Eg: /bin/custom_app_integration.sh --rollback /root/application-sdk-tools-1.0.25*

**Result**

```
Are you sure to delete all the custom actions and restore the application json files (Yes/No) yes
topology browser is successfully restored.
```