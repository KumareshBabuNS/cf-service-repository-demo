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
cd cf-service-repository-demo/
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

Getting apps in org xxx / space yyy as zzz...
OK

name             requested state   instances   memory   disk   urls
service-broker   started           1/1         512M     1G     service-broker-nonepiscopalian-funiculus.cfapps.io
```
