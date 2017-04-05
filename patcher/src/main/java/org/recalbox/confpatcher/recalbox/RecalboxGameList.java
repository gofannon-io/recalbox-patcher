/*
 * Copyright 2016 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.recalbox.confpatcher.recalbox;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="gameList")
public class RecalboxGameList {

    private List<RecalboxGame> gameList = new ArrayList<>();

    public RecalboxGameList() {
    }

    public RecalboxGameList(List<RecalboxGame> gameList) {
        this.gameList = gameList;
    }

    @XmlElement(name="game")
    public List<RecalboxGame> getGameList() {
        return gameList;
    }
    
    public void setGameList(List<RecalboxGame> gameList) {
        this.gameList = gameList;
    }

    public void add(RecalboxGame game) {
        if( game != null && gameList != null)
            gameList.add(game);
    }
}
