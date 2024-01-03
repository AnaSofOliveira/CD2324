### Exame Época Recurso 2021-2022

1. __Sobre as caracteristicas dos sistemas distribuídos:__
- O teorema CAP demonstra que num sistema distribuído, temos sempre 100% das características de consistência(C),  disponibilidade (A) e partições (P) por existência de falhas 
___Resposta:__ Falso. O teorema CAP afirma que num sistema distribuído, é impossível garantir simultaneamente 100% de Consistência (C), Disponibilidade(A) e Tolerância a Partições(P)._
- A característica de Speedup de um sistema permite definir o desempenho em termos de capacidade de CPU.
___Resposta:__ Verdadeira. Speedup é uma medida de desempenho que mostra o quanto um sistema pode ser acelerado pela adição de recursos, como CPUs._
- A disponibilidade de um sistema é definida pela razão, em percentagem, do tempo em que um sistema funciona(Uptime) versus o tempo em que o sistema deveria funcionar (Uptime+Downtime).
___Resposta:__ Verdadeiro. A disponibilidade é geralmente expressa com uma percentagem de uptime num dado periodo de tempo._
- A Latência de comunicação de dados em aplicações distribuídas é sempre maior que zero.
___Resposta:__ Verdadeiro. A latência é o tempo que leva para que uma mensagem seja transmitida de um ponto e é sempre maior que zero em sistemas reais._

<br>

2. __Sobre o conceito de Tempo e Coordenação em sistemas distribuídos:__
- Os valores dos relógios físicos (hh:mm:ss) dos múltiplos computadores podem ser sincronizados usando o algoritmo Cristian a partir de um servidor central com acesso a um relógio de grande precisão.
___Resposta:__ Verdadeiro. O algoritmo de Cristian é um método comum para a sincronização de relógios em sistemas distribuídos. _ 
- Em sistemas de comunicação multicast, os relógios lógicos vetoriais transportam nas mensagens valores dos relógios entre os participantes que permitem detetar na receção falta de ordem nas mensagens. 
___Resposta:__ Verdadeiro. Os relógios lógicos vetoriais são usados para manter a ordem dos eventos em sistemas reais._
- Os relógios lógicos como proposto por Leslie Lamport em 1978 permitem ordenar os eventos num sistema distribuído através de um participante coordenador que é eleito com o algoritmo Bully. 
___Resposta:__ Falso. Os relógios lógicos de Lamport permitem a ordenação de eventos, mas não requerem um coordenador eleito pelo algoritmo de Bully._
- A causalidade entre eventos é fundamental na medida em que se existem duas mensagens A e B, em que B é consequência de A, então deve garantir-se que todos os participantes recebem primeiro A e depois B. 
___Resposta:__ Verdadeiro. A ordem causal dos eventos é fundamental em sistemas distribuídos._

<br>

3. __Relativamente à operação gRPC: rpc oper(stream Msg1) returns (Msg2):__
- O cliente pode chamar onNext() num stream para enviar N mensagens do tipo Msg1, terminando com a chamada de onCompleted(). O servidor, então responde com única mensagem do tipo Msg2. 
___Resposta:__ Verdadeiro. Esta é uma descrição correta de como o streaming de cliente para servidor funciona._
- A operação oper pode ser chamada nos clientes com um stub bloqueante ou com um stub não bloqueante. 
___Resposta:__ Verdadeiro. O gRPC suporta tanto chamadas bloqueante como não bloqueante._
- Em protobuf a mensagem Msg2 pode ser vazia, sendo definida como: message Msg2 { }. 
___Resposta:__ Verdadeiro. No protobuf é possível definir uma mensagem vazia dessa forma._
- Num cliente, a chamada da operação oper pode ser feita pelo seguinte código:
StreamObserver<Msg2> str2= new someStreamObserver<Msg2>() {...};
StreamObserver<Msg1> str1=stubNonBlocked.oper(str2);
___Resposta:__ Verdadeiro. É um exemplo válido de uma operação gRPC em Java._

<br>

4. __Sobre o RabbitMQ:__
- Um Exchange do tipo TOPIC é mais genérico do que um do tipo DIRECT, pois tem routing keys flexíveis usando os caracteres * e # para que as filas (queues) recebam mensagens com diferentes padrões de encaminhamento. 
___Resposta:__ Verdadeiro. Os exchanges do tipo TOPIC no RabbitMQ permitem uma correspondência mais flexível das routing keys._
- Se uma fila (queue) tiver associação com múltiplos consumidores todos eles recebem as mesmas mensagens . 
___Resposta:__ Falso. No RabbitMQ, as mensagens são distribuídas de forma round-robin entre os consumidores por padrão._
- Quando um consumidor dá acknowledge negativo a uma mensagem o produtor da mensagem recebe automaticamente uma notificação. 
___Resposta:__ Falso. No RabbitMQ, um acknowledge negativo não resulta automaticamente numa notificação ao produtor._
- Quando é enviada uma mensagem M para um exchange do tipo FANOUT, associado a N filas (queues) em que cada queue tem associado um único consumidor, então todos os consumidores recebem a mensagem M. 
___Resposta:__ Verdadeiro. Um exchange do tipo FANOUT no RabbitMQ distribui mensagens para todas as queues associadas.

<br>

5. __Sobre algoritmos de exclusão mútua, eleição e consensos:__
- Para resolver os conflitos de acesso a um recurso critico, o algoritmo de exclusão mútua Ricart&Agrawala utiliza a relação de precedência (Aconteceu-Antes) proporcionada pelos relógios lógicos de Lamport. 
___Resposta:__ Verdadeiro. O algoritmo de Ricart&Agrawala utiliza a relação de precedência para resolver conflitos._
- O algoritmo de exclusão mútua com topologia em anel, não usa relógios lógicos de Lamport mas também garante uma relação de precedência (Aconteceu-Antes). 
___Resposta:__ Verdadeiro. O algoritmo de exclusão mútua com topologia em anel não necessita de relógios lógicos para garantir a precedência._
- O protocolo Two-phase commit (2PC) assume que não há falhas do coordenador ou de algum participante durantes as fases de Voting phase e de Commit phase. 
___Resposta:__ Verdadeiro. O protocolo 2PC assume que não há falhas durante as suas fases._
- No algoritmo Raft os clientes submetem sempre os pedidos a um participante eleito como líder, por um algoritmo de consenso entre todos os participantes, que garante a replicação consistente nos outros participantes. 
___Resposta:__ Verdadeiro. No algoritmo Raft, os clientes submetem pedidos ao líder, que é responsável pela replicação consistente._

<br>

6. Considere um sistema distribuído, constituído por 3 participantes (p0, p1, p2), que utiliza comunicação por multicast fiável e um mecanismo baseado em relógios vetoriais que garante ordenação causal de mensagens. Quando p0 envia a mensagem com o vetor [6,2,6], justifique o que acontece na atualização do vetor local nos outros participantes p1 e p2, considerando que:
    <br>
    6.1. p1 tem no seu vetor local p1=[5,3,6].
    ___Resposta:__ Quando o participante p0 envia uma mensagem com o vetor [6,2,6], os outros participantes (neste caso, p1 e p2) atualizam os seus vetores locais de acordo com a seguinte regra: para cada posição i no vetor, o novo valor será o máximo entre o valor atual no vetor local e o valor na mesma posição no vetor recebido.No caso do participante p1, que tem o vetor local [5,3,6], a atualização ocorrerá da seguinte forma:
    - Para a primeira posição, o máximo entre 5 (valor local) e 6 (valor recebido) é 6.
    - Para a segunda posição, o máximo entre 3 (valor local) e 2 (valor recebido) é 3.
    - Para a terceira posição, o máximo entre 6 (valor local) e 6 (valor recebido) é 6.
    Portanto, após a atualização, o vetor local do participante p1 será [6,3,6]._ 
    <br>
    6.2. p2 tem no seu vetor local p2=[4,1,6].
    ___Resposta:__ Quando o participante p0 envia uma mensagem com o vetor [6,2,6], os outros participantes (neste caso, p1 e p2) atualizam os seus vetores locais de acordo com a seguinte regra: para cada posição i no vetor, o novo valor será o máximo entre o valor atual no vetor local e o valor na mesma posição no vetor recebido. No caso do participante p2, que tem o vetor local [4,1,6], a atualização ocorrerá da seguinte forma:
    - Para a primeira posição, o máximo entre 4 (valor local) e 6 (valor recebido) é 6.
    - Para a segunda posição, o máximo entre 1 (valor local) e 2 (valor recebido) é 2.
    - Para a terceira posição, o máximo entre 6 (valor local) e 6 (valor recebido) é 6.
    Portanto, após a atualização, o vetor local do participante p2 será [6,2,6]._

<br>

7. Considere um sistema desenvolvido com o middleware RabbitMQ que usa o exchange ExGeral do tipo TOPIC para que diferentes produtores enviem mensagens. Associadas (binding) ao exchange ExGeral existem 3 queues com as routing keys (“#”, ”RK-A” e “RK-B”). Considerando o envio das mensagens m1, m2, m3 e m4 e respetivas routing keys indicadas, escreva sobre o diagrama da figura o fluxo de encaminhamento dessas mensagens, indicando claramente à frente de cada consumidor as mensagens que cada um receberá. 

    __Resposta:__ A queue com a routing key "#" receberá todas as mensagens, independentemente da sua routing key. Portanto, o consumidor desta queue receberá as mensagens m1, m2, m3 e m4. A queue com a routing key "RK-A" receberá apenas as mensagens que têm "RK-A" como routing key. Portanto, se alguma das mensagens m1, m2, m3 ou m4 tiver "RK-A" como routing key, será encaminhada para esta queue. De forma semelhante, a queue com a routing key "RK-B" receberá apenas as mensagens que têm "RK-B" como routing key. Portanto, se alguma das mensagens m1, m2, m3 ou m4 tiver "RK-B" como routing key, será encaminhada para esta queue._

8. Um mau programador implementou uma aplicação servidora (web) colocando hard-coded o porto TCP/IP 8080, onde a aplicação escuta os pedidos HTTP, vindos das aplicações cliente. Apresente os passos principais para colocar múltiplos servidores web em execução, em diferentes portos TCP/IP, numa única máquina física Linux (computador host), considerando que essa máquina já tem instalado o sistema de virtualização Docker.

    __Resposta:__  1.Criar uma imagem Docker para a aplicação web. Se a aplicação já estiver num repositório Git, você pode criar um Dockerfile no diretório raiz do projeto. O Dockerfile deve incluir instruções para copiar o código da aplicação para a imagem, instalar todas as dependências necessárias e definir o comando para iniciar a aplicação._

    ```DockerFile
        # Exemplo de Dockerfile
        FROM node:14
        WORKDIR /usr/src/app
        COPY package*.json ./
        RUN npm install
        COPY . .
        EXPOSE 8080
        CMD [ "node", "server.js" ]
    ```
    _2. Construir a imagem Docker usando o comando docker build._

    ```linux
    docker build -t minha-aplicacao-web .
    ```

    _3. Instanciar múltiplos containers Docker a partir da imagem que você acabou de construir, mapeando cada containers para um porto diferente na máquina host._

    ```linux
    docker run -d -p 8081:8080 --name minha-aplicacao-web-1 minha-aplicacao-web
    docker run -d -p 8082:8080 --name minha-aplicacao-web-2 minha-aplicacao-web
    docker run -d -p 8083:8080 --name minha-aplicacao-web-3 minha-aplicacao-web    
    ```
    


9. Considere um cenário de controlo de N tanques de armazenamento de água como se indica na figura.
Cada tanque tem um dispositivo eletrónico com sensores que controlam o nível de água no tanque e que automaticamente ligam ou desligam a respetiva bomba de enchimento, consoante o nível de água está baixo ou alto.
Assuma como pressuposto que foi desenvolvida uma biblioteca Java que permite obter do dispositivo eletrónico o nível do tanque em metros cúbicos de água, bem como o estado da bomba de enchimento (ligada/desligada), que facilita o desenvolvimento de uma aplicação cliente (T1,…,Tn) que através de um contrato Ctank envia periodicamente para um servidor (Tanks Manager) o estado de cada tanque.
O servidor Tanks Manager armazena em memória o estado de cada tanque, permitindo que múltiplas aplicações (User#1,…,User#n), através do contrato Csvc, possam estar registadas no servidor para receberem dinamicamente as mudanças de estado (ligada/desligada) das bombas, incluindo a capacidade de água armazenada em cada
tanque.
Considerando o middleware gRPC, proponha os contratos (Ctank e Csvc) em protobuf, os pressupostos das configurações, bem como o esboço em Java das aplicações (Server, User e T), nomeadamente as classes necessárias. Em particular apresente a funcionalidade da operação do servidor que recebe as mensagens dos clientes T, atualiza o estado interno e envia a notificação para as aplicações User que estejam registadas.

```protobuf
// Ctank
syntax = "proto3";

service TankService {
  rpc ReportState (TankState) returns (Empty) {}
}

message TankState {
  string id = 1;
  float level = 2;
  bool pumpState = 3;
}

message Empty {}

// Csvc
syntax = "proto3";

service UserService {
  rpc Subscribe (Empty) returns (stream TankState) {}
}
```

##### Aplicação TanksManagerServer

```java 
public class TanksManagerServer {
  private final int port;
  private final Server server;

  public TanksManagerServer(int port) throws IOException {
    this.port = port;
    this.server = ServerBuilder.forPort(port)
        .addService(new TankServiceImpl())
        .build();
  }

  public void start() throws IOException, InterruptedException {
    server.start();
    server.awaitTermination();
  }

  private class TankServiceImpl extends TankServiceGrpc.TankServiceImplBase {
    private final Map<String, TankState> tanks = new ConcurrentHashMap<>();
    private final List<StreamObserver<TankState>> observers = new CopyOnWriteArrayList<>();

    @Override
    public void reportState(TankState request, StreamObserver<Empty> responseObserver) {
      tanks.put(request.getId(), request);
      for (StreamObserver<TankState> observer : observers) {
        observer.onNext(request);
      }
      responseObserver.onNext(Empty.newBuilder().build());
      responseObserver.onCompleted();
    }

    @Override
    public void subscribe(Empty request, StreamObserver<TankState> responseObserver) {
      observers.add(responseObserver);
      for (TankState state : tanks.values()) {
        responseObserver.onNext(state);
      }
    }
  }
}
```

##### Aplicação T

```java
public class TankClient {
  private final TankServiceGrpc.TankServiceBlockingStub stub;

  public TankClient(Channel channel) {
    stub = TankServiceGrpc.newBlockingStub(channel);
  }

  public void reportState(String id, float level, boolean pumpState) {
    TankState state = TankState.newBuilder()
        .setId(id)
        .setLevel(level)
        .setPumpState(pumpState)
        .build();
    stub.reportState(state);
  }
}

```

##### Aplicação UserClient
```java
public class UserClient {
  private final UserServiceGrpc.UserServiceStub stub;

  public UserClient(Channel channel) {
    stub = UserServiceGrpc.newStub(channel);
  }

  public void subscribe() {
    stub.subscribe(Empty.newBuilder().build(), new StreamObserver<TankState>() {
      @Override
      public void onNext(TankState state) {
        System.out.println("Received state: " + state);
      }

      @Override
      public void onError(Throwable t) {
        System.out.println("Error: " + t);
      }

      @Override
      public void onCompleted() {
        System.out.println("Stream completed");
      }
    });
  }
}

```


10. Considere uma empresa de nome EXR23 que realiza vendas pela internet que disponibiliza uma aplicação onde os seus clientes podem submeter mensagens sobre reclamações genéricas relacionadas com as compras que realizaram. Na empresa existem dezenas de funcionários que tratam em simultâneo as mensagens dos clientes (cada mensagem por um funcionário diferente). O funcionário analisa a mensagem e de acordo com a sua interpretação reenvia a mesma para ser tratada nos departamentos de Vendas, Faturação e Suporte, onde posteriormente outros funcionários mais especializados tratam e resolvem as reclamações através de contacto
telefónico com os clientes. Para efeitos de registo e possíveis futuras auditorias, todas as mensagens envidas pelos clientes são guardadas num ficheiro de Logging. Utilizando unicamente o middleware RabbitMQ, apresente:

a) Um diagrama de arquitetura (Aplicações, tipos de Exchange, Queues, respetivos Bindings e Routing Keys necessários) de um sistema para a empresa EXR23.

___Resposta:__ 
__Aplicações:__ Teríamos três aplicações principais - a aplicação de clientes, a aplicação de funcionários e a aplicação de departamentos (Vendas, Faturação e Suporte).
__Exchanges:__ Teríamos dois tipos de exchanges - um exchange do tipo direct para encaminhar as mensagens dos clientes para os funcionários e um exchange do tipo topic para encaminhar as mensagens dos funcionários para os departamentos apropriados.
__Queues:__ Teríamos várias queues - uma queue para cada funcionário, uma queue para cada departamento e uma queue para o logging.
__Bindings:__ As queues dos funcionários estariam ligadas (binding) ao exchange direct e as queues dos departamentos e do logging estariam ligadas ao exchange topic.
__Routing Keys:__ As mensagens dos clientes seriam enviadas com uma routing key genérica (por exemplo, "cliente.mensagem"). As mensagens dos funcionários seriam enviadas com routing keys específicas para cada departamento (por exemplo, "departamento.vendas", "departamento.faturacao", "departamento.suporte")._


b) Apresente os troços de código Java essenciais da aplicação do funcionário que permite ao mesmo analisar os pedidos dos clientes, reclassificá-los e enviá-los para os departamentos (Vendas, Faturação e Suporte) da empresa.

##### EmplyeeApp
```java

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmployeeApp {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");

            // Aqui, você deve implementar a lógica para analisar os pedidos dos clientes.
            // Depois de analisar o pedido, você deve determinar o departamento apropriado
            // e definir a routing key de acordo.
            String routingKey = getRoutingKeyBasedOnAnalysis();
            String message = getMessageFromCustomer();

            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
        }
    }

    // Este método analisa a mensagem do cliente e determina o departamento apropriado.
    private static String getRoutingKeyBasedOnAnalysis(String message) {
        // Aqui, você deve implementar a lógica para analisar a mensagem e determinar o departamento apropriado.
        // Por exemplo, se a mensagem contém a palavra "fatura", então a routing key deve ser "departamento.faturacao".
        if (message.contains("fatura")) {
            return "departamento.faturacao";
        } else if (message.contains("venda")) {
            return "departamento.vendas";
        } else if (message.contains("suporte")) {
            return "departamento.suporte";
        } else {
            // Se a mensagem não contém nenhuma das palavras-chave, então a routing key pode ser "departamento.geral".
            return "departamento.geral";
        }
    }

    // Este método obtém a mensagem do cliente.
    // Neste exemplo, a mensagem é simplesmente lida da entrada padrão.
    private static String getMessageFromCustomer() throws IOException {
        System.out.println("Por favor, insira a sua mensagem:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }
}
```

11. Considere um cenário em que se pretende desenvolver uma aplicação Cliente/Servidor, com distribuição de carga (load balancing) por múltiplos servidores, isto é, permite distribuir os múltiplos pedidos dos clientes pelos múltiplos servidores. Apresente os diagramas de arquitetura e respetiva descrição para as seguintes hipóteses:


a) Utilização exclusiva do middleware gRPC;

___Resposta:__ 
__Cliente -> Servidor de Balanceamento de Carga:__ O cliente inicia a comunicação enviando uma solicitação de RPC para o servidor de balanceamento de carga. Esta solicitação pode incluir detalhes como o serviço que o cliente pretende invocar.
__Servidor de Balanceamento de Carga -> Cliente:__ O servidor de balanceamento de carga responde com uma lista de servidores que podem atender à solicitação do cliente. Esta lista pode ser baseada em vários fatores, como a carga atual em cada servidor, a proximidade do servidor ao cliente, etc.
__Cliente -> Servidor:__ O cliente seleciona um servidor da lista fornecida pelo servidor de balanceamento de carga e envia a solicitação de RPC para esse servidor.
__Servidor -> Cliente:__ O servidor processa a solicitação e envia a resposta de volta ao cliente._ 

b) Utilização conjunta do middleware de comunicação por grupos (Spread Toolkit) e do middleware gRPC.

___Resposta:__ __Cliente -> Servidor de Balanceamento de Carga (gRPC):__ O cliente inicia a comunicação enviando uma solicitação de RPC para o servidor de balanceamento de carga. Esta solicitação pode incluir detalhes como o serviço que o cliente pretende invocar.
__Servidor de Balanceamento de Carga -> Cliente (gRPC):__ O servidor de balanceamento de carga responde com uma lista de servidores que podem atender à solicitação do cliente. Esta lista pode ser baseada em vários fatores, como a carga atual em cada servidor, a proximidade do servidor ao cliente, etc.
__Cliente -> Grupo de Servidores (Spread):__ O cliente usa o Spread para enviar a solicitação para um grupo de servidores. O Spread garante que a mensagem seja entregue a todos os servidores no grupo, mesmo em face de falhas de rede ou de servidores.
__Servidores -> Cliente (Spread):__ Cada servidor processa a solicitação e usa o Spread para enviar a resposta de volta ao cliente. O cliente pode então processar as respostas conforme elas chegam._
