package dev.christianbaumann.entities.pojo.builder.plain;

import dev.christianbaumann.entities.pojo.plain.Address;

public class AddressBuilder {
    private String street = "1 Bagshot Row";
    private String postalCode = "123";
    private String city = "Hobbiton";
    private String country = "The Shire";

    public Address build() {
        return new Address(street, postalCode, city, country);
    }

    public void withStreet(String street) {
        this.street = street;
    }

    public void withPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void withCity(String city) {
        this.city = city;
    }

    public void withCountry(String country) {
        this.country = country;
    }
}
