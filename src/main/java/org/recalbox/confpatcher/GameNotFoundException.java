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
package org.recalbox.confpatcher;

/**
 * Created by gwen on 24/12/16.
 */
public class GameNotFoundException extends RuntimeException {

    private String game;

    public GameNotFoundException(String game) {
        this.game=game;
    }

    public GameNotFoundException(String game,String message) {
        super(message);
        this.game=game;
    }

    public GameNotFoundException(String game,String message, Throwable cause) {
        super(message, cause);
        this.game=game;
    }

    public GameNotFoundException(String game,Throwable cause) {
        super(cause);
        this.game=game;
    }

    public GameNotFoundException(String game,String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.game=game;
    }

    public String getGame() {
        return game;
    }
}
