#!/bin/bash
. $(dirname $0)/setenv.sh

if [ ! $# -eq 2 ]
then
  echo "Usage: getplan.sh servicename planname"
  exit
fi

RESULT=`curl -s $SERVER_URL/admin/service/$1/plan/$2`
if [ -z "$RESULT" ]
then
  echo Service $1, Plan $2 not found
  exit
fi

function getJsonVal () { 
    python -c "import json,sys;sys.stdout.write(str(json.load(sys.stdin)['$1']))";
}

echo "Service Name: "`echo $RESULT | getJsonVal "serviceName"`
echo "Plan Name:    "`echo $RESULT | getJsonVal "name"`
echo "Description:  "`echo $RESULT | getJsonVal "description"`
echo "Credentials:  "`echo $RESULT | getJsonVal "credentials"`

CREDENTIALS=`curl -s $SERVER_URL/admin/service/$1/plan/$2/credential`
if [ ! "$CREDENTIALS" == "[]" ]
then
  echo
  echo Credentials:
  echo "`echo $CREDENTIALS | $(dirname $0)/printjsontable.py 'key,value' 'Key,Value'`"
fi

SERVICE_INSTANCES=`curl -s $SERVER_URL/admin/service/$1/plan/$2/serviceinstance`
if [ ! "$SERVICE_INSTANCES" == "[]" ]
then
  echo
  echo Service Instances:
  echo "`echo $SERVICE_INSTANCES | $(dirname $0)/printjsontable.py 'organization_guid,space_guid' 'Organization GUID,Space GUID'`"
fi

SERVICE_BINDINGS=`curl -s $SERVER_URL/admin/service/$1/plan/$2/servicebinding`
if [ ! "$SERVICE_BINDINGS" == "[]" ]
then
  echo
  echo Service Bindings:
  echo "`echo $SERVICE_BINDINGS | $(dirname $0)/printjsontable.py 'app_guid' 'Application GUID'`"
fi
