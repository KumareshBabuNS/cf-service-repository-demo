#Cloud Foundry Service Repository Demo
Small demo that demonstrates how to build and deploy your own service broker on Cloud Foundry. The demo implements a simple service repository where you can add your own services, which can then be linked into any application deployed on Cloud Foundry.

##Deployment Instructions
###Download Source Code from Repository
```bash
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
service-repository   started           1/1         512M     1G     service-repository-stalkless-overbrutality.10.244.0.34.xip.io
```
###Register and enable the service repository as a service broker within Cloud Foundry
* Create service broker
```
cf create-service-broker service-repository admin admin http://service-repository-stalkless-overbrutality.10.244.0.34.xip.io
```
Note that the admin/admin username/password is defined in the file src/main/resources/application.properties. Also remember to to update the URL according to your environment.
* Verify the service broker has been deployed by executing ```cf service-brokers```
```
cf service-brokers
Getting service brokers as admin...

name                 url
service-repository   http://service-repository-stalkless-overbrutality.10.244.0.34.xip.io
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
hello-client         started           1/1         512M     1G     hello-client-trancelike-bootjack.10.244.0.34.xip.io
service-repository   started           1/1         512M     1G     service-repository-stalkless-overbrutality.10.244.0.34.xip.io
hello-svc-english    started           1/1         512M     1G     hello-svc-eng-unbenefited-stealage.10.244.0.34.xip.io
hello-svc-italian    started           1/1         512M     1G     hello-svc-ita-puffier-nonagreement.10.244.0.34.xip.io
```
The following apps should now be running:
* hello-client - Hello World client application
* hello-svc-english - Hello World service, exposing a REST service doing Hello World in English, which is called by hello-client
* hello-svc-italian - Hello World service, exposing a REST service doing Hello World in Italian, which is called by hello-client
* service-repository - The service repository installed earlier in this document
##Register hello world services in service repository
These instructions leverages a set of scripts in the admintool directory. The endpoint information is configured in setenv.sh. The file may look as follows:
```
export SERVER_URL=http://admin:admin@service-repository-stalkless-overbrutality.10.244.0.34.xip.io
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
```
