#Issues and To-Do List
* Service repository is currently stored in an in-memory database, which will be reset if the service application is restarted.
* Adding/removing services or plans in the registry requires a separate update of the marketplace in Cloud Foundry by executing ```cf update-service-broker```. This part can be automated, but that requires the repository to have admin credentials back to Cloud Foundry.
* Cloud Foundry plans are not designed have the plans credentials change. A simple ```cf restage``` will not go back to the service broker to refresh the credentials. In order for that to happen, the service needs to be unbinded, binded back to the application and the have it restaged.
* The current implementation of the service repository does not support nested structures for credentials.

