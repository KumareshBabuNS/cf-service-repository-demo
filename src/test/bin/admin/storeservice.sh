curl http://admin:admin@localhost:8080/admin/service/SR-1 -d '{
  "description":"Service Repository 1, updated",
  "bindable":true
}' -X PUT -H "Content-Type:application/json"
echo
