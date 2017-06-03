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

package io.gofannon.recalboxpatcher.patcher.model.hyperspin;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class HyperspinGameTest {

    private HyperspinGame game;

    @Before
    public void setUp() throws Exception {
        game = new HyperspinGame();
    }


    @Test
    public void getPlayerCount_shallReturn0WhenPlayersIsNull() throws Exception {
        game.setPlayers(null);
        assertThat(game.getPlayerCount()).isEqualTo(0);
    }


    @Test
    public void getPlayerCount_shallReturn0WhenPlayersIsEmpty() throws Exception {
        game.setPlayers("");
        assertThat(game.getPlayerCount()).isEqualTo(0);
    }


    @Test
    public void getPlayerCount_shallReturn0WhenPlayersContainsNoNumber() throws Exception {
        game.setPlayers("fezfzefez");
        assertThat(game.getPlayerCount()).isEqualTo(0);
    }


    @Test
    public void getPlayerCount_shallReturnZeroWhenPlayersContainsZero() throws Exception {
        game.setPlayers("0 players");
        assertThat(game.getPlayerCount()).isEqualTo(0);
    }

    @Test
    public void getPlayerCount_shallReturnAbsoluteCountWhenPlayersContainsNegativeNumber() throws Exception {
        game.setPlayers("-5 players");
        assertThat(game.getPlayerCount()).isEqualTo(5);
    }

    @Test
    public void getPlayerCount_shallReturnCountWhenPlayersContainsPositiveNumber() throws Exception {
        game.setPlayers("3 players");
        assertThat(game.getPlayerCount()).isEqualTo(3);
    }
}
