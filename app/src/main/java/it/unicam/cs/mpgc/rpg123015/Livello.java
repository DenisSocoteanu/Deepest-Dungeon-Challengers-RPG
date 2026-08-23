package it.unicam.cs.mpgc.rpg123015;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Livello {

    private int id;
    private LevelStatus status;
    private LevelDifficulty diff;
    private ScenaLivello scena;

    private int lArena,hArena;
    private int nEnemies;
    private List<Enemy> enemies = new ArrayList<Enemy>();
    private List<Personaggio> initiativeOrder = new ArrayList<>();
    private Personaggio[][] arena;

    public Livello() {

        id = this.hashCode();

        diff = genDiff(LevelDifficulty.class);
        genArena(diff);

        arena = new Personaggio[lArena][hArena];
        scena = new ScenaLivello(id);

        Avatar hero = new Avatar("Guglielmo", scena, lArena, hArena);
        hero.ShareStats();
        scena.genArena(lArena, hArena);
        initiativeOrder.add(hero);
        initiativeOrder.addAll(enemies);

        //Eseguire la funzione spawn di tutti gli elementi in initiative, aggiungerli ad arena[][] e aggiungere un'icona a scenalivello
        for (Personaggio e : initiativeOrder) {
            SpawnEntity(e);
        }

    }

    public void GameStart() {

        System.out.println("GAME START!");
        /*initiativeOrder.getFirst().TakeAction();
        initiativeOrder.addLast(initiativeOrder.getFirst());
        initiativeOrder.removeFirst();*/

        Action azione;
        System.out.println("HAI 10 SECONDI PER MUOVERTI");
        azione = initiativeOrder.getFirst().TakeAction();
        if(azione != null)
            System.out.println(azione.getTypeOfAction().toString());
        else
            System.out.println("non hai fatto niente.");
        System.out.println("YOUR TURN ENDEEEEEEEEEEEEED!");

    }

    private void SpawnEntity(Personaggio e) {
        Random RANDOM = new Random();

        int x = 0;
        int y = 0;

        do {
            x = RANDOM.nextInt(0, lArena);
            y = RANDOM.nextInt(0, hArena);
            if (arena[x][y] == null)
            {
                e.Spawn(x, y);
                scena.SpawnEntity(e.getIcon(), e.getName(),x,y);
            }
        } while (arena[x][y] != null);

        System.out.println("Spawnato " + e.getName() + " in posizione " + x + ", " + y);
    }

    //Genera casualmente un valore dell'enum LevelDifficulty
    private static <T extends Enum<LevelDifficulty>> T genDiff(Class<T> clazz) {

        Random RANDOM = new Random();
        int x = RANDOM.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    private void genArena(LevelDifficulty diff) {
        System.out.println("Generating " + diff + " Arena..." );

        Random RANDOM = new Random();
        nEnemies = 0;

        //In base alla DIFFICOLTA' del livello, verrà generata un'arena con le adeguate prioprietà.
        switch (diff){
            case EASY:
                nEnemies = RANDOM.nextInt(1,4);
                for(int i = 0; i < nEnemies; i++){
                    enemies.add(new Enemy(diff, i));
                }

                lArena = RANDOM.nextInt(5,7);
                hArena = RANDOM.nextInt(5,7);
                break;
            case MEDIUM:
                //Generati il numero di nemici ed i nemici
                nEnemies = RANDOM.nextInt(1,9);
                for(int i = 0; i < nEnemies; i++){
                enemies.add(new Enemy(diff, i));
                }

                lArena = RANDOM.nextInt(6,9);
                hArena = RANDOM.nextInt(6,9);
                break;
            case HARD:
                //Generati il numero di nemici ed i nemici
                nEnemies = RANDOM.nextInt(1,13);
                for(int i = 0; i < nEnemies; i++){
                    enemies.add(new Enemy(diff, i));
                }

                lArena = RANDOM.nextInt(7,11);
                hArena = RANDOM.nextInt(7,11);
                break;
            case BOSS:
                //Generati il numero di nemici, 1 boss ed il resto dei nemici
                nEnemies = RANDOM.nextInt(1,9);
                enemies.add(new Enemy(diff, 0));
                for(int i = 0; i < nEnemies; i++){
                    enemies.add(new Enemy(LevelDifficulty.EASY, i));
                }

                lArena = RANDOM.nextInt(8,13);
                hArena = RANDOM.nextInt(8,13);
                break;
        }

        for (Enemy enemy : enemies) {
            System.out.println(enemy.getName() + " | HP: " + enemy.hp);
        }

    }

    public Scene getScenaLivello() {
        return scena.getScenaLivello();
    }

}
