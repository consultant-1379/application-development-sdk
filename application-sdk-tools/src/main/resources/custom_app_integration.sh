#!/bin/bash
#
# This Script updates the application json files in the
# /ericsson/tor/data/apps/ folder to display the custom
# actions as specified by the InputFile.
#
# Make sure single instance of the script is running at a time.
/bin/mkdir /var/tmp/customApplicationLock/
if [ $? -ne 0 ]
then
    /bin/echo "Already an instance of the script is running... Please try after some time"
    exit 1
fi

CustomAppSdk="$1/"
InputFile="$CustomAppSdk/config/customAppConfig.json"
JARFile="$CustomAppSdk/lib/"
ME="custom_app_integration.sh"

#######################################
#Create temporary application Directories
# Globals:
#   InputFile
# Arguments:
#   None
# Returns:
#   None
#######################################
createApplicationDirectories() {
    appNames=$($_GREP appName $InputFile | $_AWK -F ":" '{print $2}' | $_SED 's/\"//g' | $_SED 's/,//g' | $_SED "s/ //g" | xargs)
    for appname in $appNames
    do
        $_MKDIR -p $CustomAppSdk/$appname
        backUpFiles $appname
    done
}

#####################################################
#To backup the application directories and input.json
# Globals:
#   InputFile
#   backupDirectory
#   appsFolder
# Arguments:
#   appNames: Names of the application directories.
# Returns:
#   None
####################################################
backUpFiles() {
    $_MKDIR -p $backupDirectory

    # checking application directories
    appNames=$1
    for appName in $appNames
    do
         if [ -d $backupDirectory/$appName ]
         then
             $_RM -rf $backupDirectory/$appName
             $_CP -r $appsFolder/$appName $backupDirectory
         else
             $_CP -r $appsFolder/$appName $backupDirectory
             $_CP -r $appsFolder/$appName $backupDirectory/$appName-original
         fi
    done

# Back up input file
    if [ -f $backupDirectory/customAppConfig.json ]
    then
        $_DIFF $backupDirectory/customAppConfig.json $InputFile >> /dev/null
        if [ $? -ne 0 ]
        then
            $_CP $InputFile $backupDirectory/customAppConfig.json
        fi
    else
        $_CP $InputFile $backupDirectory/customAppConfig.json
    fi
}

#####################################################
#Create required json files for the applications.
# Globals:
#   InputFile
#   JARFile
#   CustomAppSdk
# Arguments:
#   None
# Returns:
#   None
####################################################
createJsonFiles() {
    $_ECHO "Parsing the input file: " $InputFile
    $_JAVA -jar $JARFile/custom-json-parser* $CustomAppSdk $InputFile >> "LOG_MESSAGE"  2> ERROR_MESSAGE

    # Handling log messages.
    checkLogging=$($_WC -l LOG_MESSAGE | $_AWK '{print $1}')
    if [ $checkLogging -gt 0 ]
    then
        while read -r log
        do
            $_LOGGER ${ME}: $($_WHOAMI) customJsonParser: $log
        done < LOG_MESSAGE
        $_CAT LOG_MESSAGE
    fi

    # Handling Error messages.
    checkError=$(wc -l ERROR_MESSAGE | awk '{print $1}')
    if [ $checkError -gt 0 ]
    then
        $_ECHO "Issue in executing the JAR file" $JARFile/custom-json-parser*
        $_HEAD -1 ERROR_MESSAGE
        $_LOGGER ${ME}: $($_WHOAMI) Error: $($_HEAD -1 ERROR_MESSAGE)
        $_RM -f ERROR_MEESAGE
        exit 1
    fi

    $_RM -f ERROR_MESSAGE LOG_MESSAGE
}

#####################################################
#Triggers the update of the required json files.
# Globals:
#   InputFile
# Arguments:
#   None
# Returns:
#   None
####################################################
updateApps() {
    appNames=$($_GREP appName $InputFile | $_AWK -F ":" '{print $2}' | $_SED 's/\"//g' | $_SED 's/,//g' | $_SED "s/ //g" | xargs)
    for appname in $appNames
    do
        $_ECHO "Starting the customization for the application" $appname
        updateLocale $appname
        updateApplicationFiles $appname
    done
}

####################################################################
#Creates the apps_actions.json locale file with the updated actions.
# Globals:
#   InputFile
#   appsFolder
#   CustomAppSdk
# Arguments:
#   appNames: Names of the application directories.
# Returns:
#   None
####################################################################
updateLocale() {

    appname=$1
    localeFile=$appsFolder/$1/locales/en-us/app_actions.json
    $_CP $appsFolder/$1/locales/en-us/app_actions.json  $CustomAppSdk/$appname/app_actions.json

    startString=$($_GREP appName $InputFile | $_GREP $1)
    actionLabels=$(getActionLabels $appname)
    for al in $actionLabels
    do
        isActionEnabled=$(getActionStatus $al)
        label=$($_ECHO $al | $_CUT -d "-" -f 2- | $_SED "s/-/ /g" | $_SED 's/\b./\u&/g' )
        $_GREP $al $CustomAppSdk/$appname/app_actions.json >> /dev/null
        if [ $? -ne 0 ]
        then
            if [ $isActionEnabled == "true" ] || [ $isActionEnabled == "True" ] || [ $isActionEnabled == "TRUE" ]
            then
                $_HEAD -n -1 $CustomAppSdk/$appname/app_actions.json > $CustomAppSdk/$appname/app_actions.json.tmp
                $_SED -i  '${s/$/,/}' $CustomAppSdk/$appname/app_actions.json.tmp
                $_CP $CustomAppSdk/$appname/app_actions.json.tmp $CustomAppSdk/$appname/app_actions.json
                $_ECHO "\"$al\" : {" >> $CustomAppSdk/$appname/app_actions.json
                $_ECHO "\"label\": \"$label\"" >> $CustomAppSdk/$appname/app_actions.json
                $_ECHO "}" >> $CustomAppSdk/$appname/app_actions.json
                $_ECHO "}" >> $CustomAppSdk/$appname/app_actions.json
                $_RM -f $CustomAppSdk/$appname/app_actions.json.tmp
            fi
        else
            if [ $isActionEnabled == "false" ] || [ $isActionEnabled == "False" ] || [ $isActionEnabled == "FALSE" ]
            then
                $_SED -i "/$al/,+2d" $CustomAppSdk/$appname/app_actions.json
                $_HEAD -n -1 $CustomAppSdk/$appname/app_actions.json > $CustomAppSdk/$appname/app_actions.json.tmp
                lastLine=$(tail -1 $CustomAppSdk/$appname/app_actions.json.tmp)
                $_SED -i '$d' $CustomAppSdk/$appname/app_actions.json.tmp
                $_ECHO $lastLine | $_SED "s/,//g" >> $CustomAppSdk/$appname/app_actions.json.tmp
                $_CP $CustomAppSdk/$appname/app_actions.json.tmp $CustomAppSdk/$appname/app_actions.json
                $_ECHO "}" >> $CustomAppSdk/$appname/app_actions.json
                $_RM -f $CustomAppSdk/$appname/app_actions.json.tmp
            fi
        fi
    done
}

#####################################################
#Update appjson, locale & rules files if required
# Globals:
#   InputFile
#   appsFolder
# Arguments:
#   appNames: Names of the application directories.
# Returns:
#   None
####################################################
updateApplicationFiles() {
    appname=$1
    cd $CustomAppSdk/$appname

    # appjson
    actualfile=$($_LS -ltr $appsFolder/$appname | $_GREP -i $appname | $_AWK '{print $9}')
    $_DIFF $appsFolder/$appname/$actualfile $CustomAppSdk/$appname/$appname.json > /dev/null
    if [ $? -ne 0 ]
    then
        $_CP $CustomAppSdk/$appname/$appname.json $appsFolder/$appname/$actualfile
        $_ECHO $appname  "application json file is successfully updated"
        $_LOGGER ${ME}: $($_WHOAMI): $appname application json file is successfully updated.
    else
        $_ECHO -e "No new actions to be updated.\nScript executed successfully."
        $_LOGGER ${ME}: $($_WHOAMI): $appname No new actions to be updated. Script executed successfully.
    fi

    # localeFile
    actualFile=$appsFolder/$appname/locales/en-us/app_actions.json
    $_DIFF $actualFile $CustomAppSdk/$appname/app_actions.json > /dev/null
    if [ $? -ne 0 ]
    then
        $_CP $CustomAppSdk/$appname/app_actions.json $actualFile
        $_ECHO $appname  "application locale file is successfully updated"
        $_LOGGER ${ME}: $($_WHOAMI): $appname application locale file is successfully updated.
    fi

    # rules
    actionsList=$(getActionLabels $appName)
    for action in $actionsList
    do
        label=$($_ECHO $action | $_CUT -d "-" -f 2- | $_SED "s/-/ /g" | $_SED 's/\b./\u&/g' )
        actionStatus=$(getActionStatus $label)
        if [ $actionStatus == "false" ] || [ $actionStatus == "False" ] || [ $actionStatus == "FALSE" ]
        then
            fileName=$($_LS -ltr $appsFolder/$appname/actions/rules/ | grep -i $action | awk '{print $9}')
            if [ ! -z "$fileName" ]
            then
                $_RM -f $CustomAppSdk/$appname/$fileName $appsFolder/$appname/actions/rules/$fileName
            fi
        fi
    done

    rulesFiles=$($_LS -ltr $CustomAppSdk/$appname/ | $_GREP -w 'any\|type' | $_AWK '{print $9}'| xargs)
    for ruleFile in $rulesFiles
    do
        $_LS -ltr $appsFolder/$appname/actions/rules/ | $_GREP -i $ruleFile >> /dev/null
        if [ $? -ne 0 ]
        then
            $_MV $CustomAppSdk/$appname/$ruleFile $appsFolder/$appname/actions/rules/
            $_CHOWN jboss_user:jboss $appsFolder/$appname/actions/rules/$ruleFile
            $_LOGGER ${ME}: $($_WHOAMI): Rule $ruleFile is successfully added to application $appname rules.
        fi
    done

    # remove temporary files
    $_RM -rf $CustomAppSdk/$1
}

#####################################################
#Rollback entire customactions.
# Globals:
#   backupDirectory
#   appsFolder
# Arguments:
#   None
# Returns:
#   None
####################################################
rollbackCustomActions() {
    appsToRestore=$($_LS -ltr $backupDirectory | $_GREP "\original" | $_AWK '{print $9}' | $_AWK -F "-" '{print $1}')
    for appname in $appsToRestore
    do
        $_CP -r $appsFolder/$appname $backupDirectory/$appname-backup
        $_RM -rf $appsFolder/$appname
        $_CP -r $backupDirectory/$appname-original $appsFolder/$appname
        $_CHOWN -R jboss_user:jboss $appsFolder/$appname
        if [ $? -eq 0 ]
        then
            $_ECHO $appname is successfully restored.
            $_LOGGER ${ME}: $($_WHOAMI): $appname is successfully restored.
            $_RM -rf $backupDirectory/$appname-backup
        fi
    done
}

#####################################################
#Parses input file for the action labels.
# Globals:
#   backupDirectory
#   appsFolder
# Arguments:
#   application Name
# Returns:
#   actionLabels
####################################################
getActionLabels() {
    applicationName=$1
    startString=$($_GREP appName $InputFile | $_GREP $applicationName)
    actionLabels=$($_SED -n -e "/${startString}/,/appName/ p" $InputFile | $_EGREP -i "(\"guiLabel\"|\"scriptNameLabel\")" | $_AWK -F ":" '{print $2}' | $_SED "s/\"//g" | $_SED "s/,//g" | $_SED "s/\'//g" | $_SED "s/^ //g" | $_SED "s/ /-/g" | $_SED -e "s/^/"$applicationName"-/" | xargs)
    $_ECHO $actionLabels
}

#####################################################
#Parses input file for the status of the action
# Globals:
#   backupDirectory
#   appsFolder
# Arguments:
#   action Name
# Returns:
#   actionStatus
####################################################
getActionStatus() {
    label=$1
    appname=$($_ECHO $al | $_CUT -d "-" -f 1)
    startString=$($_GREP appName $InputFile | $_GREP $appname)
    $_SED -n -e "/${startString}/,/appName/ p" $InputFile > /var/tmp/appFile
    label_name=$($_ECHO $al | $_CUT -d "-" -f 2- | $_SED "s/-/ /g" | $_SED 's/\b./\u&/g' )
    guiLabelString=$($_GREP -w -i "$label_name" /var/tmp/appFile | $_GREP "guiLabel")
    isActionEnabled=$($_SED -n -e "/${guiLabelString}/,/actionEnable/ p" /var/tmp/appFile | grep -i actionEnable | awk -F ":" '{print $2}' | sed "s/\"//g" | sed "s/ //g" | sed "s/,//g")
    $_RM -f /var/tmp/appFile
    $_ECHO  $isActionEnabled
}

#########################################################
#Validates the arguments and pre-requisites of the script
# Globals:
#   InputFile
#   appsFolder
# Arguments:
#   appNames: Names of the application directories.
# Returns:
#   None
#########################################################
argumentValidator() {
    _ECHO="/bin/echo"
    # Validating the number of arguments provided. Checking if atleast one argument is provided.
    if [ $# -lt 1 ]
    then
        $_ECHO -e "\n  \t ################## Please provide atleast one argument ##################"
        $_ECHO -e "\tPlease execute script as shown below "
        $_ECHO -e "\n\tUsage: \t" $0 "<absolute path for the application-sdk-tools-<version> directory > \n\tExample: " $0 " /root/application-sdk-tool/ \n"
        /bin/rm -rf /var/tmp/customApplicationLock/
        exit 1
    fi

    # Validating the number of arguments provided.
    if [ $# -gt 2 ]
    then
        $_ECHO -e "\n  \t ################## Please provide only one argument ##################"
        $_ECHO -e "\tPlease execute script as shown below "
        $_ECHO -e "\n\tUsage: \t" $0 "<absolute path for the application-sdk-tools-<version> directory > \n\tExample: " $0 " /root/application-sdk-tool/ \n"
        /bin/rm -rf /var/tmp/customApplicationLock/
        exit 1
    fi

    if [ $1 == "--rollback" ] || [ $1 == "--Rollback" ]
    then
        $_ECHO -e "\n\tAre you sure to delete all the custom actions and restore the application json files (Yes/No)"
        read response
        if [ $response == "yes" ] || [ $response == "Yes" ] || [ $response == "YES" ] || [ $response == "Y" ] || [ $response == "y" ]
        then
            source $2/bin/constants
            rollbackCustomActions
        else
            $_ECHO -e "\n\tNo changes made to the application json files"
        fi
        /bin/rm -rf /var/tmp/customApplicationLock/
        exit 0
    fi

    if [ $1 == "-h" ] || [ $1 == "--help" ] || [ $1 == "--h" ] || [ $1 == "-help" ]
    then
        $_ECHO -e " ################## Please execute the script as below ##################"
        $_ECHO -e "\n\tUsage: \t" $0 "<absolute path for the application-sdk-tools-<version> directory > \n\tExample: " $0 " /root/application-sdk-tool/ \n"
        /bin/rm -rf /var/tmp/customApplicationLock/
        exit 1
    fi

    # checking if the provided input is a valid directory.
    if [ ! -d $1 ]
    then
        $_ECHO -e "\n  \t################## Invalid directory ##################"
        $_ECHO -e "\tThe given directory " $1 " does not exist. Please execute as shown below "
        $_ECHO -e "\n\tUsage: \t" $0 "<absolute path for the application-sdk-tools-<version> directory > \n\tExample: " $0 " /root/application-sdk-tool/ \n"
        /bin/rm -rf /var/tmp/customApplicationLock/
        exit 1
    fi

    # checking if the provided input has the application sdk tool files.
    if [ ! -f $1/bin/custom_app_integration.sh ]
    then
        $_ECHO -e "\n  \t################## Invalid directory ##################"
        $_ECHO -e "\tNo application sdk tool files the given directory " $1 ". Please execute as shown below "
        $_ECHO -e "\n\tUsage: \t" $0 "<absolute path for the application-sdk-tools-<version> directory > \n\tExample: " $0 " /root/application-sdk-tool/ \n"
        /bin/rm -rf /var/tmp/customApplicationLock/
        exit 1
    fi
}

argumentValidator "$@"
source $CustomAppSdk/bin/constants
$_ECHO "Starting the script to add custom actions"
createApplicationDirectories
createJsonFiles
updateApps
$_ECHO "Script is successfully executed"
$_RM -rf /var/tmp/customApplicationLock/
