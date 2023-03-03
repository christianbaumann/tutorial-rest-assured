package dev.christianbaumann.entities.lombok.allargsconstructor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Address {

    private String street;
    private String postalCode;
    private String city;
    private String country;
}
