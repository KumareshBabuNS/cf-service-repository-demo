#!/bin/bash
. $(dirname $0)/setenv.sh

if [ ! $# -eq 2 ]
then
  echo "Usage: deleteplan.sh servicename planname"
  exit
fi

curl -s $SERVER_URL/admin/service/$1/plan/$2 -X DELETE
