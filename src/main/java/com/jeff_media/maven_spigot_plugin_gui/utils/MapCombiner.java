package com.jeff_media.maven_spigot_plugin_gui.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapCombiner<K, V> {

    @SafeVarargs
    public final Map<K, V> combine(Map<K, V>... maps) {
        Map<K, V> map = new LinkedHashMap<>();
        for (Map<K, V> m : maps) {
            map.putAll(m);
        }
        return map;
    }
}
