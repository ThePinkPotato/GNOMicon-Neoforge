package dev.haxalotl.gnomicon;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(GNOMicon.MODID)
public class GNOMicon {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "gnomicon";
    // Directly reference a slf4j logger
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

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public GNOMicon(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());



        }
    }


}
