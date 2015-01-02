#!/bin/bash
. $(dirname $0)/setenv.sh

if [ ! $# -eq 3 ]
then
  echo "Usage: storeservice.sh name description isbindable"
  exit
fi

RESULT=`curl -s $SERVER_URL/admin/service/$1 -d "{
  \"description\":\"$2\",
  \"bindable\":$3
}" -X PUT -H "Content-Type:application/json"`
if [ -z "$RESULT" ]
then
  exit
fi

function getJsonVal () {
    python -c "import json,sys;sys.stdout.write(str(json.load(sys.stdin)['$1']))";
}
echo "Name:        "`echo $RESULT | getJsonVal "name"`
echo "Description: "`echo $RESULT | getJsonVal "description"`
echo "Is Bindable: "`echo $RESULT | getJsonVal "bindable"`
