#!/bin/bash
. $(dirname $0)/setenv.sh

if [ ! $# -eq 1 ]
then
  echo "Usage: getservice.sh name"
  exit
fi

RESULT=`curl -s $SERVER_URL/admin/service/$1`
if [ -z "$RESULT" ]
then
  echo $1 not found
  exit
fi

function getJsonVal () { 
    python -c "import json,sys;sys.stdout.write(str(json.load(sys.stdin)['$1']))";
}
echo "Name:        "`echo $RESULT | getJsonVal "name"`
echo "Description: "`echo $RESULT | getJsonVal "description"`
echo "Is Bindable: "`echo $RESULT | getJsonVal "bindable"`
