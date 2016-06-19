package org.recalbox.confpatcher.recalbox;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public class RecalboxGame {

    private String path;
    private String name;
    private String desc;
    private String image;
    private String rating;
    private String releasedate;
    private String developer;
    private String publisher;
    private String genre;
    
    @XmlElement
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    @XmlElement
    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
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
    
    @XmlTransient
    public String getNameFromPath() {
        int index = path.lastIndexOf('/');
        return path.substring(index+1).replace(".nes", "");                
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((desc == null) ? 0 : desc.hashCode());
        result = prime * result + ((developer == null) ? 0 : developer.hashCode());
        result = prime * result + ((genre == null) ? 0 : genre.hashCode());
        result = prime * result + ((image == null) ? 0 : image.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + ((publisher == null) ? 0 : publisher.hashCode());
        result = prime * result + ((rating == null) ? 0 : rating.hashCode());
        result = prime * result + ((releasedate == null) ? 0 : releasedate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RecalboxGame other = (RecalboxGame) obj;
        if (desc == null) {
            if (other.desc != null)
                return false;
        } else if (!desc.equals(other.desc))
            return false;
        if (developer == null) {
            if (other.developer != null)
                return false;
        } else if (!developer.equals(other.developer))
            return false;
        if (genre == null) {
            if (other.genre != null)
                return false;
        } else if (!genre.equals(other.genre))
            return false;
        if (image == null) {
            if (other.image != null)
                return false;
        } else if (!image.equals(other.image))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        if (publisher == null) {
            if (other.publisher != null)
                return false;
        } else if (!publisher.equals(other.publisher))
            return false;
        if (rating == null) {
            if (other.rating != null)
                return false;
        } else if (!rating.equals(other.rating))
            return false;
        if (releasedate == null) {
            if (other.releasedate != null)
                return false;
        } else if (!releasedate.equals(other.releasedate))
            return false;
        return true;
    }

}