FROM armdocker.rnd.ericsson.se/proj_oss_releases/enm/access-control

RUN mkdir -p /ericsson/tor/data/presentation_server/config
COPY *.xml /ericsson/tor/data/presentation_server/config/

# access-control image provides the /ericsson/tor/data volume
# so we need to extend it to include the presentation_server folder
# and expose it to jboss image
VOLUME /ericsson/tor/data/ericsson/tor/data/presentation_server/config
#VOLUME /ericsson/tor/data/apps/my-app-01/
#VOLUME /ericsson/tor/data/apps/my-app-01/actions/rules
