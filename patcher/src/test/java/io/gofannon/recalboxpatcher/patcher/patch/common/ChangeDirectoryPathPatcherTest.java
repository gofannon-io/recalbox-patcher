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

package io.gofannon.recalboxpatcher.patcher.patch.common;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class ChangeDirectoryPathPatcherTest {

    ChangeDirectoryPathPatcher patcher;

    @Before
    public void setUp() throws Exception {
        patcher = new ChangeDirectoryPathPatcher();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = NullPointerException.class)
    public void constructor_shallNotAcceptNullArg() {
        new ChangeDirectoryPathPatcher(null);
    }

    @Test
    public void constructor_shallAcceptEmptyArg() {
        new ChangeDirectoryPathPatcher("");
    }

    @Test
    public void constructor_shallAcceptNotEmptyArg() {
        new ChangeDirectoryPathPatcher("sample");
    }

    @Test
    public void getDirectoryPath_shallReturnConstructorArgument() throws Exception {
        patcher = new ChangeDirectoryPathPatcher("/path/image/");
        assertThat(patcher.getDirectoryPath())
            .isEqualTo("/path/image/");
    }

    @Test
    public void getDirectoryPath_shallReturnSetDirectoryPathArgument() throws Exception {
        patcher.setDirectoryPath("/just/a/path/");
        assertThat(patcher.getDirectoryPath())
                .isEqualTo("/just/a/path/");
    }

    @Test
    public void getDirectoryPath_shallReturnEmptyStringByDefault() throws Exception {
        assertThat(patcher.getDirectoryPath())
                .isEqualTo("");
    }

    @Test(expected = NullPointerException.class)
    public void setDirectoryPath_shallNotAcceptNullArg() throws Exception {
        patcher.setDirectoryPath(null);
    }

    @Test
    public void setDirectoryPath_shallAcceptEmptyArg() throws Exception {
        patcher.setDirectoryPath("");
    }

    @Test
    public void setDirectoryPath_shallAcceptNonEmptyArg() throws Exception {
        patcher.setDirectoryPath("/a/path/image/");
    }

    @Test
    public void patch_shallReturnNullWhenArgumentIsNull() throws Exception {
        assertThat(patcher.patch(null))
                .isNull();
    }

    @Test
    public void patch_shallReturnEmptyWhenArgumentIsEmpty() throws Exception {
        patcher.setDirectoryPath("/a/path/");
        assertThat(patcher.patch(""))
                .isEqualTo("");
    }

    @Test
    public void patch_nominal_endWithSlash() throws Exception {
        patcher.setDirectoryPath("/a/path/");
        assertThat(patcher.patch("/root/sample/image.png"))
                .isEqualTo("/a/path/image.png");
    }

    @Test
    public void patch_nominal_endWithNoSlash() throws Exception {
        patcher.setDirectoryPath("/a/path");
        assertThat(patcher.patch("/root/sample/image.png"))
                .isEqualTo("/a/path/image.png");
    }

}