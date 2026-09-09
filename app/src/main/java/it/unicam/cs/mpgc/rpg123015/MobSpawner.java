package it.unicam.cs.mpgc.rpg123015;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MobSpawner {
    int lArena, hArena;

    public MobSpawner(int lArena, int hArena) {
        this.lArena = lArena;
        this.hArena = hArena;
    }

    public List<Enemy> SpawnEnemies(int enemyValue)
    {
        int nE = 0, nM = 0, nH = 0;
        List<Enemy> enemyList = new ArrayList<>();

        while(enemyValue >= 4)
        {
            LevelDifficulty enemyDiff = genDiff(LevelDifficulty.class);
            switch(enemyDiff)
            {
                case EASY:
                {
                    enemyList.add(new Enemy(enemyDiff, nE));
                    nE++;
                    enemyValue-=4;
                    break;
                }
                case MEDIUM:
                {
                    enemyList.add(new Enemy(enemyDiff, nM));
                    nM++;
                    enemyValue-=9;
                    break;
                }
                case HARD:
                {
                    enemyList.add(new Enemy(enemyDiff, nH));
                    nH++;
                    enemyValue-=19;
                    break;
                }
                default:
                    break;

            }
        }

        return enemyList;
    }

    private static <T extends Enum<LevelDifficulty>> T genDiff(Class<T> clazz) {

        Random RANDOM = new Random();
        int x = RANDOM.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

}
