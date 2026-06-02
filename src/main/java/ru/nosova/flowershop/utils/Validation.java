package ru.nosova.flowershop.utils;

public class Validation {
    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    public static boolean isValidPhone(String phone) {
        return phone.matches("^\\+?[0-9]{10,15}$");
    }

    public static boolean isValidName(String name) {
        return name.matches("^[А-Яа-яA-Za-zЁё\\-\\s]+$");
    }
    public static boolean isValidPrice(double price) {
        return price > 0;
    }

    public static boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }

}
