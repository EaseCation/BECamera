package nakern.be_camera.mixin.client;

import nakern.be_camera.camera.CameraManager;
import nakern.be_camera.camera.path.CameraPathManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    void beCamera$getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Float> cir) {
        Float pathFov = CameraPathManager.INSTANCE.getFov();
        if (pathFov != null) {
            cir.setReturnValue(pathFov);
        }
    }

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    void beCamera$renderHand(float tickDelta, boolean bl, Matrix4f matrix, CallbackInfo ci) {
        if (CameraManager.INSTANCE.isCameraChanged()) {
            ci.cancel();
        }
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    void beCamera$bobView(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (CameraManager.INSTANCE.isCameraChanged()) {
            ci.cancel();
        }
    }

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    void beCamera$tiltViewWhenHurt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (CameraManager.INSTANCE.isCameraChanged()) {
            ci.cancel();
        }
    }
}
