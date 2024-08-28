FROM centos:centos6

ADD cleanup.sh /cleanup.sh

RUN chmod -v +x /cleanup.sh

ENTRYPOINT ["/cleanup.sh"]
