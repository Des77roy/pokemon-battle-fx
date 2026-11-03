package com.pokemon.engine;

import com.pokemon.model.Move;
import com.pokemon.model.Pokemon;
import java.util.Random;

public class BattleEngine {
    private final Random random = new Random();

    public static class AttackResult {
        public boolean hit;
        public int damage;
        public double typeMultiplier;
        public boolean critical;
        public String message;

        public AttackResult(boolean hit, int damage, double typeMultiplier, boolean critical, String message) {
            this.hit = hit;
            this.damage = damage;
            this.typeMultiplier = typeMultiplier;
            this.critical = critical;
            this.message = message;
        }
    }

    public AttackResult executeMove(Pokemon attacker, Pokemon defender, Move move) {
        if (!move.use()) {
            return new AttackResult(false, 0, 1.0, false, attacker.getName() + " intentó usar " + move.getName() + " ¡pero no le quedan PP!");
        }

        int accRoll = random.nextInt(100);
        if (accRoll >= move.getAccuracy()) {
            return new AttackResult(false, 0, 1.0, false, "¡El ataque " + move.getName() + " de " + attacker.getName() + " falló!");
        }

        double typeMultiplier = move.getType().getMultiplierAgainst(defender.getType());
        boolean isCritical = random.nextInt(16) == 0;
        double critMultiplier = isCritical ? 1.5 : 1.0;
        double randomFactor = 0.85 + (random.nextDouble() * 0.15);

        double baseDamage = (((2.0 * attacker.getLevel() / 5.0 + 2.0) * move.getPower() * ((double) attacker.getAttack() / defender.getDefense())) / 50.0) + 2.0;
        int finalDamage = (int) Math.round(baseDamage * typeMultiplier * critMultiplier * randomFactor);
        finalDamage = Math.max(1, finalDamage);

        defender.takeDamage(finalDamage);

        StringBuilder sb = new StringBuilder();
        sb.append(attacker.getName()).append(" usó ").append(move.getName()).append("!\n");
        if (isCritical) sb.append("¡Un golpe crítico!\n");
        if (typeMultiplier > 1.0) sb.append("¡Es súper efectivo!\n");
        else if (typeMultiplier < 1.0 && typeMultiplier > 0) sb.append("No es muy efectivo...\n");
        sb.append("Causó ").append(finalDamage).append(" puntos de daño.");

        return new AttackResult(true, finalDamage, typeMultiplier, isCritical, sb.toString());
    }

    public String usePotion(Pokemon target) {
        int healAmount = 50;
        int oldHp = target.getCurrentHp();
        target.heal(healAmount);
        int restored = target.getCurrentHp() - oldHp;
        return "¡Usaste una Poción en " + target.getName() + "! Recuperó " + restored + " PS.";
    }
}