#!/usr/bin/env bash

# This script updates your running docker environment to use the real XML and JSON files instead the test data.
# You need to run this script on a clean docker environment. Make sure to run docker-clean-all.sh first
# ------------------------------------------------------------------------------------------------------------

# Removes the test XMLs from the file system
docker exec docker_ps_jboss_1 rm -rf /ericsson/tor/data/presentation_server

# Copy the RPM to the server
RPM_NAME=$(ls ../../../../ERICps_CXP9030203/target/rpm/ERICps_CXP9030203/RPMS/noarch/*.rpm | xargs -n 1 basename)
docker cp ../../../../ERICps_CXP9030203/target/rpm/ERICps_CXP9030203/RPMS/noarch/${RPM_NAME} docker_ps_jboss_1:/tmp/

# Install the RPM (this should copy the default JSON files)
docker exec docker_ps_jboss_1 rpm -i /tmp/${RPM_NAME} --force
EAR_FILE=$(docker exec docker_ps_jboss_1 ls /opt/ericsson/ERICps_CXP9030203)

# Updates PIB
JBOSS_IP_ADDRESS=$(docker inspect --format '{{ .NetworkSettings.Networks.docker_default.IPAddress }}' docker_ps_jboss_1)
PIB_HOME=/opt/ericsson/PlatformIntegrationBridge/etc

docker exec docker_ps_jboss_1 ${PIB_HOME}/config.py update \
    --app_server_address=${JBOSS_IP_ADDRESS}:8080 --name=PresentationService_webHost \
    --value="default:localhost,ossMonitoringHost:localhost,alexHost:localhost,esmon:localhost" \
    --type="String[]" --service_identifier=presentation-server --scope=SERVICE

# Deploy Presentation Server
docker exec docker_ps_jboss_1 cp /opt/ericsson/ERICps_CXP9030203/${EAR_FILE} /ericsson/3pp/jboss/standalone/deployments

docker exec docker_ps_jboss_1 \
    /ericsson/3pp/jboss/bin/jboss-cli.sh \
        --connect \
        --controller=${JBOSS_IP_ADDRESS}:9999 \
        --command="deploy /ericsson/3pp/jboss/standalone/deployments/${EAR_FILE}"