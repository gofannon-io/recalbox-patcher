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
package io.gofannon.recalboxpatcher.patcher.patch.image;

import io.gofannon.recalboxpatcher.patcher.model.image.FileImageRepository;
import io.gofannon.recalboxpatcher.patcher.model.image.NamedImage;
import javafx.scene.image.Image;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

import static io.gofannon.recalboxpatcher.patcher.TestResourceHelper.*;

public class FileImageRepositoryTest {

    FileImageRepository repository;

    @Rule
    public TemporaryFolder rootFolder = new TemporaryFolder();


    @Before
    public void setUp() throws Exception {
        repository = new FileImageRepository();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = IllegalArgumentException.class)
    public void setDirectory_shallNotAcceptFileArgument() throws Exception {
        File file = rootFolder.newFile();
        repository.setDirectory(file);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setDirectory_shallNotAcceptNotExistingDirectory() throws Exception {
        File notExistingDirectory = new File(rootFolder.getRoot(), "/sample/");
        repository.setDirectory(notExistingDirectory);
    }

    @Test(expected = NullPointerException.class)
    public void setDirectory_shallNotAcceptNullArgument() throws Exception {
        repository.setDirectory(null);
    }

    @Test
    public void setDirectory_shallAcceptEmptyDirectoryl() throws Exception {
        File existingDirectory = rootFolder.newFolder();
        repository.setDirectory(existingDirectory);
    }

    @Test
    public void setDirectory_shallAcceptDirectoryWithImageFiles() throws Exception {
        File directory = createDirectoryWithFiles("/image1.png", "/image2.png");

        repository.setDirectory(directory);
    }

    private File createDirectoryWithFiles(String... resourceNames ) throws IOException {
        File directory = rootFolder.newFolder();
        for(String resourceName : resourceNames ) {
            addResourceToDirectory(resourceName, directory);
        }
        return directory;
    }

    @Test
    public void setDirectory_shallAcceptDirectoryWithNoImageFiles() throws Exception {
        File directory = createDirectoryWithFiles("/style.css", "/text-file.txt");

        repository.setDirectory(directory);
    }


    @Test
    public void setDirectory_shallAcceptDirectoryWithMixImageAndNoImageFiles() throws Exception {
        File directory = createDirectoryWithFiles("/style.css",
                "/text-file.txt", "/image1.png", "/image2.png");

        repository.setDirectory(directory);
    }


    @Test
    public void getAllImages_shallReturnEmptyListWhenNoFiles() throws Exception {
        File existingDirectory = rootFolder.newFolder();
        repository.setDirectory(existingDirectory);
        assertThat(repository.getAllImages()).isEmpty();
    }

    @Test
    public void getAllImages_shallReturnEmptyListWhenNoImageFiles() throws Exception {
        File directory = createDirectoryWithFiles("/style.css", "/text-file.txt");

        repository.setDirectory(directory);
        assertThat(repository.getAllImages()).isEmpty();
    }

    @Test
    public void getAllImages_shallReturnAllImagesInDirectoryWhenOnlyImageFiles() throws Exception {
        File directory = createDirectoryWithFiles("/image1.png", "/image2.png");
        repository.setDirectory(directory);

        assertThat(repository.getAllImages()).hasSize(2);

        assertThat( new File(directory,"image1.png"))
                .exists()
                .isFile();
        assertThat( new File(directory,"image2.png"))
                .exists()
                .isFile();
    }

    @Test
    public void getAllImages_shallReturnAllImagesInDirectoryWhenMixOfImageAndNoImageFiles() throws Exception {
        File directory = createDirectoryWithFiles("/style.css",
                "/text-file.txt", "/image1.png", "/image2.png");
        repository.setDirectory(directory);

        assertThat(repository.getAllImages()).hasSize(2);
        assertThat( new File(directory,"image1.png"))
                .exists()
                .isFile();
        assertThat( new File(directory,"image2.png"))
                .exists()
                .isFile();
    }

    @Test
    public void addImages_shallSupportEmptyImageList() throws Exception {
        File directory = createDirectoryWithFiles("/style.css", "/image1.png");
        repository.setDirectory(directory);
        assertThat( repository.getAllImages()).hasSize(1);

        repository.addImages();

        assertThat( repository.getAllImages()).hasSize(1);
    }


    @Test
    public void addImages_shallAcceptSingleImage() throws Exception {
        File directory = createDirectoryWithFiles("/style.css", "/image1.png");
        repository.setDirectory(directory);
        assertThat( repository.getAllImages()).hasSize(1);

        NamedImage namedImage2 = createNamedImage("/image2.png");

        repository.addImages(namedImage2);

        assertThat( repository.getAllImages()).hasSize(2);

        assertThat( new File(directory,"image1.png"))
                .exists()
                .isFile();
        assertThat( new File(directory,"image2.png"))
                .exists()
                .isFile();
    }

    private NamedImage createNamedImage(String resourcePath ) throws Exception {
        String name = extractResourceFilename(resourcePath);
        Image image = getImage(resourcePath);
        return new NamedImage(name,image);
    }

    @Test
    public void addImages_shallAcceptSeveralImages() throws Exception {
        File directory = createDirectoryWithFiles("/style.css");
        repository.setDirectory(directory);
        assertThat( repository.getAllImages()).hasSize(0);

        NamedImage namedImage1 = createNamedImage("/image1.png");
        NamedImage namedImage2 = createNamedImage("/image2.png");
        repository.addImages(namedImage1, namedImage2);

        assertThat( repository.getAllImages()).hasSize(2);

        assertThat( new File(directory,"image1.png"))
                .exists()
                .isFile();
        assertThat( new File(directory,"image2.png"))
                .exists()
                .isFile();
    }

    @Test(expected = NullPointerException.class)
    public void addAllImages_shallNotAcceptNullCollection() throws Exception {
        File directory = rootFolder.newFolder();
        repository.setDirectory(directory);

        repository.addAllImages( null);
    }

    @Test
    public void addAllImages_shallAcceptEmptyCollection() throws Exception {
        File directory = rootFolder.newFolder();
        repository.setDirectory(directory);

        assertThat( repository.getAllImages()).hasSize(0);

        repository.addAllImages(Collections.emptyList());

        assertThat( repository.getAllImages()).hasSize(0);
    }

    @Test
    public void addAllImages_shallAcceptSingleton() throws Exception {
        File directory = rootFolder.newFolder();
        repository.setDirectory(directory);

        assertThat( repository.getAllImages()).hasSize(0);

        NamedImage namedImage1 = createNamedImage("/image1.png");
        repository.addImages(namedImage1);

        assertThat( repository.getAllImages()).hasSize(1);
        assertThat( new File(directory,"image1.png"))
                .exists()
                .isFile();
    }

    @Test
    public void addAllImages_shallAcceptSeveralImages() throws Exception {
        File directory = rootFolder.newFolder();
        repository.setDirectory(directory);

        assertThat( repository.getAllImages()).hasSize(0);

        NamedImage namedImage1 = createNamedImage("/image1.png");
        NamedImage namedImage2 = createNamedImage("/image2.png");
        repository.addImages(namedImage1, namedImage2);

        assertThat( repository.getAllImages()).hasSize(2);
        assertThat( new File(directory,"image1.png"))
                .exists()
                .isFile();
        assertThat( new File(directory,"image2.png"))
                .exists()
                .isFile();
    }
}