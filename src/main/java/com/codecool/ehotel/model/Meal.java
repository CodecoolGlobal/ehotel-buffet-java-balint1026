package com.codecool.ehotel.model;

import java.time.LocalDate;

public class Meal {
    private MealType type;
    private LocalDate expireTime;

    public Meal(MealType type) {
        this.type = type;
        this.expireTime = expireTime;
    }

    public MealType getType() {
        return type;
    }

    public LocalDate getExpireTime() {
        return expireTime;
    }
}
