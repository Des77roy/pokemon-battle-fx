package com.pokemon.model;

public class Move {
    private final String name;
    private final ElementType type;
    private final int power;
    private final int accuracy;
    private final int maxPp;
    private int currentPp;

    public Move(String name, ElementType type, int power, int accuracy, int maxPp) {
        this.name = name;
        this.type = type;
        this.power = power;
        this.accuracy = accuracy;
        this.maxPp = maxPp;
        this.currentPp = maxPp;
    }

    public String getName() { return name; }
    public ElementType getType() { return type; }
    public int getPower() { return power; }
    public int getAccuracy() { return accuracy; }
    public int getCurrentPp() { return currentPp; }
    public int getMaxPp() { return maxPp; }

    public boolean use() {
        if (currentPp > 0) {
            currentPp--;
            return true;
        }
        return false;
    }

    public void restorePp() { this.currentPp = maxPp; }
}   