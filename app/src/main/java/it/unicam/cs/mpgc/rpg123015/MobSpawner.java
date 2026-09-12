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

    public List<Enemy> SpawnEnemies(LevelDifficulty stageDiff, int diffMod)
    {
        int nE = 0, nM = 0, nH = 0;
        List<Enemy> enemyList = new ArrayList<>();

        int enemyValue = diffMod;
        switch (stageDiff) {
            case EASY:
                enemyValue += 12;
                break;
            case MEDIUM:
                enemyValue += 27;
                break;
            case HARD:
                enemyValue += 42;
                break;
            case BOSS:
                enemyValue += 15;
                break;

        }

        while(enemyValue >= 4)
        {
            LevelDifficulty enemyDiff = genDiff(LevelDifficulty.class);
            switch(enemyDiff)
            {
                case EASY:
                {
                    enemyList.add(new Enemy(enemyDiff, diffMod, nE));
                    nE++;
                    enemyValue-=4;
                    break;
                }
                case MEDIUM:
                {
                    enemyList.add(new Enemy(enemyDiff, diffMod, nM));
                    nM++;
                    enemyValue-=9;
                    break;
                }
                case HARD:
                {
                    enemyList.add(new Enemy(enemyDiff, diffMod, nH));
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
