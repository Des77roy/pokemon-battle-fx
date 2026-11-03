package com.pokemon.model;

import java.util.ArrayList;
import java.util.List;

public class PokemonData {

    public static List<Pokemon> getAvailablePokemons() {
        List<Pokemon> list = new ArrayList<>();

        Pokemon charizard = new Pokemon("Charizard", ElementType.FIRE, 50, 153, 104, 98, 120,
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/6.png");
        charizard.addMove(new Move("Lanzallamas", ElementType.FIRE, 90, 100, 15));
        charizard.addMove(new Move("Garra Dragón", ElementType.NORMAL, 80, 100, 15));
        charizard.addMove(new Move("Tajo Aéreo", ElementType.FLYING, 75, 95, 15));
        charizard.addMove(new Move("Llamarada", ElementType.FIRE, 110, 85, 5));
        list.add(charizard);

        Pokemon pikachu = new Pokemon("Pikachu", ElementType.ELECTRIC, 50, 110, 75, 60, 140,
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/25.png");
        pikachu.addMove(new Move("Rayo", ElementType.ELECTRIC, 90, 100, 15));
        pikachu.addMove(new Move("Ataque Rápido", ElementType.NORMAL, 40, 100, 30));
        pikachu.addMove(new Move("Trueno", ElementType.ELECTRIC, 110, 70, 10));
        list.add(pikachu);

        Pokemon blastoise = new Pokemon("Blastoise", ElementType.WATER, 50, 158, 93, 120, 98,
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/9.png");
        blastoise.addMove(new Move("Hidrobomba", ElementType.WATER, 110, 80, 5));
        blastoise.addMove(new Move("Surf", ElementType.WATER, 90, 100, 15));
        blastoise.addMove(new Move("Mordisco", ElementType.NORMAL, 60, 100, 25));
        list.add(blastoise);

        Pokemon venusaur = new Pokemon("Venusaur", ElementType.GRASS, 50, 160, 92, 98, 80,
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/3.png");
        venusaur.addMove(new Move("Rayo Solar", ElementType.GRASS, 120, 100, 10));
        venusaur.addMove(new Move("Hoja Afilada", ElementType.GRASS, 55, 95, 25));
        venusaur.addMove(new Move("Placaje", ElementType.NORMAL, 40, 100, 35));
        list.add(venusaur);

        Pokemon snorlax = new Pokemon("Snorlax", ElementType.NORMAL, 50, 230, 110, 110, 30,
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/143.png");
        snorlax.addMove(new Move("Golpe Cuerpo", ElementType.NORMAL, 85, 100, 15));
        snorlax.addMove(new Move("Hiperrayo", ElementType.NORMAL, 150, 90, 5));
        list.add(snorlax);

        return list;
    }

    public static Pokemon getEnemyPokemon() {
        Pokemon enemy = new Pokemon("Mewtwo", ElementType.NORMAL, 50, 180, 120, 90, 130,
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/150.png");
        enemy.addMove(new Move("Psíquico", ElementType.NORMAL, 90, 100, 10));
        enemy.addMove(new Move("Bola Sombra", ElementType.NORMAL, 80, 100, 15));
        enemy.addMove(new Move("Rayo", ElementType.ELECTRIC, 90, 100, 15));
        return enemy;
    }
}
