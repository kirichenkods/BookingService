package ru.sber.bookingservice.model.enums;

public enum DurationType {
    MINUTES("MINUTES"),
    HOURS("HOURS"),
    DAYS("DAYS");

    private String value;

    DurationType(String value) {
        this.value = value;
    }

    public String getDisplayValue() {
        return this.value;
    }
}
