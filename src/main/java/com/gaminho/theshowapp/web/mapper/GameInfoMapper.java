package com.gaminho.theshowapp.web.mapper;

import com.gaminho.theshowapp.model.game.GameInfo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

class GameInfoMapper {

    private static Logger log = LoggerFactory.getLogger(GameInfoMapper.class);


    static GameInfo toGame(JSONObject json) {

        GameInfo game = new GameInfo();
        game.setImprovisation(json.has("improvisation")
                && json.getBoolean("improvisation"));

        JSONArray jToAvoid = json.has("games_to_avoid") ?
                json.getJSONArray("games_to_avoid")
                : new JSONArray();

        List<String> toAvoid = new ArrayList<>();
        for (Object o : jToAvoid) {
            toAvoid.add(o.toString());
        }

        game.setToAvoid(toAvoid);
        return game;
    }
}
