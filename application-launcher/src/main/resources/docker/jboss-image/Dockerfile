FROM armdocker.rnd.ericsson.se/proj_oss_releases/enm/jboss-dps

COPY standalone-enm.xml $JBOSS_HOME/standalone/configuration/standalone-enm.xml
COPY overload-protection-config.json $JBOSS_HOME/standalone/configuration/overload-protection-config.json
COPY deployments/* $JBOSS_HOME/standalone/deployments

ENTRYPOINT ["entrypoint.sh"]
CMD ["-IPJ", "-s", "jboss"]