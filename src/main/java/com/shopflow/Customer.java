
package com.shopflow;

public class Customer {

    private final int id;
    private final String name;
    private final String email;
    private final String phone;

    public Customer(int id, String name, String email, String phone) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Customer ID must be positive."
            );
        }

        if (name == null || name.isBlank()
                || email == null || email.isBlank()
                || phone == null || phone.isBlank()) {
            throw new IllegalArgumentException(
                    "Customer details cannot be empty."
            );
        }

        this.id = id;
        this.name = name.trim();
        this.email = email.trim();
        this.phone = phone.trim();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "Customer ID: " + id
                + "\nName: " + name
                + "\nEmail: " + email
                + "\nPhone: " + phone;
    }
}
