./gradlew assemble
cf push
sleep 5
cf create-service-broker cf-service-broker admin admin http://cf-service-broker.10.244.0.34.xip.io
cf service-brokers
cf enable-service-access CFServiceBroker
cf service-access
