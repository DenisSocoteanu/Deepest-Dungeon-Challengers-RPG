package it.unicam.cs.mpgc.rpg123015;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;

import javafx.scene.image.ImageView;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//import com.github.cliftonlabs.json_simple.*;

public class SaveLoadParser {

    public SaveLoadParser() {

    }

    public void writeSave(Avatar a) {
        JSONObject nSave = new JSONObject();

        nSave.put("nome", a.name);
        nSave.put("icon", a.getIconDir());
        nSave.put("attack_animation", a.getAttackAnim1().getImageDir());
        nSave.put("STR", a.getSTR());
        nSave.put("DEX", a.getDEX());
        nSave.put("maxHP", a.maxHp);
        nSave.put("HP", a.hp);
        nSave.put("level", a.lvl);
        nSave.put("exp", a.exp);
        nSave.put("exp_to_level_up", a.expToLvlUp);
        nSave.put("vittorie", a.getVittorie());
        nSave.put("isAlive", a.getIsAlive());

        try {
            FileWriter fw = new FileWriter(("..\\app\\src\\main\\resources\\saves\\"+a.name+".json"));
            fw.write(nSave.toJSONString());
            fw.close();

        } catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Error writing save fileeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        }
    }

    public Avatar loadSave(String name) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject save = (JSONObject) parser.parse(new FileReader(("..\\app\\src\\main\\resources\\saves\\"+name+".json")));

            String nome = (String) save.get("nome");
            String icon = (String) save.get("icon");
            String attAnim = (String) save.get("attack_animation");
            int STR = Math.toIntExact((long) save.get("STR"));
            int DEX = Math.toIntExact((long) save.get("DEX"));
            int maxHp = Math.toIntExact((long) save.get("maxHP"));
            int hp = Math.toIntExact((long) save.get("HP"));
            int lvl = Math.toIntExact((long) save.get("level"));
            int exp = Math.toIntExact((long) save.get("exp"));
            int expToLvlUp = Math.toIntExact((long) save.get("exp_to_level_up"));
            int vittorie = Math.toIntExact((long) save.get("vittorie"));
            boolean isAlive = (boolean) save.get("isAlive");

            Avatar a = new Avatar(nome,icon,attAnim,STR,DEX,maxHp,hp,lvl,exp,expToLvlUp,vittorie,isAlive);
            return a;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
