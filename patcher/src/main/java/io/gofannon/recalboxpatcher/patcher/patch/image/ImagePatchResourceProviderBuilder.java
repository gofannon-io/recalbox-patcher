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

import io.gofannon.recalboxpatcher.patcher.model.image.ImageRepository;

import static org.apache.commons.lang3.Validate.*;

/**
 * A builder of {@link ImagePatchResourceProvider}
 * <p>This builder checks the parameters to insure the {@link ImagePatchResourceProvider} is valid.</p>
 */
public class ImagePatchResourceProviderBuilder {

    private ImageRepository inputRepository;
    private ImageRepository outputRepository;
    private ImageDimension outputDimension;


    public ImagePatchResourceProviderBuilder setInputRepository(ImageRepository repository ) {
        notNull(repository, "repository argument shall not be null");
        this.inputRepository = repository;
        return this;
    }

    public ImagePatchResourceProviderBuilder setOutputRepository(ImageRepository repository) {
        notNull(repository, "repository argument shall not be null");
        this.outputRepository = repository;
        return this;
    }

    public ImagePatchResourceProviderBuilder setOutputDimension(ImageDimension dimension) {
        notNull(dimension, "dimension argument shall not be null");
        this.outputDimension = new ImageDimension(outputDimension);
        return this;
    }

    public ImagePatchResourceProviderBuilder setOutputDimension(int with, int height) {
        outputDimension = new ImageDimension(with, height);
        return this;
    }

    public ImagePatchResourceProvider build() {
        notNull(inputRepository, "inputRepository not set");
        notNull(outputRepository, "outputRepository not set");
        notNull(outputDimension, "outputDimension not set");

        DefaultImagePatcherResourceProvider resourceProvider = new DefaultImagePatcherResourceProvider();
        resourceProvider.setInputRepository(inputRepository);
        resourceProvider.setOutputRepository(outputRepository);
        resourceProvider.setOutputDimension(outputDimension);

        return resourceProvider;
    }

    private static class DefaultImagePatcherResourceProvider implements ImagePatchResourceProvider {
        private ImageRepository inputRepository;
        private ImageRepository outputRepository;
        private ImageDimension outputDimension;

        public void setInputRepository(ImageRepository inputRepository) {
            this.inputRepository = inputRepository;
        }

        public void setOutputRepository(ImageRepository outputRepository) {
            this.outputRepository = outputRepository;
        }

        public void setOutputDimension(ImageDimension outputDimension) {
            this.outputDimension = outputDimension;
        }

        @Override
        public void startup() {
        }

        @Override
        public void shutdown() {
        }

        @Override
        public ImageRepository getInputRepository() {
            return inputRepository;
        }

        @Override
        public ImageRepository getOutputRepository() {
            return outputRepository;
        }

        @Override
        public ImageDimension getOutputDimension() {
            return outputDimension;
        }
    }
}
