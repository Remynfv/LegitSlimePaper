package com.legitimoose.lsp;

import com.google.common.base.Throwables;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

// INSPIRED BY org.spigotmc.SpigotConfig
// This file uses Standard logging since this is used even before Bukkit's logger gets created.
public class MooseConfig {

    private static File CONFIG_FILE;
    private static final String HEADER = """
            This is the main config of LegitSlimePaper.\
            
            Note that for the allowed-extra-commands-in-cmd-blocks, you need to manually put command aliases for them to work.
            """;
    public static YamlConfiguration config;

    public static void init(File configFile) {
        MooseConfig.CONFIG_FILE = configFile;
        MooseConfig.config = new YamlConfiguration();
        try {
            MooseConfig.config.load(MooseConfig.CONFIG_FILE);
        } catch (IOException ex) {
        } catch (InvalidConfigurationException ex) {
            System.err.println("Could not load moose.yml, please correct your syntax errors");
            throw Throwables.propagate(ex);
        }

        MooseConfig.config.options().header(MooseConfig.HEADER);
        MooseConfig.config.options().copyDefaults(true);

        MooseConfig.readConfig(MooseConfig.class, null);
    }

    public static void readConfig(Class<?> clazz, Object instance) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) {
                if (method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE) {
                    try {
                        method.setAccessible(true);
                        method.invoke(instance);
                    } catch (InvocationTargetException ex) {
                        throw Throwables.propagate(ex.getCause());
                    } catch (Exception ex) {
                        System.err.println("Error invoking " + method);
                        ex.printStackTrace();
                    }
                }
            }
        }
        MooseConfig.save();
    }

    public static void save() {
        try {
            MooseConfig.config.save(MooseConfig.CONFIG_FILE);
        } catch (IOException ex) {
            System.err.println("Could not save moose.yml, please correct your syntax errors");
            ex.printStackTrace();
        }
    }

    private static void set(String path, Object val) {
        MooseConfig.config.set(path, val);
    }

    private static boolean getBoolean(String path, boolean def) {
        MooseConfig.config.addDefault(path, def);
        return MooseConfig.config.getBoolean(path, MooseConfig.config.getBoolean(path));
    }

    private static int getInt(String path, int def) {
        MooseConfig.config.addDefault(path, def);
        return MooseConfig.config.getInt(path, MooseConfig.config.getInt(path));
    }

    private static <T> List getList(String path, T def) {
        MooseConfig.config.addDefault(path, def);
        return (List<T>) MooseConfig.config.getList(path, MooseConfig.config.getList(path));
    }

    private static String getString(String path, String def) {
        MooseConfig.config.addDefault(path, def);
        return MooseConfig.config.getString(path, MooseConfig.config.getString(path));
    }

    private static double getDouble(String path, double def) {
        MooseConfig.config.addDefault(path, def);
        return MooseConfig.config.getDouble(path, MooseConfig.config.getDouble(path));
    }
//    ========================================================================


    public static List<String> blacklistedVanillaCommands = new ArrayList<>();
    private static void blacklistedVanillaCommands() {
        MooseConfig.blacklistedVanillaCommands = MooseConfig.getList("blacklisted-vanilla-commands-in-cmd-blocks", new ArrayList<>(List.of(
                "stop", "kick", "op", "deop", "ban", "ban-ip", "pardon", "pardon-ip", "reload", "jfr", "debug", "whitelist", "perf", "save-all", "save-on", "save-off", "setidletimeout"
        )));
    }

    public static List<String> allowedExtraCommands = new ArrayList<>();
    private static void allowedExtraCommands() {
        MooseConfig.allowedExtraCommands = MooseConfig.getList("allowed-extra-commands-in-cmd-blocks", new ArrayList<>(List.of(

        )));
    }

    public static boolean allowPlayerDataModification = false;
    private static void allowPlayerDataModification() {
        MooseConfig.allowPlayerDataModification = MooseConfig.getBoolean("allow-player-data-modification", false);
    }
}