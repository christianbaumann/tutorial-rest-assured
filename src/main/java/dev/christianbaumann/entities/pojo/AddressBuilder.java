package dev.christianbaumann.entities.pojo;

public class AddressBuilder {
    private String street = "1 Bagshot Row";
    private String postalCode = "123";
    private String city = "Hobbiton";
    private String country = "The Shire";

    public Address build() {
        return new Address(street, postalCode, city, country);
    }
}
