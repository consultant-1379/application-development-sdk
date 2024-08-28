#!/bin/bash

SLEEP_INTERVAL=60

function gracefulShutdown {
  for dir in $(ls -d /tmp/volume_mount_*); do
  echo "Assigning permissions go+rwx recursively to $dir"
      chmod -R go+rwx $dir
  done
}

trap gracefulShutdown SIGTERM

function wait_indefinitely {
    while true
    do
       echo "Sleeping for $SLEEP_INTERVAL"
       sleep $SLEEP_INTERVAL
    done
}

echo "Running cleanup container"
wait_indefinitely &
wait

