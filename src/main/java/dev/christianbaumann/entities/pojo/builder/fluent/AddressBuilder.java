package dev.christianbaumann.entities.pojo.builder.fluent;

import dev.christianbaumann.entities.pojo.plain.Address;

public class AddressBuilder {
    private String street = "1 Bagshot Row";
    private String postalCode = "123";
    private String city = "Hobbiton";
    private String country = "The Shire";

    public Address build() {
        return new Address(street, postalCode, city, country);
    }

    public AddressBuilder withStreet(String street) {
        this.street = street;
        return this;
    }

    public AddressBuilder withPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public AddressBuilder withCity(String city) {
        this.city = city;
        return this;
    }

    public AddressBuilder withCountry(String country) {
        this.country = country;
        return this;
    }
}
