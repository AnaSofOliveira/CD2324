set /p "IP=Insira o IP do Broker do RabbitMQ: "
set /p "PORT=Insira o porto do Broker do RabbitMQ: "
java -jar PointOfSales/target/PointOfSalesApp-1.0-jar-with-dependencies.jar %IP% %PORT%

pause