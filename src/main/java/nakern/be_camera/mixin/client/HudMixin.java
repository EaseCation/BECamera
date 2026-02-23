package nakern.be_camera.mixin.client;

import nakern.be_camera.camera.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HudMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void beCamera$onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        FadeDrawer.INSTANCE.draw(context);
    }
}
