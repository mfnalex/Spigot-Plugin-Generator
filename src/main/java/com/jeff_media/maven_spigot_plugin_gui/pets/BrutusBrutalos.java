package com.jeff_media.maven_spigot_plugin_gui.pets;

public class BrutusBrutalos extends Pet {
    public BrutusBrutalos() {
        super("Brutus Brutalos", Pet.AnimalType.DOG);
    }

    @Override
    public String getSound() {
        return "Woof woof";
    }
}
