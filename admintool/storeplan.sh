#!/bin/bash
. $(dirname $0)/setenv.sh

if [ ! $# -gt 3 ]
then
  echo "Usage: storeplan.sh servicename planname description <attribute key 1> <attribute key 2>..."
  exit
fi

echo Service Name: $1
echo Plan Name: $2
echo Description: $3

keyvalue=''

for i in ${@:4}
do 
  echo Value for $i:
  read input
  #echo You wrote $input
  if [ ! -z "$keyvalue" ]
  then
    keyvalue=$keyvalue,
  fi
  keyvalue=$keyvalue{\"key\":\"$i\",\"value\":\"$input\"}
done
#echo Input: $keyvalue

RESULT=`curl -s $SERVER_URL/admin/service/$1/plan/$2 -d "{
  \"description\":\"$3\",
  \"credentials\":[$keyvalue]
}" -X PUT -H "Content-Type:application/json"`
echo
echo New version:
$(dirname $0)/getplan.sh "$1" "$2"
