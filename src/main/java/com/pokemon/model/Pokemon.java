package com.pokemon.model;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {
    private final String name;
    private final ElementType type;
    private final int level;
    private final int maxHp;
    private int currentHp;
    private final int attack;
    private final int defense;
    private final int speed;
    private final List<Move> moves;
    private final String spriteUrl;

    public Pokemon(String name, ElementType type, int level, int maxHp, int attack, int defense, int speed, String spriteUrl) {
        this.name = name;
        this.type = type;
        this.level = level;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.spriteUrl = spriteUrl;
        this.moves = new ArrayList<>();
    }

    public void addMove(Move move) {
        if (moves.size() < 4) {
            moves.add(move);
        }
    }

    public void takeDamage(int amount) {
        this.currentHp = Math.max(0, this.currentHp - amount);
    }

    public void heal(int amount) {
        this.currentHp = Math.min(maxHp, this.currentHp + amount);
    }

    public boolean isFainted() { return currentHp <= 0; }

    public String getName() { return name; }
    public ElementType getType() { return type; }
    public int getLevel() { return level; }
    public int getMaxHp() { return maxHp; }
    public int getCurrentHp() { return currentHp; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getSpeed() { return speed; }
    public List<Move> getMoves() { return moves; }
    public String getSpriteUrl() { return spriteUrl; }
}