package com.pibes.dnd.combat.tracker;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "character")
public class CharacterProperties {

    private Map<String, CharacterConfig> characters;

    @Setter
    @Getter
    public static class CharacterConfig {
        // Getters and Setters
        private String name;
        private int ac;
        private int initiative;
        private int health;

    }
}
