package com.gaminho.theshowapp.model.media;

public enum MediaType {
    FAVORITE_SONG("favorite_song_id", "favorite"),
    SONG_TO_PLAY("song_to_play_id", "song-to-play"),
    BEAT_TO_PLAY("beat_to_play_id", "beat");

    private final String key;
    private final String prefix;

    MediaType(final String key, final String prefix) {
        this.key = key;
        this.prefix = prefix;
    }

    public String getKey() {
        return key;
    }

    public String getPrefix() {
        return prefix;
    }
}
