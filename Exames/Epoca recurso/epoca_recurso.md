### Exame Época Recurso 2021-2022

1. __Sobre as caracteristicas dos sistemas distribuídos:__
- O teorema CAP demonstra que num sistema distribuído, temos sempre 100% das características de consistência(C),  disponibilidade (A) e partições (P) por existência de falhas 
___Resposta:__ _
- A característica de Speedup de um sistema permite definir o desempenho em termos de capacidade de CPU.
___Resposta:__ _
- A disponibilidade de um sistema é definida pela razão, em percentagem, do tempo em que um sistema funciona(Uptime) versus o tempo em que o sistema deveria funcionar (Uptime+Downtime).
___Resposta:__ _
- A Latência de comunicação de dados em aplicações distribuídas é sempre maior que zero.
___Resposta:__ _

<br>

2. __Sobre o conceito de Tempo e Coordenação em sistemas distribuídos:__
- Os valores dos relógios físicos (hh:mm:ss) dos múltiplos computadores podem ser sincronizados usando o algoritmo Cristian a partir de um servidor central com acesso a um relógio de grande precisão.
___Resposta:__ _ 
- Em sistemas de comunicação multicast, os relógios lógicos vetoriais transportam nas mensagens valores dos relógios entre os participantes que permitem detetar na receção falta de ordem nas mensagens. 
___Resposta:__ _
- Os relógios lógicos como proposto por Leslie Lamport em 1978 permitem ordenar os eventos num sistema distribuído através de um participante coordenador que é eleito com o algoritmo Bully. 
___Resposta:__ _
- A causalidade entre eventos é fundamental na medida em que se existem duas mensagens A e B, em que B é consequência de A, então deve garantir-se que todos os participantes recebem primeiro A e depois B. 
___Resposta:__ _

<br>

3. __Relativamente à operação gRPC: rpc oper(stream Msg1) returns (Msg2):__
- O cliente pode chamar onNext() num stream para enviar N mensagens do tipo Msg1, terminando com a chamada de onCompleted(). O servidor, então responde com única mensagem do tipo Msg2. 
___Resposta:__ _
- A operação oper pode ser chamada nos clientes com um stub bloqueante ou com um stub não bloqueante. 
___Resposta:__ _
- Em protobuf a mensagem Msg2 pode ser vazia, sendo definida como: message Msg2 { }. 
___Resposta:__ _
- Num cliente, a chamada da operação oper pode ser feita pelo seguinte código:
StreamObserver<Msg2> str2= new someStreamObserver<Msg2>() {...};
StreamObserver<Msg1> str1=stubNonBlocked.oper(str2);
___Resposta:__ _

<br>

4. __Sobre o RabbitMQ:__
- Um Exchange do tipo TOPIC é mais genérico do que um do tipo DIRECT, pois tem routing keys flexíveis usando os caracteres * e # para que as filas (queues) recebam mensagens com diferentes padrões de encaminhamento. 
___Resposta:__ _
- Se uma fila (queue) tiver associação com múltiplos consumidores todos eles recebem as mesmas mensagens . 
___Resposta:__ _ 
- Quando um consumidor dá acknowledge negativo a uma mensagem o produtor da mensagem recebe automaticamente uma notificação. 
___Resposta:__ _ 
- Quando é enviada uma mensagem M para um exchange do tipo FANOUT, associado a N filas (queues) em que cada queue tem associado um único consumidor, então todos os consumidores recebem a mensagem M. 
___Resposta:__ _

<br>

5. __Sobre algoritmos de exclusão mútua, eleição e consensos:__
- Para resolver os conflitos de acesso a um recurso critico, o algoritmo de exclusão mútua Ricart&Agrawala utiliza a relação de precedência (Aconteceu-Antes) proporcionada pelos relógios lógicos de Lamport. 
___Resposta:__ _
- O algoritmo de exclusão mútua com topologia em anel, não usa relógios lógicos de Lamport mas também garante uma relação de precedência (Aconteceu-Antes). 
___Resposta:__ _
- O protocolo Two-phase commit (2PC) assume que não há falhas do coordenador ou de algum participante durantes as fases de Voting phase e de Commit phase. 
___Resposta:__ _
- No algoritmo Raft os clientes submetem sempre os pedidos a um participante eleito como líder, por um algoritmo de consenso entre todos os participantes, que garante a replicação consistente nos outros participantes. 
___Resposta:__ _

<br>

6. Considere um sistema distribuído, constituído por 3 participantes (p0, p1, p2), que utiliza comunicação por multicast fiável e um mecanismo baseado em relógios vetoriais que garante ordenação
causal de mensagens. Quando p0 envia a mensagem com o vetor [6,2,6], justifique o que acontece na atualização do vetor local nos outros participantes p1 e p2, considerando que:
    <br>
    6.1. p1 tem no seu vetor local p1=[5,3,6].
    ___Resposta:__ _ 
    <br>
    6.2. p2 tem no seu vetor local p2=[4,1,6].
    ___Resposta:__ _

<br>

7. Considere um sistema desenvolvido com o middleware RabbitMQ que usa o exchange ExGeral do tipo TOPIC para que diferentes produtores enviem mensagens. Associadas (binding) ao exchange ExGeral existem 3 queues com as routing keys (“#”, ”RK-A” e “RK-B”). Considerando o envio das mensagens m1, m2, m3 e m4 e respetivas routing keys indicadas, escreva sobre o diagrama da figura o fluxo de encaminhamento dessas mensagens, indicando claramente à frente de cada consumidor as mensagens que cada um receberá. 

    __Resposta:__ 

    ##### Cliente
    ```java
    

    ```

    ##### Servidor
    ```java
    
    ```

8. Um mau programador implementou uma aplicação servidora (web) colocando hard-coded o porto TCP/IP 8080, onde a aplicação escuta os pedidos HTTP, vindos das aplicações cliente. Apresente os passos principais para colocar múltiplos servidores web em execução, em diferentes portos TCP/IP, numa única máquina física Linux (computador host), considerando que essa máquina já tem instalado o sistema de virtualização Docker.

    __Resposta:__  

    ```linux
    docker run -d -p 8001:8000 -v /usr/app/app1:/user/app nome_imagem
    ```

9. Considere um cenário de controlo de N tanques de armazenamento de água como se indica na figura.
Cada tanque tem um dispositivo eletrónico com sensores que
controlam o nível de água no tanque e que automaticamente ligam ou desligam a respetiva bomba de enchimento, consoante o nível de água está baixo ou alto.
Assuma como pressuposto que foi desenvolvida uma biblioteca Java que permite obter do dispositivo eletrónico o nível do tanque em metros cúbicos de água, bem como o estado da bomba de enchimento (ligada/desligada), que facilita o desenvolvimento de uma aplicação cliente (T1,…,Tn) que através de um contrato Ctank envia periodicamente para um servidor (Tanks Manager) o estado de cada tanque.
O servidor Tanks Manager armazena em memória o estado de cada tanque, permitindo que múltiplas aplicações (User#1,…,User#n), através do contrato Csvc, possam estar registadas no servidor para receberem dinamicamente
as mudanças de estado (ligada/desligada) das bombas, incluindo a capacidade de água armazenada em cada
tanque.
Considerando o middleware gRPC, proponha os contratos (Ctank e Csvc) em protobuf, os pressupostos das configurações, bem como o esboço em Java das aplicações (Server, User e T), nomeadamente as classes necessárias. Em particular apresente a funcionalidade da operação do servidor que recebe as mensagens dos clientes T, atualiza o estado interno e envia a notificação para as aplicações User que estejam registadas.

```protobuf


```

##### Servidor

```java 

```

##### Cliente

```java


```


10. Considere uma empresa de nome EXR23 que realiza vendas pela internet que disponibiliza uma aplicação
onde os seus clientes podem submeter mensagens sobre reclamações genéricas relacionadas com as compras
que realizaram. Na empresa existem dezenas de funcionários que tratam em simultâneo as mensagens dos
clientes (cada mensagem por um funcionário diferente). O funcionário analisa a mensagem e de acordo com a
sua interpretação reenvia a mesma para ser tratada nos departamentos de Vendas, Faturação e Suporte, onde
posteriormente outros funcionários mais especializados tratam e resolvem as reclamações através de contacto
telefónico com os clientes. Para efeitos de registo e possíveis futuras auditorias, todas as mensagens envidas
pelos clientes são guardadas num ficheiro de Logging.
Utilizando unicamente o middleware RabbitMQ, apresente:

a) Um diagrama de arquitetura (Aplicações, tipos de Exchange, Queues, respetivos Bindings e Routing Keys necessários) de um sistema para a empresa EXR23;


##### Cliente
``` java

```

b) Apresente os troços de código Java essenciais da aplicação do funcionário que permite ao mesmo analisar os pedidos dos clientes, reclassificá-los e enviá-los para os departamentos (Vendas, Faturação e Suporte) da empresa.

##### NormalApp
```java


```

##### EspecializadoApp
```java

```

11. Considere um cenário em que se pretende desenvolver uma aplicação Cliente/Servidor, com distribuição de carga (load balancing) por múltiplos servidores, isto é, permite distribuir os
múltiplos pedidos dos clientes pelos múltiplos servidores.
Apresente os diagramas de arquitetura e respetiva descrição para as seguintes hipóteses:


a) Utilização exclusiva do middleware gRPC;

    ___Resposta:__ _

b) Utilização conjunta do middleware de comunicação por grupos (Spread Toolkit) e do middleware gRPC.

    ___Resposta:__ _
