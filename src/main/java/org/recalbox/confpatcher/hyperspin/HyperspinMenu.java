package org.recalbox.confpatcher.hyperspin;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="menu")
public class HyperspinMenu {

    private ArrayList<HyperspinGame> gameList;
    
    
    @XmlElement(name="game")
    public ArrayList<HyperspinGame> getGameList() {
        return gameList;
    }
    
    public void setGameList(ArrayList<HyperspinGame> gameList) {
        this.gameList = gameList;
    }

}
