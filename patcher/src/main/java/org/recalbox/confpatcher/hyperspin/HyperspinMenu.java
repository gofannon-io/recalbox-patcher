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
