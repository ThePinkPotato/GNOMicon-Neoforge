package dev.haxalotl.gnomicon;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;

@Mod(GNOMicon.MODID)
public class GNOMicon {
    public static final String MODID = "gnomicon";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void createDesktopEntry() {
        try {
            Process process = new ProcessBuilder("gnome-shell", "--version").start();
            String getGnome = new String(process.getInputStream().readAllBytes()).trim();
            process.waitFor();
            Path desktopPath = Path.of(System.getProperty("user.home") + "/.local/share/applications/" + "gnomicon-" + GNOMiconConfig.windowName + ".desktop");

            if (getGnome.toUpperCase().contains("GNOME") && Files.notExists(desktopPath)) {
                System.out.println("GNOME version: " + getGnome);

                if (Files.notExists(desktopPath)) {
                    Files.createFile(desktopPath);
                    Files.writeString(desktopPath,
                            "#!/usr/bin/env xdg-open\n\n" +
                                    "[Desktop Entry]\n" +
                                    "Type=Application\n" +
                                    "Icon=" + FMLPaths.CONFIGDIR.get() + GNOMiconConfig.iconName + "\n" +
                                    "StartupWMClass=" + GNOMiconConfig.windowName
                    );
                    LOGGER.info("Desktop file has been created");
                } else {
                    LOGGER.warn("Your computer is probably using KDE");
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("GNOME is gonna give me an aneurysm");
        }
    }


}
