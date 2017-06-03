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
package io.gofannon.recalboxpatcher.patcher.model.recalbox;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import static org.apache.commons.lang3.Validate.*;

public class RecalboxGame {

    private String uniqueName;
    private String path;
    private String name;
    private String desc;
    private String image;
    private String rating;
    private String releaseDate;
    private String developer;
    private String publisher;
    private String genre;
    private int playCount;
    private String lastPlayed;

    public RecalboxGame() {
    }

    public RecalboxGame(RecalboxGame recalboxGame) {
        notNull(recalboxGame,"recalboxGame argument shall not be null");
        this.path = recalboxGame.path;
        this.name = recalboxGame.name;
        this.desc= recalboxGame.desc;
        this.image = recalboxGame.image;
        this.rating= recalboxGame.rating;
        this.releaseDate = recalboxGame.releaseDate;
        this.developer = recalboxGame.developer;
        this.publisher = recalboxGame.publisher;
        this.genre = recalboxGame.genre;
        this.playCount = recalboxGame.playCount;
        this.lastPlayed = recalboxGame.lastPlayed;
        initUniqueName();
    }

    private void initUniqueName() {
        int index = path.lastIndexOf('/');
        uniqueName = path.substring(index+1).replace(".nes", "");
    }


    @XmlElement
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        initUniqueName();
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @XmlElement
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @XmlElement
    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @XmlElement(name = "releasedate")
    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @XmlElement
    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    @XmlElement
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @XmlElement
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


    @XmlElement(name = "playcount")
    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    @XmlElement(name = "lastplayed")
    public String getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(String lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    @XmlTransient
    public String getUniqueName() {
        return uniqueName;
    }

    @Override
    public String toString() {
        return "RecalboxGame{" +
                "uniqueName='" + uniqueName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecalboxGame that = (RecalboxGame) o;

        return uniqueName != null ? uniqueName.equals(that.uniqueName) : that.uniqueName == null;
    }

    @Override
    public int hashCode() {
        return uniqueName != null ? uniqueName.hashCode() : 0;
    }
}