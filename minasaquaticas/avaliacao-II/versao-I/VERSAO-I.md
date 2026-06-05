# VERSAO-I.md

# Avaliação II – Versão I

## Sistema de Monitoramento de Minas Aquáticas

---

# 1. Introdução

Olá professor.

Nesta primeira versão da Avaliação II desenvolvi um sistema distribuído para monitoramento de minas aquáticas.

O objetivo foi transformar a solução da Avaliação I em uma arquitetura Cliente-Servidor, mantendo as funcionalidades já implementadas e adicionando comunicação via rede.

---

# 2. Estrutura do Projeto

## Mostrar

Abrir a árvore de diretórios do projeto.

Mostrar:

```text
cliente/
servidor/
```

## Falar

"O projeto foi dividido em dois módulos.

O cliente é responsável por simular as minas aquáticas e gerar as leituras.

O servidor é responsável por receber, armazenar e processar os dados enviados pelas minas."

---

# 3. Servidor

## Mostrar

Arquivo:

```text
Servidor.java
```

Destacar:

```java
public static void main(...)
```

e

```java
iniciarServidor()
```

## Falar

"Aqui temos a inicialização do servidor utilizando Jersey e Grizzly.

O servidor disponibiliza os endpoints que serão utilizados pelas minas para envio das leituras."

Executar:

```text
Servidor.java
```

Mostrar no console:

```text
Servidor de Minas Aquaticas iniciado.
```

---

# 4. Rotas REST

## Mostrar

Arquivo:

```text
Rotas.java
```

Destacar:

```java
@Path("/minas")
```

e

```java
@POST
@Path("/{id}/{modelo}/{profundidade}/{proximidade}")
```

## Falar

"Cada leitura é enviada para o servidor através de uma requisição HTTP POST.

A rota recebe:

* identificador da mina;
* modelo;
* profundidade;
* proximidade."

Mostrar rapidamente:

```java
Mina mina = new Mina(id, modelo);

Leitura leitura =
    new Leitura(
        profundidade,
        proximidade
    );
```

---

# 5. Armazenamento Centralizado

## Mostrar

Arquivo:

```text
OperacoesImpl.java
```

Destacar:

```java
private final Map<Mina, List<Leitura>> leiturasPorMinas
```

## Falar

"Todas as leituras recebidas são armazenadas nesta estrutura.

Cada mina possui uma lista com suas respectivas leituras.

Diferentemente da Avaliação I, agora os dados ficam centralizados no servidor."

Mostrar também:

```java
gravar(...)
```

---

# 6. Execução Concorrente

## Mostrar

Arquivo:

```text
App.java
```

Destacar:

```java
Thread processo = new Thread(cliente);
processo.start();
```

## Falar

"Cada mina é executada em uma thread independente.

Dessa forma várias minas conseguem enviar leituras simultaneamente para o servidor."

---

# 7. Cliente

## Mostrar

Arquivo:

```text
ClienteImpl.java
```

Destacar:

```java
URL urlEnvio = new URL(...)
```

## Falar

"Nesta parte é realizada a comunicação HTTP com o servidor.

Cada leitura gerada pela mina é enviada através de uma requisição POST."

Mostrar também:

```java
conexao.setRequestMethod("POST");
```

---

# 8. Demonstração da Comunicação

## Mostrar

Executar:

```text
App.java
```

Mostrar o console do cliente.

Exemplo:

```text
Mina [ID=4, Modelo=8]
enviou leitura ...
```

## Falar

"Neste momento as minas estão gerando leituras e enviando os dados para o servidor."

---

# 9. Recebimento das Leituras

## Mostrar

Voltar para o console do servidor.

Exemplo:

```text
Leitura recebida ->
Mina [ID=4, Modelo=8]
Valores=>[profundidade: 15, proximidade: 38]
```

## Falar

"Podemos observar que as leituras estão chegando ao servidor.

As mensagens aparecem de forma intercalada porque as minas estão executando concorrentemente."

---

# 10. Funcionalidade D.4

## Mostrar

Arquivo:

```text
OperacoesImpl.java
```

Destacar:

```java
detectarObjetoEmTransito(...)
```

Mostrar rapidamente os loops principais.

## Falar

"Esta funcionalidade foi mantida da Avaliação I.

Ela compara as leituras de diferentes minas para identificar possíveis objetos em trânsito quando duas minas detectam proximidade crítica simultaneamente."

---

# Inserir após a seção onde é apresentado o OperacoesImpl.java

# Fila de Dados Recentes

## Mostrar

Arquivo:

```text
servidor/impl/OperacoesImpl.java
```

### Destacar

Estrutura utilizada:

```java
Map<Mina, Queue<Leitura>>
```

### Destacar também

Trecho responsável pelo controle da fila:

```java
if (leituras.size() >= LIMIAR_ROTACIONAMENTO_LEITURAS)
{
    leituras.poll();
}
```

### Falar

Para atender ao requisito de acúmulo e processamento somente dos dados mais recentes, o servidor utiliza uma fila de leituras para cada mina monitorada.

Quando o limite configurado é atingido, a leitura mais antiga é removida automaticamente da estrutura através do método poll().

Dessa forma, o sistema mantém apenas os dados mais recentes, evitando crescimento excessivo do consumo de memória e tornando o processamento mais eficiente.



# 11. Rota de Quantidade

## Mostrar

Navegador.

Abrir:

```text
http://localhost:8080/minas/quantidade
```

## Falar

"Esta rota permite verificar quantas minas foram monitoradas e quantas leituras foram recebidas pelo servidor."

Mostrar resultado.

---

# 12. Rota de Trânsito

## Mostrar

Navegador.

Abrir:

```text
http://localhost:8080/minas/transito
```

## Falar

"Esta rota executa a funcionalidade D.4 e retorna a quantidade de objetos em trânsito detectados."

Mostrar o resultado.

Mostrar também o console do servidor exibindo:

```text
Objeto em trânsito detectado...
```

---

# 13. Conclusão

## Falar

"Nesta primeira versão foi implementada uma arquitetura Cliente-Servidor para o monitoramento de minas aquáticas.

Foram utilizados:

* Threads para execução concorrente;
* Comunicação HTTP;
* Armazenamento centralizado;
* Reaproveitamento das funcionalidades da Avaliação I.

Esta implementação servirá como base para a Versão II, onde serão realizadas otimizações para reduzir o volume de dados transmitidos e melhorar a eficiência do sistema."
