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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class HyperspinDatabase {

    private Map<String, HyperspinGame> nameToGame = new HashMap<>();

    public void loadFromFile(File file) throws IOException {
        String rawXml = readFile(file);
        String patchedXml = patch(rawXml);
        HyperspinMenu menu = unmarshalMenu(patchedXml);
        //@formatter:off
        nameToGame = menu.getGameList().stream()
                .collect(
                    toMap(
                        HyperspinDatabase::normalizedName,
                        identity()
                         )
                        );
        //@formatter:on
    }

    private java.lang.String readFile(File file) throws IOException {
        return FileUtils.readFileToString(file, Charset.defaultCharset());
    }

    private HyperspinMenu unmarshalMenu(String patchedXml) {
        StringReader reader = new StringReader(patchedXml);
        return JAXB.unmarshal(reader, HyperspinMenu.class);
    }

    private static String patch(String rawXml) {
        return XmlPatcher.fix(rawXml);
    }
    
    private static String normalizedName(HyperspinGame game) {
        return StringEscapeUtils.unescapeXml(game.getName());
    }
    
    public HyperspinGame findByName(String name) {
        return nameToGame.get(name);
    }
    
    public List<String> getGameNames() {
        //@formatter:off
        return nameToGame.keySet()
                .stream()
                .sorted()
                .collect(toList());
        //@formatter:on
    }
}