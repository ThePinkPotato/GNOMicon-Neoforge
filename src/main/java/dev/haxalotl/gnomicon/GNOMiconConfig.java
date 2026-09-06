package dev.haxalotl.gnomicon;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Files;
import java.nio.file.Path;

public class GNOMiconConfig {

    public static String configString;
    public static String windowName;
    public static String iconName;
    public static Boolean generateDesktopEntry;

    private static final Path configPath = Path.of(FMLPaths.CONFIGDIR.get() + "/gnomicon");
    private static final Path jsonPath = Path.of(configPath + "/gnomicon.json");
    private static final Path iconPath = Path.of(configPath + "/icon.png");

    public static void createConfig() {
        try {
            if (Files.notExists(configPath)) {
                Files.createDirectory(configPath);
            }

            if (Files.notExists(jsonPath)) {
                Files.createFile(jsonPath);
                Files.writeString(jsonPath,
                        "{\n" +
                                "\"name\":\"Minecraft\",\n".indent(4) +
                                "\"icon\":\"icon.png\",\n".indent(4) +
                                "\"generateDesktopEntry\": true".indent(4) +
                                "}"
                );
            }

            if (Files.notExists(iconPath)) {
                Files.copy(GNOMicon.class.getResourceAsStream("/assets/gnomicon/appicon.png"), iconPath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readConfig() {
        try {
            configString = String.join(" ", Files.readAllLines(jsonPath));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObject gnomiconConfig = JsonParser.parseString(configString).getAsJsonObject();
        windowName = gnomiconConfig.get("name").getAsString();
        iconName = gnomiconConfig.get("icon").getAsString();
        generateDesktopEntry = gnomiconConfig.get("generateDesktopEntry").getAsBoolean();
    }

}
