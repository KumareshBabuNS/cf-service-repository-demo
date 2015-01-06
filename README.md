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
