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
package io.gofannon.recalboxpatcher.patcher.patch.database;

import io.gofannon.recalboxpatcher.patcher.TestResourceHelper;
import io.gofannon.recalboxpatcher.patcher.model.image.NamedImage;
import javafx.scene.image.Image;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.assertj.core.api.Assertions.*;

public class NamedImageTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    NamedImage namedImage;
    Image image;
    Image image2;


    @Before
    public void setUp() throws Exception {
        image = TestResourceHelper.getImage("/image1.png");
        image2 = TestResourceHelper.getImage("/image2.png");

        namedImage = new NamedImage("image.png", image);
    }


    @Test(expected = NullPointerException.class)
    public void namedImage_shallNotAcceptNullName() throws Exception {
        new NamedImage(null, image);
    }

    @Test(expected = IllegalArgumentException.class)
    public void namedImage_shallNotAcceptEmptyName() throws Exception {
        new NamedImage("", image);
    }

    @Test(expected = NullPointerException.class)
    public void namedImage_shallNotAcceptNullImage() throws Exception {
        new NamedImage("image.png", null);
    }

    @Test
    public void namedImage_shallAcceptNotEmptyNameAndNotNullImage() throws Exception {
        namedImage = new NamedImage("image.png", image);

        assertThat(namedImage)
                .extracting("name", "image")
                .contains("image.png", image);
    }

    @Test(expected = NullPointerException.class)
    public void setName_shallNotAcceptNullName() throws Exception {
        namedImage.setName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setName_shallNotAcceptEmptyName() throws Exception {
        namedImage.setName("");
    }

    @Test
    public void setName_shallAcceptFilledName() throws Exception {
        assertThat(namedImage)
                .extracting("name")
                .contains("image.png");

        namedImage.setName("image2.png");

        assertThat(namedImage)
                .extracting("name")
                .contains("image2.png");
    }

    @Test(expected = NullPointerException.class)
    public void setImage_shallNotAcceptNullImage() throws Exception {
        namedImage.setImage(null);
    }

    @Test
    public void setImage_shallAcceptNotNullImage() throws Exception {
        assertThat(namedImage)
                .extracting("image")
                .contains(image);

        namedImage.setImage(image2);

        assertThat(namedImage)
                .extracting("image")
                .contains(image2);
    }
}