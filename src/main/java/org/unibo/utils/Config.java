package org.unibo.utils;

import static org.unibo.handler.FileHandler.*;

// Manage the configuration of the game
public class Config {
    private static final String CONFIG_FILE = "config.yaml";

    public Config() {
        if (!existsConfig()) {
            initConfig();
        }
    }

    private void initConfig() {
        String content = "options:\n fullscreen: false\n volume: 50\n music: true\n sound: true\n";
        writeFile(CONFIG_FILE, content);
    }

    private boolean existsConfig() {
        return existsFile(CONFIG_FILE);
    }

}