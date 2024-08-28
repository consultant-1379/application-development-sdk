# Examples for Launching URL #

### Example 1: Launching a URL. ###

Present new action in Topology Browser which is applicable to all the NE types. On click of the action, it will open google.com in a new tab. You can add any URL in the domainOfIpAddress field. For example, your trouble ticketing system.

```
{
"guiLabel": "Google",
"protocol": "https",
"domainOrIPAddress": "www.google.com",
"actionEnable": "true",
"actionRule":
{
"condition":
{
"dataType": "ManagedObject"
}
}
}
```
### Example 2 - Launching an URL with Context ###

In this example we will be launching an nodecli application and will be sending NE type and node id as query parameters. This action will only be visible when the neType ERBS has been selected. The &(ampersand) delimiter is being used to separate the node id and the neType. The domainOrIPAddress can be the trouble ticketing system URL, you can pass the different values as request parameters. The names of these request parameters can also be configured as per your requirement to match your trouble ticketing system fields. This may help pre-filling the data in your trouble ticketing system. 

```
{
"guiLabel": "Launch Custom Node CLI",
"category": "Configuration Management",
"protocol": "https",
"domainOrIPAddress": "ieatenm5267-1.athtem.eei.ericsson.se",
"path": "#nodecli",
"queryparams": "poid=%id%&neType=%neType%",
"queryDelimiter": "&",
"actionEnable": "true",
"customAction": "true",
"actionRule": {
"condition": {
"dataType": "ManagedObject"
}
}
}
```