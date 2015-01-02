./gradlew assemble
cf push
sleep 5
cf create-service-broker service-broker admin admin http://service-broker-bboe.10.244.0.34.xip.io
cf service-brokers
cf enable-service-access SR-1
cf enable-service-access SR-2
cf service-access
