### Exame Época Normal 2021-2022

1. __Sobre as caracteristicas dos sistemas distribuídos:__
- O teorema CAP demonstra que a escalabilidade de um sistema depende da capacidade de CPU. 
___Resposta:__ Falso. O teorema CAP não está relacionado com a capacidade do CPU, mas sim com a Consistência, Disponibilidade e Tolerância às partições de um sistema._
- O desacoplamento (loosely coupled) entre partes, depende da existência de contratos, por exemplo contratos protobuf em gRPC, para separar as implementações do cliente e do servidor. 
___Resposta:__ Verdadeiro. O desacoplamento entre partes depende da exitência de contratos, como os cotratos protobuf em gRPC._
- Um stub de cliente em gRPC, bloqueante ou não bloqueante facilita a total transparência à localização que é uma das caracteristicas desejáveis nos sitemas distribuídos. 
___Resposta:__ Verdadeiro. Um stub de cliente em gRPC facilita a total transparência à localização._
- A latência é um fator irrelevante podendo ser zero, em aplicações distribuídas. 
___Resposta:__ Falso. A latência é um fator relecante em aplicações distribuídas e não pode ser zero._

<br>

2. __Sobre o conceito de Tempo e Coordenação em sistemas distribuídos:__
- Os valores de um relógio físico (hh:mm:ss) podem ser sincronixados entre multiplos computadores usando o protocolo Network Time Protocol (NTP).
___Resposta:__ Verdadeiro. Os valores de um relógio físico podem ser sincronizados entre multiplos computadores usando o protocolo NTP._ 
- Devido à inexistência de um relógio global, exatamente igual em todos os computadores, a ordenação de eventos tem de recorrer a relógios lógicos como proposto por Lesluie Lamport em 1978. 
___Resposta:__ Verdadeiro. Devido à inexistência de um relógio global, a ordenação de eventos tem de recorrer a relógios lógicos._
- Os relógios lógicoscomo proposto por Leslie Lamport em 1978 permitem ordenar os eventos e detetar dependências causais entre eventos num sistema distribuído. 
___Resposta:__ Verdadeiro. Os relógios lógicos permitem ordenar os eventos e detetar dependências causaus entre eventos num sistema distribuído._
- Os relógios lógicos vetoriais transportam nas mensagens valores dos relógios entre participantes que permitem detetar dependências causais (a ocorrência de um evento ser consequência de outros). 
___Resposta:__ Verdadeiro. Os relógios lógicos vetoriais transportam nas mensagens valores dos relógios entre participantes que permitem detetar dependências causais._

<br>

3. __Relativamente ao desenvolvimento de aplicações em gRPC:__
- Quando numa operação rpc se usa streaming de cliente e de servidor, a cada mensagem do cliente tem de corresponder uma mensagem de resposta do servidor. 
___Resposta:__ Falso. Quando num operação rpc se usa streaming de cliente e se servidor, não é necessário que cada mensagem do cliente corresponsa uma mensagem de resposta do servidor._
- Uma operação que usa streaming de cliente e de servidor não pode ser chamada com um stub bloqueante. 
___Resposta:__ Verdadeiro. Uma operação que usa streaming de cliente e de servidor não pode ser chamada com um stub bloqueante._
- Numa operação com stream de cliente, o cliente tem a garantia que as mensagens chegam ao servidor pela ordem com que foram enviadas. 
___Resposta:__ Verdadeiro. Numa operação com stream de cliente, o cliente tem a garantia que as mensagens chegam ao servidor pela ordem com que foram enviadas._
- Um servidor gRPC pode implementar diferentes contratos protobuf para diferentes tipos de cliente. 
___Resposta:__ Verdadeiro. Um servidor gRPC pode implementar diferentes contratos protobuf para diferentes tipos de cliente._

<br>

4. __Sobre o RabbitMQ:__
- Para evitar que se percam mensagens enviadas com routing keys inválidas um exchange pode ter associado um alternate-exchange. 
___Resposta:__ Verdadeiro. Para evitar que se percam mensagens enviadas com routing keys inválidas um exchange pode ter associado um alternate-exchange._
- Sobre uma fila (queue) pode usar-se a politica de work-queue para certificar que todos os consumidores desse queue recebem todos as mesmas mensagens. 
___Resposta:__ Falso. Sobre uma fila (queue) a politica de work-queue garante que cada mensagem é entregue a um único consumidor, e não a todos._ 
- Quando um consumidor dá acknowledge negativos a uma mensagem o produtor da mensagem não recebe nenhuma notificação. 
___Resposta:__ Verdadeiro. Quando um consumidor dá acknowledge negativos a uma mensagem o produtor da mensagem não recebe nenhuma notificação._ 
- Quando um consumidor dá acknowledge negativos a uma mensagem a mesma é descartada, não sendo recebida posteriormente por qualquer outro consumidor. 
___Resposta:__ Verdadeiro.Quando um consumidor dá acknowledge negativos a uma mensagem a mesma é descartada, não sendo recebida posteriormente por qualquer outro consumidor._

<br>

5. __Sobre algoritmos de exclusão mútua, eleição e consensos:__
- O algoritmo de exclusão mútua Ricart&Agrawala que envolve um número elevado (N) de participantes é pouco eficiênte pois cada autorização de acesso requer 2*(N-1) mensagens. 
___Resposta:__ Verdadeiro. O algoritmo de exclusão mútua Ricart&Agrawala que envolve um elevado número (N) de participantes é pouco eficiênte pois cada autorização de acesso requer 2*(N-1) mensagens._
- No algoritmo Raft o consenso é garantido pela eleição de um servidor líder, que tem a responsabilidade de coordenar a atualização das réplicas dos logs que servem de base às atualizações de dados. 
___Resposta:__ Verdadeiro. No algoritmo Raft o consenso é garantido pela eleição de um servidor líder._
- O protocolo Two-Phase commit (2PC) é muito utilizado na coordenação de consensos pois é total é totalmente tolerante a falhas do coordenador ou de algum dos participantes. 
___Resposta:__ Falso. O protocolo Two-Phase commit (2PC) não é totalmente tolerante a falhas do coordenador ou de algum dos participantes._
- O algoritmo de eleição Bully utiliza relógios lógicos, sendo eleito o coordenador que tiver menor relógio. 
___Resposta:__ Falso. O algoritmo de eleição Bully não utiliza relógios lógicos, mas sim a identifidade dos processos (o processo com a maior entidade é eleito)._

<br>

6. Considere um sistema distribuído, constituído por 3 participantes (p0, p1, p2), que utiliza comunicação por multicast fiável e um mecanismo baseado em relógios vetoriais que garante ordenação causal de mensagens. Quando p0 envia a mensagem com o vetor [5, 2, 6], justifique a razão porque acontece o seguinte nos participantes p1 e p2: 
    <br>
    6.1. Como p1 tem o seu vetor local p1=[3, 3, 5], em p1 a mensagem vista de p0 é retida (Hold). 
    ___Resposta:__ O vetor local é [3, 3, 5] e o vetor na mensagem de p0 é [5, 2, 6]. Como o valor para p0 na mensagem (5) não é exatamente um maior que o valor correspondente no vetor local (3), a mensagem é retida. Ou seja, p1 ainda espera pela mensagem 4 de p0._ 
    <br>
    6.2. Como p2 tem o seu vetor local p1=[4, 1, 6], em p2 a mensagem vista de p0 é retida (Hold). 
    ___Resposta:__ O vetor local é [4, 1, 6] e o vetor na mensagem de p0 é [5, 2, 6]. retida porque o valor para p1 na mensagem (2) é maior que o valor correspondente no vetor local (1). Isso indica que p2 ainda não recebeu uma mensagem de p1 que deve ser entregue antes desta mensagem de p0._

<br>

7. Considere que usa o middleware de comunicação por grupos (Spread Toolkit) no desenvolvimento de uma aplicação Cliente/Servidor, onde se pretende ter um grupo de multiplos servidores, em que cada um trata o mesmo pedido P com algoritmos diferentes e o cliente obtém as multiplas respostas R1, ... Rn, podendo escolher as que considerar mais adequadas. Desenhe um esquema de arquitetura e descreva com frases simples como implementava o sistema. 

    __Resposta:__ A arquitetura geral seria composta por um cliente e um grupo de servidores. O cliente e o servidores estariam todos conetados através do middleware de comunicação por grupos Spread. 
    - O cliente envia um pedido P para um grupo de servidores através do Spread; 
    - Cada servidor recebe o pedido e processa-o usando o seu próprio algoritmo; 
    - Cada servidor envia a sua resposta R1, ..., Rn de volta para o cliente através do Spread; 
    - O cliente recebe todas as respostas dos servidores e escolhe a mais adequada. 

    ##### Cliente
    ```java
    import spread.*;

    public class Cliente {
        public static void main(String[] args) throws Exception {
            SpreadConnection connection = new SpreadConnection();
            connection.connect(InetAddress.getByName("localhost"), 4803, "cliente", false, true);

            SpreadMessage msg = new SpreadMessage();
            msg.setObject("Pedido P");
            msg.addGroup("servidores");
            msg.setReliable();
            connection.multicast(msg);

            while (true) {
                SpreadMessage resposta = connection.receive();
                System.out.println("Resposta recebida: " + resposta.getObject());
            }
        }
    }

    ```

    ##### Servidor
    ```java
    import spread.*;

    public class Servidor {
        public static void main(String[] args) throws Exception {
            SpreadConnection connection = new SpreadConnection();
            connection.connect(InetAddress.getByName("localhost"), 4803, "servidor", false, true);
            connection.join("servidores");

            while (true) {
                SpreadMessage msg = connection.receive();
                String pedido = (String) msg.getObject();
                System.out.println("Pedido recebido: " + pedido);

                // Processar o pedido com o algoritmo específico do servidor
                String resposta = processarPedido(pedido);

                SpreadMessage respostaMsg = new SpreadMessage();
                respostaMsg.setObject(resposta);
                respostaMsg.addGroup(msg.getSender());
                respostaMsg.setReliable();
                connection.multicast(respostaMsg);
            }
        }

        private static String processarPedido(String pedido) {
            // Implementar o algoritmo específico do servidor aqui
            return "Resposta para " + pedido;
        }
    }

    ```

8. Considere uma imagem Docker com uma aplicação servidora gRPC que aceita conexões clientes através do porto 8000 e que escreve mensagens de logging no ficheiro /user/app/logs.txt. Considerando que só tem um computador (host) com suporte para Docker, descreva como executar nesse computador host multiplos gRPC (app1, ..., appN), em que todos eles escreviam os logs no computador host em ficheiros com os nomes /usr/app1/logs.txt, ..., /usr/appN/logs.txt.

    __Resposta:__ Para executar num computador (host) multiplas aplicações seria necessário criar uma imagem Docker.
    Após isso, ao iniciar um Docker container com a aplicação é necessário mapear os portos do host e do container, bem como mapear um volume no host onde os ficheiros criados nos containers ficariam armazenados. 

    ```linux
    docker run -d -p 8001:8000 -v /usr/app/app1:/user/app nome_imagem
    ```

9. Considere um cenário de controlo de excesso de velocidade média entre os pontos P1 e P2 de uma estrada.
Cada veículo tem um dispositivo eletrónico que ao passar em determinados pontos (P1 e P2 na figura) envia por uma rede sem fios a sua matrícula para um equipamento (fora do âmbito da questão). Esse equipamento executa um cliente que comunica com um servidor gRPC que disponibiliza o contrato Cc. O cliente envia para o servidor uma mensagem com matrícula do veículo e o valor de relógio (hh:mm:ss) de passagem do veículo. Cada servidor, por exemplo o existente no ponto P1, reenvia a mensagem (matrícula, hora) para o servidor seguinte (P2 na figura) que disponibiliza um contrato entre servidores (Cis).
Quando um veículo passa no segundo servidor é registada da mesma forma a matrícula e a hora de passagem que permitirá calcular se o veículo ultrapassou a velocidade média (na figura 100 Km/h). Embora fora do âmbito desta questão, se o veículo ultrapassar essa velocidade média o servidor regista numa base de dados esse facto para que posteriormente o proprietário do veículo seja notificado pela contraordenação.
Considerando o middleware gRPC, proponha os contratos (Cc e Cis) em protobuf, os pressupostos das configurações, bem como o esboço em Java das aplicações (Server e Client), nomeadamente as classes necessárias. Em particular apresente a funcionalidade da operação do servidor que recebe a mensagem do cliente e envia a notificação para o servidor seguinte.

```protobuf
// velocidade.proto

syntax = "proto3";

package velocidade;

// Contrato Cc
service VelocidadeControl {
  rpc RegistraPassagem(VeiculoInfo) returns (Resposta);
}

// Contrato Cis
service InterServer {
  rpc NotificaPassagem(VeiculoInfo) returns (Resposta);
}

message VeiculoInfo {
  string matricula = 1;
  string hora = 2;
}

message Resposta {
  string status = 1;
}

```

##### Servidor

```java 
import io.grpc.Server;
import io.grpc.ServerBuilder;
import velocidade.VelocidadeControlGrpc.VelocidadeControlImplBase;
import velocidade.VelocidadeControlOuterClass.VeiculoInfo;
import velocidade.VelocidadeControlOuterClass.Resposta;

public class VelocidadeServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(8000)
            .addService(new VelocidadeControlImpl())
            .build()
            .start();
        server.awaitTermination();
    }

    static class VelocidadeControlImpl extends VelocidadeControlImplBase {
        @Override
        public void registraPassagem(VeiculoInfo request, StreamObserver<Resposta> responseObserver) {
            // Implementar a lógica para registrar a passagem do veículo e notificar o próximo servidor
        }
    }
}
```

##### Cliente

```java
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import velocidade.VelocidadeControlGrpc;
import velocidade.VelocidadeControlOuterClass.VeiculoInfo;
import velocidade.VelocidadeControlOuterClass.Resposta;

public class VelocidadeClient {
    public static void main(String[] args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8000)
            .usePlaintext()
            .build();

        VelocidadeControlGrpc.VelocidadeControlBlockingStub stub = VelocidadeControlGrpc.newBlockingStub(channel);

        VeiculoInfo veiculo = VeiculoInfo.newBuilder()
            .setMatricula("ABC-1234")
            .setHora("12:34:56")
            .build();

        Resposta resposta = stub.registraPassagem(veiculo);

        channel.shutdown();
    }
}

```


10. Considere uma empresa financeira de comercialização de ativos financeiros, nomeadamente crypto moedas, ações etc. Nessa empresa existem dezenas de funcionários a analisar e a dar respostas aos milhares de pedidos diários dos clientes. Esses pedidos podem indicar uma das seguintes 4 operações: Compra; Venda; Troca; Criação de NFT (non-fungible tokens). Utilizando unicamente o middleware RabbitMQ, proponha a arquitetura (Exchanges, Queues, respetivos Bindings e Routing Keys necessários) de um sistema com os
seguintes requisitos:
• Associado a cada operação podem existir múltiplos funcionários normais, em número variável ao longo dos dias. Por exemplo, nalguns dias é normal existir mais pedidos sobre compras, exigindo mais funcionários;
• Quando um funcionário considera que um pedido é complexo, por exemplo criação de NFT ou Troca de ativos, reenvia o pedido para um departamento especial, onde outros funcionários especializados tratam desse pedido;
• Para efeitos de registo, controlo de qualidade e possíveis futuras auditorias, todas as operações pedidas pelos clientes são guardadas num ficheiro de Logging;
Apresente os troços de código Java essenciais para a aplicação do cliente que submete pedidos e as aplicações usadas pelos funcionários (NormalApp e EspecializadoApp ) que analisam e tratam as várias operações pedidas pelos clientes da empresa financeira.


##### Cliente
``` java
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ClienteApp {
    private final static String EXCHANGE_NAME = "pedidos";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            String pedido = "Compra";
            channel.basicPublish(EXCHANGE_NAME, pedido, null, pedido.getBytes("UTF-8"));
            System.out.println(" [x] Pedido enviado '" + pedido + "'");
        }
    }
}
```

##### NormalApp
```java
import com.rabbitmq.client.*;

public class NormalApp {
    private final static String EXCHANGE_NAME = "pedidos";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, EXCHANGE_NAME, "Compra");
        channel.queueBind(queueName, EXCHANGE_NAME, "Venda");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Recebido '" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}

```

##### EspecializadoApp
```java

import com.rabbitmq.client.*;

public class EspecializadoApp {
    private final static String EXCHANGE_NAME = "pedidos";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, EXCHANGE_NAME, "Troca");
        channel.queueBind(queueName, EXCHANGE_NAME, "Criação de NFT");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Recebido '" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
```

11. Considere que usa o middleware de comunicação por grupos (Spread Toolkit) conjuntamente com o middleware gRPC no desenvolvimento de uma aplicação Cliente/Servidor, onde se pretende ter um grupo de múltiplos servidores em load balancing que permite distribuir a carga dos múltiplos pedidos pelos múltiplos servidores com os seguintes requisitos:
• Todos os clientes e todos os servidores pertencem a um grupo de nome Q11.
• Após se juntar ao grupo, o cliente envia um pedido prévio ao grupo Q11 para lhe ser devolvido o par (IP, port) do servidor gRPC, ao qual deverá enviar o pedido com os dados que pretende processar;
• De seguida o cliente envia o pedido para o servidor gRPC 
Tendo em conta que deve existir uma distribuição de pedidos (load balancing) pelos vários servidores, descreva uma estratégia baseada num diagrama de interação, nas estruturas de dados e nas mensagens necessárias, para
que os servidores decidam entre si por consenso qual o servidor que responde ao pedido prévio do cliente com seu
par (IP, port), para que lhe possa ser submetido, via gRPC, o pedido de processamento de dados.

    ___Resposta:__ A estratégia que proponho é baseada em um algoritmo de consenso, como o Raft ou Paxos, que permite que os servidores cheguem a um acordo sobre qual servidor deve processar o próximo pedido._
    _1. Quando um cliente se junta ao grupo, ele envia um pedido para o grupo Q11 solicitando o par (IP, port) do servidor gRPC.
    2. Todos os servidores no grupo recebem este pedido. Cada servidor tem uma estrutura de dados que mantém o número de pedidos que está atualmente a processar.
    3. Cada servidor propõe a si mesmo como o próximo servidor a processar o pedido se ele tiver o menor número de pedidos em processamento. Se houver um empate, o servidor com o menor IP é escolhido.
    4. A proposta é enviada a todos os outros servidores no grupo. Cada servidor vota na proposta que escolhe o servidor com o menor número de pedidos em processamento.
    5. A proposta que recebe a maioria dos votos é escolhida. O servidor escolhido envia o seu par (IP, port) de volta ao cliente._
