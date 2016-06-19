package org.recalbox.confpatcher.recalbox;

import java.io.*;
import java.util.List;
import static java.util.stream.Collectors.*;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class RecalboxDatabase {

    private RecalboxGameList gameList;

    public void loadFromFile(File file) throws IOException {
        gameList = JAXB.unmarshal(file, RecalboxGameList.class);
    }

    public void saveToFile(File file) throws IOException, JAXBException {
        JAXBContext jc = JAXBContext.newInstance(RecalboxGameList.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        try (FileWriter writer = new FileWriter(file)) {
            marshaller.marshal(gameList, writer);
        }
    }

    public List<String> getGameNamesFromPath() {
        //@formatter:off
        return gameList.getGameList().stream()
                .map(g -> g.getNameFromPath())
                .sorted()
                .collect(toList());
        //@formatter:on
    }

    public List<RecalboxGame> getAllGames() {
        return gameList.getGameList();
    }

}
