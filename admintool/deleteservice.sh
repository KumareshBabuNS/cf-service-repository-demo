#!/bin/bash
. $(dirname $0)/setenv.sh

if [ ! $# -eq 1 ]
then
  echo "Usage: deleteservice.sh servicename"
  exit
fi

curl -s $SERVER_URL/admin/service/$1 -X DELETE
