package org.recalbox.confpatcher.hyperspin;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class HyperspinGame {
    
    private String name;
    private String description;

    public HyperspinGame() {
    }

    public HyperspinGame(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @XmlAttribute(name="name", required=true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name="description", required=true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
