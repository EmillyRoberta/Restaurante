package br.com.fiap.restaurante.restaurante.controllers.types;

import org.springframework.http.HttpStatus;

/**
 * Interface para que se possa utilizar constantes em annotations que exigem uma
 * String constante em tempo de compilaçao. Não é possível passar diretamente um {@link Enum}, como {@link HttpStatus},
 * para o parâmetro {@code responseCode}, pois as anotações Java não suportam valores dinâmicos em Runtime e
 * nem conversões de {@link Enum} para {@link String} na definição do atributo.
 */
public interface HttpStatusCode {
    String OK = "200";
    String CREATED = "201";
    String NO_CONTENT = "204";
    String BAD_REQUEST = "400";
    String UNAUTHORIZED = "401";
    String NOT_FOUND = "404";
    String UNPROCESSABLE_CONTENT = "422";
    String INTERNAL_SERVER_ERROR = "500";
}
