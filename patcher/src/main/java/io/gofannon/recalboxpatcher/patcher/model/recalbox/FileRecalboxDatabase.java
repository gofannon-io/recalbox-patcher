/*
 * Copyright 2017  the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.gofannon.recalboxpatcher.patcher.model.recalbox;

import io.gofannon.recalboxpatcher.patcher.utils.IORuntimeException;

import javax.xml.bind.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.Validate.*;

/**
 * Implementation of a {@link RecalboxDatabase} based on file
 */
public class FileRecalboxDatabase implements RecalboxDatabase {

    private RecalboxGameList gameList = new RecalboxGameList();


    public FileRecalboxDatabase() {
    }

    public FileRecalboxDatabase(RecalboxDatabase database) {
        notNull(database,"database argument shall not be null");

        List<RecalboxGame> clonedList = new ArrayList<>(database.getAllGames());
        gameList = new RecalboxGameList(clonedList);
    }


    public void loadFromFile(File file) throws IOException {
        notNull(file, "file argument shall not be null");
        try {
            gameList = JAXB.unmarshal(file, RecalboxGameList.class);
        } catch (DataBindingException ex) {
            throw new IORuntimeException("Cannot read file "+file.getAbsolutePath(),ex);
        }
    }

    public void loadFromStream(InputStream stream) throws IOException {
        notNull(stream, "stream argument shall not be null");
        gameList = JAXB.unmarshal(stream, RecalboxGameList.class);
    }

    public void saveToFile(File file) throws IOException, JAXBException {
        try( OutputStream stream = new FileOutputStream(file)) {
            saveToStream(stream);
        }
    }

    public void saveToStream(OutputStream stream) throws IOException, JAXBException {
        JAXBContext jc = JAXBContext.newInstance(RecalboxGameList.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        try (Writer writer = new OutputStreamWriter(stream)) {
            marshaller.marshal(gameList, writer);
        }
    }

    @Override
    public List<String> getGameUniqueNames() {
        return gameList.getGameList().stream()
                .map(g -> g.getUniqueName())
                .sorted()
                .collect(toList());
    }

    @Override
    public void addGame(RecalboxGame game) throws NullPointerException {
        gameList.add(game);
    }

    @Override
    public List<RecalboxGame> getAllGames() {
        return gameList.getGameList();
    }

    @Override
    public RecalboxGame findByUniqueName(String uniqueName) {
        Optional<RecalboxGame> foundGame = gameList.getGameList().stream()
                .filter( g -> g.getUniqueName().equals(uniqueName) )
                .findFirst();
        return foundGame.orElse(null);
    }
}
