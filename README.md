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

4. Subir o DB e a aplicação com o Docker Compose:
```
docker run --rm -e SPRING_PROFILES_ACTIVE=docker -it restaurante
```

## Documentação da API (Swagger)
Acesse o path "my-docs" para visualizar a documentação da API. 
```
Exemplo: http://localhost:8080/my-docs
```

