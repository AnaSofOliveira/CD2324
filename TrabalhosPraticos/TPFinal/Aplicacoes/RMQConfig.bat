set /p "IP=Enter RabbitMQ Broker IP: "
java -jar RMQConfigurator\target\RMQConfigurator-1.0-jar-with-dependencies.jar %IP%
pause