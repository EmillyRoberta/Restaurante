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

3. Executar o build da image da aplicação:
```
docker build --no-cache --progress=plain -t restaurante . -f docker/application/Dockerfile
```
> [!NOTE]
> O parâmetro `--no-cache` reconstrói as instruções do zero sem usar nenhuma camada de cache. 
> No entanto, ele não força o download de uma nova imagem base (`FROM`) se ela já existir localmente.

4. Preencher os valores no arquivo .env
> [!NOTE]
> Renomeie o arquivo `.env.example` e preencha os valores que foram passados no relatório em PDF.

```
mv docker/.env.example docker/.env
```

5. Subir o DB e a aplicação com o Docker Compose:
```
docker compose -f docker/docker-compose.yml up
```
> [!NOTE]
> Para executar apenas a aplicação, execute o comando abaixo na pasta do Dockerfile:
> ```
> docker run --rm -e SPRING_PROFILES_ACTIVE=docker -it restaurante
> ```

## Postman
As requisições estão em [Postman Request Collection](postman/postman-request_collection.json) 
(`postman/postman-request_collection.json`).</br>
Há requisições para CRUD e validações das entidades do projeto que funcionam de forma independente. 
Apenas é necessário executá-las na ordem dentro da mesma pasta, que estão organizadas por cenários.

## Documentação da API (Swagger)
Acesse o path "my-docs" para visualizar a documentação da API. 
```
Exemplo: http://localhost:8080/my-docs
```

