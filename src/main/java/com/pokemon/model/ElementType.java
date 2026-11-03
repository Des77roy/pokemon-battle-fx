package com.pokemon.model;

public enum ElementType {
    FIRE("Fuego", "#FF421D"),
    WATER("Agua", "#2980B9"),
    GRASS("Planta", "#27AE60"),
    ELECTRIC("Eléctrico", "#F1C40F"),
    NORMAL("Normal", "#95A5A6"),
    FLYING("Volador", "#A890F0");

    private final String name;
    private final String colorHex;

    ElementType(String name, String colorHex) {
        this.name = name;
        this.colorHex = colorHex;
    }

    public String getName() { return name; }
    public String getColorHex() { return colorHex; }

    public double getMultiplierAgainst(ElementType targetType) {
        if (this == FIRE && targetType == GRASS) return 2.0;
        if (this == FIRE && targetType == WATER) return 0.5;
        if (this == WATER && targetType == FIRE) return 2.0;
        if (this == WATER && targetType == GRASS) return 0.5;
        if (this == GRASS && targetType == WATER) return 2.0;
        if (this == GRASS && targetType == FIRE) return 0.5;
        if (this == ELECTRIC && targetType == WATER) return 2.0;
        if (this == ELECTRIC && targetType == GRASS) return 0.5;
        return 1.0;
    }
}