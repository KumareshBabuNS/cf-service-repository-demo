curl http://admin:admin@localhost:8080/admin/service/SR-1/plan/P-1 -d '{
  "description": "Plan 1, updated",
  "credentials": [
    {
      "key": "Other Password",
      "value": "New password"
    },
    {
      "key": "Username",
      "value": "user"
    }
  ]
}' -X PUT -H "Content-Type:application/json"
echo
