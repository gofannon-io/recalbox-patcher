package org.recalbox.confpatcher.recalbox;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="gameList")
public class RecalboxGameList {

    private List<RecalboxGame> gameList;
    
    @XmlElement(name="game")
    public List<RecalboxGame> getGameList() {
        return gameList;
    }
    
    public void setGameList(List<RecalboxGame> gameList) {
        this.gameList = gameList;
    }

}
