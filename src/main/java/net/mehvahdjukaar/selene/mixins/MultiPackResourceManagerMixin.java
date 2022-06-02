package net.mehvahdjukaar.selene.mixins;

import net.mehvahdjukaar.selene.resourcepack.EarlyPackReloadEvent;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MultiPackResourceManager.class)
public abstract class MultiPackResourceManagerMixin implements CloseableResourceManager {

    //should fire right before add reload listener, before packs are reloaded and listeners called
    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void dynamicPackEarlyReload(PackType type, List<PackResources> packs, CallbackInfo cir) {
        //fires on world load or on /reload
        if (type == PackType.SERVER_DATA) {
            //reload dynamic packs before reloading data packs
            MinecraftForge.EVENT_BUS.post(new EarlyPackReloadEvent(packs, this));
            //ServerEarlyResourceManager.loadResources(packs, this);
        }
    }
}
