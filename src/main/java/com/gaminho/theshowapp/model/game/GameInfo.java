package com.gaminho.theshowapp.model.game;

import javax.persistence.*;
import java.util.List;

@Entity
public class GameInfo {

    @Id
    @GeneratedValue
    private long id;
    private boolean isImprovisation;

    @ElementCollection(targetClass = String.class)
    @JoinTable(name = "games_to_avoid", joinColumns = @JoinColumn(name = "id"))
    private List<String> toAvoid;


    public GameInfo() {
    }

    public GameInfo(boolean isImprovisation, List<String> toAvoid) {
        this.isImprovisation = isImprovisation;
        this.toAvoid = toAvoid;
    }

    public boolean getImprovisation() {
        return isImprovisation;
    }

    public void setImprovisation(boolean improvisation) {
        isImprovisation = improvisation;
    }

    public List<String> getToAvoid() {
        return toAvoid;
    }

    public void setToAvoid(List<String> toAvoid) {
        this.toAvoid = toAvoid;
    }

    @Override
    public String toString() {
        return "OGameInfo{" +
                "isImprovisation=" + isImprovisation +
                ", toAvoid=" + toAvoid +
                '}';
    }
}
