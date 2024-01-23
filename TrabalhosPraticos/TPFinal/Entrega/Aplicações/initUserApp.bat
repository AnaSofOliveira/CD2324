set /p "IP=Insira o IP do Servidor: "
set /p "PORT=Insira o porto do Servidor: "
java -jar UserApp\target\UserApp-1.0-jar-with-dependencies.jar %IP% %PORT%