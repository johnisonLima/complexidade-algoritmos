# VERSAO-II.md

# Avaliação II – Versão II

## Sistema de Monitoramento de Minas Aquáticas

---

# 1. Introdução

Olá professor.

Nesta segunda versão do sistema de monitoramento de minas aquáticas foram implementadas as otimizações exigidas pela Avaliação II, além da criptografia da comunicação entre cliente e servidor.

As principais melhorias realizadas foram:

* envio apenas de leituras relevantes utilizando limiares;
* deslocamento do processamento mais custoso para o cliente;
* armazenamento apenas das leituras mais recentes utilizando filas;
* geração de chaves RSA utilizando aleatoriedade real;
* criptografia dos dados transmitidos pela rede.

---

# 2. Estrutura do Projeto

## Mostrar

Abrir o explorador do projeto e apresentar:

```text
clientes/
servidor/
encriptacao/
```

### Falar

Para atender aos requisitos da avaliação foi criado um módulo específico de criptografia responsável pela geração das chaves RSA.

As chaves geradas são distribuídas entre cliente e servidor:

* chave pública no cliente;
* chave privada no servidor.

---

# 3. Módulo de Criptografia

## Mostrar

Arquivo:

```text
encriptacao/App.java
```

### Destacar

Método:

```java
main()
```

### Falar

Este módulo é responsável pela geração do par de chaves RSA.

Antes da geração é realizado um deslocamento aleatório em um vídeo utilizado como fonte de aleatoriedade real.

---

## Mostrar

Arquivo:

```text
encriptacao/aleatoriedade/GeradorDeAleatoriedadeReal.java
```

### Destacar

Métodos:

```java
nextInt()
nextLong()
getAleatoriedade()
```

### Falar

A aleatoriedade não é gerada artificialmente.

Os bytes utilizados na geração das chaves são extraídos dos quadros de um vídeo, atendendo ao requisito de utilização de uma fonte de aleatoriedade real.

---

## Mostrar

Arquivo:

```text
encriptacao/impl/GeradorDeChavesImpl.java
```

### Destacar

Método:

```java
gerarChaves()
```

### Falar

Utilizando os dados aleatórios obtidos do vídeo, é gerado um par de chaves RSA composto por:

* chave pública;
* chave privada.

---

# 4. Otimização por Limiar

## Mostrar

Arquivo:

```text
clientes/ClienteImpl.java
```

### Destacar

Método:

```java
ocorreuMovimentoSuspeito(...)
```

### Falar

Nesta versão o cliente não envia todas as leituras geradas.

Cada nova leitura é comparada com a leitura anterior.

Somente diferenças significativas de profundidade ou proximidade são enviadas ao servidor.

Isso reduz o tráfego na rede e o volume de processamento.

---

# 5. Deslocamento da Complexidade para o Cliente

## Mostrar

Arquivo:

```text
clientes/ClienteImpl.java
```

### Destacar

Método:

```java
run()
```

### Mostrar também

Trecho onde os eventos são contabilizados.

### Falar

Na versão anterior o servidor realizava a análise completa das leituras.

Nesta versão a detecção de eventos de trânsito passou a ocorrer no cliente.

Cada mina contabiliza localmente seus eventos e envia apenas o resultado final ao servidor.

Dessa forma reduzimos significativamente o custo computacional do servidor.

---

# 6. Fila de Leituras Recentes

## Mostrar

Arquivo:

```text
servidor/impl/OperacoesImpl.java
```

### Destacar

Declaração:

```java
Map<Mina, Queue<Leitura>>
```

### Destacar também

Trecho:

```java
if (leituras.size() >= LIMIAR_ROTACIONAMENTO_LEITURAS)
{
    leituras.poll();
}
```

### Falar

Para evitar crescimento indefinido da memória, o servidor mantém apenas as leituras mais recentes.

Quando o limite configurado é atingido, a leitura mais antiga é removida automaticamente.

---

# 7. Comunicação Criptografada

## Mostrar

Arquivo:

```text
clientes/ClienteImpl.java
```

### Destacar

Métodos:

```java
getChave()
encriptar()
prepararEnvio()
```

### Falar

O cliente carrega a chave pública e utiliza RSA para criptografar os dados antes do envio.

O conteúdo é convertido para JSON, criptografado e codificado em Base64 para transmissão.

---

## Mostrar

Arquivo:

```text
servidor/Rotas.java
```

### Destacar

Métodos:

```java
getChavePrivada()
desencriptar()
```

### Falar

No servidor ocorre o processo inverso.

A chave privada é utilizada para descriptografar os dados recebidos.

Após a descriptografia, o JSON é convertido novamente para objetos Java.

---

# 8. Execução do Sistema

## Mostrar

Executar primeiro:

```text
Servidor.java
```

Mostrar:

```text
Servidor de Minas Aquaticas iniciado.
```

---

## Mostrar

Executar:

```text
App.java
```

do módulo de criptografia.

Mostrar:

```text
Chaves geradas com sucesso.
```

e os arquivos:

```text
clientes/chave/ch_publica.chv
servidor/chave/ch_privada.chv
```

---

## Mostrar

Executar:

```text
clientes/App.java
```

### Falar

Neste momento cada mina gera leituras, aplica os filtros por limiar e envia apenas informações relevantes.

---

## Mostrar

Saída do cliente:

```text
Resumo da Mina
Leituras enviadas
Leituras ignoradas
Eventos detectados
```

### Falar

Podemos observar que parte das leituras foi descartada pelo mecanismo de otimização.

---

## Mostrar

Saída do servidor:

```text
Leitura recebida
Eventos recebidos
Limite de leituras atingido
```

### Falar

O servidor recebe apenas os dados relevantes e mantém somente as leituras mais recentes.

---

# 9. Demonstração das Rotas

## Mostrar no navegador

```text
http://localhost:8080/minas/quantidade
```

### Falar

Esta rota exibe a quantidade de minas monitoradas e o total de leituras armazenadas.

---

## Mostrar

```text
http://localhost:8080/minas/transito
```

### Falar

Esta rota apresenta a quantidade total de eventos de trânsito detectados pelos clientes.

---

# 10. Conclusão

Nesta segunda versão foram implementadas todas as melhorias propostas na avaliação:

* otimização por limiar;
* deslocamento da análise para o cliente;
* fila de leituras recentes;
* geração de chaves RSA;
* utilização de aleatoriedade real;
* comunicação criptografada entre cliente e servidor.

Com isso o sistema tornou-se mais eficiente, seguro e escalável em comparação à versão anterior.

Obrigado.
