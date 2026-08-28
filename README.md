# Restaurante
Projeto de restaurante da Pós Fiap

## Execução

1. Clonar o projeto do GitHub:
```
git clone https://github.com/EmillyRoberta/Restaurante restaurante
```

2. Navegar até a raiz da pasta do projeto:
```
cd restaurante
```

3. Preencher os valores no arquivo .env
> [!NOTE]
> Renomeie o arquivo `.env.example` (com valores do ambiente de develop) para `.env`.

```
mv docker/.env.example docker/.env
```

4. Subir o DB e a aplicação com o Docker Compose:
```
docker compose -f docker/docker-compose.yml up
```
> [!NOTE]
> Para executar apenas a aplicação (sem o DB), execute os comandos abaixo na pasta do Dockerfile:
> ```
> docker build --no-cache --progress=plain -t restaurante . -f Dockerfile
> docker run --rm -e SPRING_PROFILES_ACTIVE=docker -it restaurante
> ```

## Postman
As requisições estão em [Postman Request Collection](postman/postman-request_collection.json) 
(`postman/postman-request_collection.json`).</br>
Há requisições para CRUD e validações das entidades do projeto que funcionam de forma independente. 
Apenas é necessário executá-las na ordem dentro da mesma pasta, que estão organizadas por cenários.

### Configurando as variáveis globais
Há duas variáveis globais, a `host` e a `port`, popule-as para configurar a URL corretamente.
A porta padrão configurada na aplicação é a `8080`.

## Documentação da API (Swagger)
Acesse o path "my-docs" para visualizar a documentação da API. 
```
Exemplo: http://localhost:8080/my-docs
```

