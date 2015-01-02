#!/bin/bash
. $(dirname $0)/setenv.sh

if [ ! $# -eq 1 ]
then
  echo "Usage: listplans.sh servicename"
  exit
fi

RESULT=`curl -s $SERVER_URL/admin/service/$1/plan`
if [ -z "$RESULT" ]
then
  echo $1 not found
  exit
fi
#echo $RESULT
echo "`echo $RESULT | $(dirname $0)/printjsontable.py 'name,description' 'Name,Description'`"
