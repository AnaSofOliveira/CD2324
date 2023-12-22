set /p "IP=Insira o IP do Broker do RabbitMQ: "
java -jar PointOfSales/target/PointOfSalesApp-1.0-jar-with-dependencies.jar %IP% 5672

pause