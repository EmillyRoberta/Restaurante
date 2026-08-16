package br.com.fiap.restaurante.restaurante.entities;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Address {
    private String street;
    private String number;
    private String city;
    private String state;
    private String zipCode;
}
