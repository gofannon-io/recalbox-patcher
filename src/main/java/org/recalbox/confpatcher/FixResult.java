package org.recalbox.confpatcher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.*;

public class FixResult implements FixOperationLogger {

    private Set<String> fixedRecalGames = new HashSet<>();
    private Set<String> notFixedGames = new HashSet<>();
    private Set<String> noImageGames = new HashSet<>();

    @Override
    public void logGameFixed(String name) {
        this.fixedRecalGames.add(name);
    }

    @Override
    public void logGameNotFoundInHypersinDatabase(String name) {
        this.notFixedGames.add(name);
    }

    public List<String> getFixedRecalGame() {
        return fixedRecalGames.stream().sorted().collect(toList());
    }

    public List<String> getNotFixedGames() {
        return notFixedGames.stream().sorted().collect(toList());
    }

    public boolean hasNotFixedGame() {
        return notFixedGames.isEmpty() == false;
    }

    public int getFixedGameCount() {
        return fixedRecalGames.size();
    }

    public int getNotFixedGameCount() {
        return notFixedGames.size();
    }

    public int getTotalGameCount() {
        return fixedRecalGames.size() + notFixedGames.size();
    }

    @Override
    public void logImageNotFound(String nameFromPath) {
        noImageGames.add(nameFromPath);
    }

    public boolean hasNoImageGames() {
        return noImageGames.size() > 0;
    }

    public List<String> getNoImageGames() {
        return noImageGames.stream().sorted().collect(toList());
    }

    public int getNoImageGameCount() {
        return noImageGames.size();
    }
}