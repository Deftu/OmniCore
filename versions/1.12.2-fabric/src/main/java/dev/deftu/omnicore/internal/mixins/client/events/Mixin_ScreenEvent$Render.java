package dev.deftu.omnicore.internal.mixins.client.events;

//#if FABRIC
import dev.deftu.omnicore.api.OmniCore;
import dev.deftu.omnicore.api.client.events.ScreenEvent;
import dev.deftu.omnicore.api.client.render.OmniRenderingContext;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderer.class)
public class Mixin_ScreenEvent$Render {
    @Redirect(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;drawScreen(IIF)V"))
    private void omnicore$onRenderScreen(GuiScreen instance, int mouseX, int mouseY, float tickDelta) {
        OmniCore.getEventBus().post(new ScreenEvent.Render.Pre(instance, OmniRenderingContext.create(), tickDelta));
        instance.drawScreen(mouseX, mouseY, tickDelta);
        OmniCore.getEventBus().post(new ScreenEvent.Render.Post(instance, OmniRenderingContext.create(), tickDelta));
    }
}
//#endif
