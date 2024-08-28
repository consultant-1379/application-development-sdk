#! /bin/bash

# This script is executed before JBoss, so it can be used to configure the environment.
#--------------------------------------------------------------------------------------

# Load the common functions
source docker-env-functions.sh

copy_jboss_config

startup.sh -NSJ

rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/current/* \
           /opt/ericsson/nms /opt/ericsson/service-framework

# Makes JBoss wait for DPS Integration image to be ready
wait_dps_integration
wait_postgres
