package dev.haxalotl.gnomicon.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.haxalotl.gnomicon.GNOMicon;
import dev.haxalotl.gnomicon.GNOMiconConfig;
import net.minecraft.client.Minecraft;
import net.neoforged.fml.loading.FMLConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @ModifyReturnValue(method="createTitle", at = @At("RETURN"))
    private String gnomicon$getWindowTitle(String original) {
        GNOMiconConfig.createConfig();
        GNOMiconConfig.readConfig();
        if (GNOMiconConfig.generateDesktopEntry) {
            GNOMicon.createDesktopEntry();
            FMLConfig.updateConfig(FMLConfig.ConfigValue.EARLY_WINDOW_CONTROL, false);
        }
        return GNOMiconConfig.windowName != null ? GNOMiconConfig.windowName : original ;
    }
}
