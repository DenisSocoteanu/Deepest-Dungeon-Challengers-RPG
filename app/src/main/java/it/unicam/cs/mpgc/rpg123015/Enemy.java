package it.unicam.cs.mpgc.rpg123015;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Enemy extends Personaggio{

    public Enemy(LevelDifficulty diff, int numero){
        name = "e-" + diff.name() + numero;

        switch(diff)
        {
            case EASY:
                setIcon("/icons/EASYEnemy_Sprite.png");
                setSTR(1);
                setDEX(1);
                maxHp = 3;
                hp = maxHp;
                break;
            case MEDIUM:
                setIcon("/icons/MEDIUMEnemy_Sprite.png");
                setSTR(1);
                setDEX(2);
                maxHp = 4;
                hp = maxHp;
                break;
            case HARD:
                setIcon("/icons/HARDEnemy_Sprite.png");
                setSTR(2);
                setDEX(2);
                maxHp = 5;
                hp = maxHp;
                break;
            case BOSS:
                setIcon("/icons/BOSSEnemy_Sprite.png");
                setSTR(3);
                setDEX(3);
                maxHp = 8;
                hp = maxHp;
                break;
        }
    }

    @Override
    public Action TakeAction()
    {
        System.out.println(name + " ha compiuto un'azione!");
        return null;
    }

}
