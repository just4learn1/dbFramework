package com.mzc.java8InAction.lambda.instance;

public class Dish {
    private final int calories;
    private final String name;
    private final boolean vegetarian;
    private final Type type;

    public Dish(int calories, String name, boolean vegetarian, Type type) {
        this.calories = calories;
        this.name = name;
        this.vegetarian = vegetarian;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "calories=" + calories +
                ", name='" + name + '\'' +
                ", vegetarian=" + vegetarian +
                ", type=" + type +
                '}';
    }

    public int getCalories() {
        return calories;
    }

    public String getName() {
        return name;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public Type getType() {
        return type;
    }
}
