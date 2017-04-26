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
package io.gofannon.recalboxpatcher.patcher.model.image;

import io.gofannon.recalboxpatcher.patcher.utils.IORuntimeException;
import javafx.scene.image.Image;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.apache.commons.lang3.Validate.*;
import static io.gofannon.recalboxpatcher.patcher.utils.ImageUtils.*;

/**
 * Implementation of {@link ImageRepository} based on file system
 */
public class FileImageRepository implements ImageRepository {

    private File directory;
    private Map<String, NamedImage> images = new HashMap<>();

    public FileImageRepository(){}

    public FileImageRepository(File directory) {
        setDirectory(directory);
    }

    private void checkDirectory() {
        notNull(directory, "directory property is not set");
        isTrue( directory.exists(), "directory "+directory.getPath()+"does not exists");
        isTrue( directory.isDirectory(), "path "+directory.getPath()+"is not a directory");
    }

    public final void setDirectory(File directory) {
        this.directory = directory;
        checkDirectory();
        images.clear();
        try {
            registerNamedImagesFromDirectory();
        } catch(IOException ioEx) {
            throw new IORuntimeException(ioEx);
        }
    }

    private void registerNamedImagesFromDirectory() throws  IOException {
        for (File file : listImageFiles()) {
            registerNamedImageFromFile(file);
        }
    }

    private void registerNamedImageFromFile(File file) throws IOException {
        NamedImage namedImage = loadNamedImage(file);
        registerNamedImage(namedImage);
    }

    private NamedImage loadNamedImage(File file) throws IOException {
        Image image = loadImage(file);
        String name = file.getName();
        return new NamedImage(name, image);
    }

    private void registerNamedImage(NamedImage namedImage) {
        String name = namedImage.getName();
        images.put(name, namedImage);
    }

    private Collection<File> listImageFiles() throws IOException{
        return FileUtils.listFiles(directory, new ImageRepositoryFileFilter(), null);
    }

    @Override
    public Collection<NamedImage> getAllImages() {
        return new ArrayList<>(images.values());
    }

    @Override
    public void addImages(NamedImage... images) {
        Collection<NamedImage> namedImages = Arrays.asList(images);
        addAllImages(namedImages);
    }

    @Override
    public void addAllImages(Collection<NamedImage> images) {
        notNull(images,"images argument cannot be null");
        for( NamedImage image : images) {
            writeAndRegisterNamedImage(image);
        }
    }

    private void writeAndRegisterNamedImage(NamedImage image) {
        try {
            NamedImage clonedImage = new NamedImage(image);
            writeNamedImageToDirectory(clonedImage);
            registerNamedImage(clonedImage);
        } catch (IOException ioEx) {
            String message = "Cannot write image " + image.getName() + " to directory " + directory.getAbsolutePath();
            throw new IORuntimeException(message, ioEx);
        }
    }

    private void writeNamedImageToDirectory(NamedImage namedImage) throws IOException {
        String filename = namedImage.getName();
        File file = new File(directory, filename);
        Image image = namedImage.getImage();
        writeImage(image, file);
    }
}