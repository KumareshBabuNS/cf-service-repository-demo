curl http://admin:admin@localhost:8080/admin/service/SR-3 -d '{
  "description":"Broker number 3",
  "bindable":true
}' -X PUT -H "Content-Type:application/json"
echo
