package com.rob.betBot.conf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.common.base.Strings;

public class ConfigManager {

    private static Logger log = LogManager.getLogger(ConfigManager.class);
    private static final String CONFIG_PATH_PROPERTY = "configPath";
    private static final ConfigManager instance = new ConfigManager();

    private final Map<String, PropertiesListener> propertyListeners = new HashMap<>();
    private final String configPath;
    private boolean running = false;
    private Thread fileWatcherThread;

    private ConfigManager() {

        configPath = System.getProperty(CONFIG_PATH_PROPERTY);

        addListener(new TeamNameConverter());
        addListener(new LoggingManager());
        addListener(new Property());
    }

    public static ConfigManager instance() {
        return instance;
    }

    public void start() {

        load();

        if (fileWatcherThread != null)
            return;

        running = true;
        fileWatcherThread = new Thread(new PropertiesWatcher());
        fileWatcherThread.start();
    }

    public void stop() {

        if (fileWatcherThread == null)
            return;

        running = false;
        fileWatcherThread.interrupt();
    }

    private void load() {

        String configPath = System.getProperty(CONFIG_PATH_PROPERTY);
        if (Strings.isNullOrEmpty(configPath)) {
            System.out.println("CONFIG_PATH not set");
            return;
        }

        for (PropertiesListener propertiesListener : propertyListeners.values()) {
            try {
                File propertiesFile = new File(configPath, propertiesListener.getFileName());
                if (!propertiesFile.exists())
                    copyProperties(propertiesListener.getDefaultFileName(), propertiesFile);

                load(propertiesListener.getFileName());
            } catch (IOException ex) {
                System.out.println("Unable to load application properties");
                ex.printStackTrace();
            }
        }
    }

    private void load(String fileName)
        throws IOException {

        PropertiesListener propertyListener = propertyListeners.get(fileName);
        if (propertyListener == null)
            return;

        log.info("Reloading[" + fileName + "]");
        Properties properties = new Properties();
        Path path = Paths.get(configPath, fileName);
        properties.load(Files.newInputStream(path));
        propertyListener.onLoad(properties);
    }

    private void copyProperties(String source, File target) {

        try {
            InputStream propertiesStream = Property.class.getClassLoader().getResourceAsStream(source);
            Files.copy(propertiesStream, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            log.error("Couldnt copy properties[" + source + "]" , ex);
        }
    }

    private void addListener(PropertiesListener listener) {
        propertyListeners.put(listener.getFileName(), listener);
    }

    class PropertiesWatcher implements Runnable {

        @Override
        public void run() {

            try (WatchService watcher = FileSystems.getDefault().newWatchService()) {

                if (Strings.isNullOrEmpty(configPath))
                    return;

                Path p = Paths.get(configPath);
                p.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

                while (running) {
                    WatchKey watchKey = watcher.take();

                    Set<Path> filePaths = new HashSet<>();
                    for (WatchEvent<?> event : watchKey.pollEvents()) {

                        // we only register "ENTRY_MODIFY" so the context is always a Path.
                        Path propertiesPath = (Path) event.context();
                        filePaths.add(propertiesPath);
                    }

                    for (Path path : filePaths) {
                        load(path.getFileName().toString());
                    }

                    // reset the key
                    watchKey.reset();
                }

            } catch (InterruptedException ex) {
            } catch (IOException ex) {
                log.error("Error listening to config path", ex);
            }
        }
    }
}
