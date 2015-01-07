#Cloud Foundry Service Repository Demo
Small demo that demonstrates how to build and deploy your own service broker on Cloud Foundry. The demo implements a simple service repository where you can add your own services, which can then be linked into any application deployed on Cloud Foundry.

This demo is not production quality and see [issues-todo](ISSUES-TODO.md) for known issues and potential improvements that can be made to this demo.

##Deployment Instructions
###Download Source Code from Repository
```
git clone https://github.com/bboe-pivotal/cf-service-repository-demo/
```
###Compile and Deploy Server Components
* Go to cf-service-repository-demo directory
```
cd cf-service-repository-demo
```
* Build application using Gradle
```
./gradlew clean assemble
```
* Push to Cloud Foundry using settings in local manifest.yml
```
cf push
```
Use the following command to see more information on the applications that were just deployed:
```
cf apps
Getting apps in org me / space development as admin...
OK

name                 requested state   instances   memory   disk   urls
service-repository   started           1/1         512M     1G     service-repository...
```
###Register and enable the service repository as a service broker within Cloud Foundry
* Create service broker
```
cf create-service-broker service-repository admin admin http://service-repository...
```
Note that the admin/admin username/password is defined in the file src/main/resources/application.properties. Also remember to to update the URL according to your environment.
* Verify the service broker has been deployed by executing ```cf service-brokers```
```
cf service-brokers
Getting service brokers as admin...

name                 url
service-repository   http://service-repository...
```
* List the service repository with the command ```cf service-access``` to see the plans available by default.
```
cf service-access
Getting service access as admin...
broker: service-repository
   service             plan        access   orgs
   ServiceRepository   DummyPlan   none
```
The repository comes by default with one service and one plan as this is requires as a minimum by Cloud Foundry. This can be reconfigured later on.
* Enable access to the newly registered plan to everybody
```
cf enable-service-access ServiceRepository
```
* Repeat listing the service repository with ```cf service-access``` to see access enabled for the new service
```
cf service-access
Getting service access as admin...
broker: service-repository
   service             plan        access   orgs
   ServiceRepository   DummyPlan   all
```
* Run ```cf marketplace``` to verify that the service and plan shows up on the marketplace.
```
cf marketplace
Getting services from marketplace in org me / space development as admin...
OK

service             plans       description
ServiceRepository   DummyPlan   Service Repository
```
##How to configure a demo application to leverage the service repository
The following examples leverages a Hello World demo application where there is a client application talking to one out of two possible back-ends to do a Hello World!
###Deploy the Hello World application
Follow the instructions for the [cf-hello-world-demo](https://github.com/bboe-pivotal/cf-hello-world-demo/blob/master/README.md) to deploy the demo application.

Use the ```cf apps``` command to verify the right components have been deployed.
```
cf apps
Getting apps in org me / space development as admin...
OK

name                 requested state   instances   memory   disk   urls
hello-client         started           1/1         512M     1G     hello-client...
service-repository   started           1/1         512M     1G     service-repository...
hello-svc-english    started           1/1         512M     1G     hello-svc-eng...
hello-svc-italian    started           1/1         512M     1G     hello-svc-ita...
```
The following apps should now be running:
* hello-client - Hello World client application
* hello-svc-english - Hello World service, exposing a REST service doing Hello World in English, which is called by hello-client
* hello-svc-italian - Hello World service, exposing a REST service doing Hello World in Italian, which is called by hello-client
* service-repository - The service repository installed earlier in this document
##Register hello world services in service repository
These instructions leverages a set of scripts in the admintool directory. The endpoint information is configured in setenv.sh. The file may look as follows:
```
export SERVER_URL=http://admin:admin@service-repository...
```
* Run ```listservices.sh``` to list the services in the repository.
```
./listservices.sh
Name               Description         Is Bindable
ServiceRepository  Service Repository  True
```
* Run ```listplans.sh``` to list the plans that belong to the configured service.
```
./listplans.sh ServiceRepository
Name       Description
DummyPlan  Dummy Plan
```
* Add the English hello world service to the repository by using ```storeplan.sh```
```
./storeplan.sh ServiceRepository Hello-World-English "Hello World in English" uri
Service Name: ServiceRepository
Plan Name: Hello-World-English
Description: Hello World in English
Value for uri:
http://hello-svc-eng...

New version:
Service Name: ServiceRepository
Plan Name:    Hello-World-English
Description:  Hello World in English
Credentials:  [{u'planName': u'Hello-World-English', u'serviceName': u'ServiceRepository', u'value': u'http://hello-svc-eng...', u'key': u'uri'}]

Credentials:
Key  Value
uri  http://hello-svc-eng...
```
* Add the Italian hello world service to the repository by using ```storeplan.sh```
```
./storeplan.sh ServiceRepository Hello-World-Italian "Hello World in Italian" uri
Service Name: ServiceRepository
Plan Name: Hello-World-Italian
Description: Hello World in Italian
Value for uri:
http://hello-svc-ita...

New version:
Service Name: ServiceRepository
Plan Name:    Hello-World-Italian
Description:  Hello World in Italian
Credentials:  [{u'planName': u'Hello-World-Italian', u'serviceName': u'ServiceRepository', u'value': u'http://hello-svc-ita...', u'key': u'uri'}]

Credentials:
Key  Value
uri  http://hello-svc-ita...
```
* Delete the dummy plan by using ```deleteplan.sh```
```
./deleteplan.sh ServiceRepository DummyPlan
```
* Run ```listplans.sh``` to see the changes made to the repository
```
./listplans.sh ServiceRepository
Name                 Description
Hello-World-English  Hello World in English
Hello-World-Italian  Hello World in Italian
```
* Note that changing the underlying repository will not refresh the marketplace in Cloud Foundry. Run ```cf update-service-broker``` to trigger an update
```
cf update-service-broker service-repository admin admin http://service-repository...
Updating service broker service-repository as admin...
OK
Warning: Service plans are missing from the broker's catalog (http://service-repository...) but can not be removed from Cloud Foundry while instances exist. The plans have been deactivated to prevent users from attempting to provision new instances of these plans. The broker should continue to support bind, unbind, and delete for existing instances; if these operations fail contact your broker provider.
ServiceRepository
  DummyPlan
```
The error can be ignored as the DummyPlan has not been used for anything in this case.
* Enable access to the newly registered plan to everybody
```
cf enable-service-access ServiceRepository
```
* Run ```cf marketplace``` to verify that the service and plan shows up on the marketplace.
```
cf marketplace
Getting services from marketplace in org me / space development as admin...
OK

service             plans                                      description
ServiceRepository   Hello-World-English, Hello-World-Italian   Service Repository
```
###Reconfigure the Hello World client to leverage the service repository instead of the user provided service
* Create a service instance for the English hello world service
```
cf create-service ServiceRepository Hello-World-English hello-service-english
```
* Create a service instance for the Italian hello world service
```
cf create-service ServiceRepository Hello-World-Italian hello-service-italian
```
* Running ```cf services``` will display the two service instances just created, as well as some user provided services created earlier.
```
cf services
Getting services in org me / space development as admin...
OK

name                    service             plan                  bound apps
hello-english           user-provided                             hello-client
hello-italian           user-provided
hello-service-english   ServiceRepository   Hello-World-English
hello-service-italian   ServiceRepository   Hello-World-Italian
```
* Unbind hello-client from user provided service
```
cf unbind-service hello-client hello-english
```
* Bind hello-client to service provided by service repository
```
cf bind-service hello-client hello-service-italian
```
* Restage client application to make sure it uses the new service
```
cf restage hello-client
```
* Delete old user provided services
```
cf delete-service hello-english -f
cf delete-service hello-italian -f
```
* Run ```cf services``` to verfiy the old user provided services are gone and that the client is leveraging the new service provided by the service registry.
```
cf services
Getting services in org me / space development as admin...
OK

name                    service             plan                  bound apps
hello-service-english   ServiceRepository   Hello-World-English
hello-service-italian   ServiceRepository   Hello-World-Italian   hello-client
```
* Run the client to verify the Hello World client is working
