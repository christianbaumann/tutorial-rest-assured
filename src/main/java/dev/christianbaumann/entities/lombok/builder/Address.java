package dev.christianbaumann.entities.lombok.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Address {

    private String street;
    private String postalCode;
    private String city;
    private String country;
}
