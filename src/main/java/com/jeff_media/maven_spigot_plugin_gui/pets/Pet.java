package com.jeff_media.maven_spigot_plugin_gui.pets;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public abstract class Pet {
    private final String name;
    private final AnimalType type;

    public void makeSound() {
        log.warn(name + " says: \"" + getSound() + "\"");
    }

    public abstract String getSound();

    public enum AnimalType {
        CAT, DOG, SNAKE, HAMSTER, FISH, MONKEY
    }
}
