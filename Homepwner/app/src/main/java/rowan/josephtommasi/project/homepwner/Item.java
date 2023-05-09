package rowan.josephtommasi.project.homepwner;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Item {
    private String name;
    private String serialNum;
    private int value;
    private Date dateCreated;

    private String[] adjectives = {"Fluffy", "Shiny", "Blue", "Red", "Green", "Bumpy"};
    private String[] nouns = {"Bear", "Spoon", "Knife", "Fork", "Sofa", "Sock"};


    public Item(){
        serialNum = UUID.randomUUID().toString().substring(0,8).toUpperCase();
        dateCreated = new Date();
        Random rand = new Random();
        name = adjectives[rand.nextInt(adjectives.length)] + " " + nouns[rand.nextInt(nouns.length)];
        value = rand.nextInt(500);
    }


    public void setSerialNum(String serial){this.serialNum = serial;}
    public String getSerialNum() {
        return serialNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getPhotoFilename(){
        return "IMG_" + getSerialNum().toString() + ".jpg";
    }

}
