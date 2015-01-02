#!/bin/bash
. $(dirname $0)/setenv.sh

RESULT=`curl -s $SERVER_URL/admin/service`
if [ -z "$RESULT" ]
then
  exit
fi

echo "`echo $RESULT | $(dirname $0)/printjsontable.py 'name,description,bindable' 'Name,Description,Is Bindable'`"
