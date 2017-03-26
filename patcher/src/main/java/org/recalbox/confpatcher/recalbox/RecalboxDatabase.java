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
