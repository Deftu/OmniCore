package dev.deftu.omnicore.internal.mixins.client.events;

//#if FABRIC
import dev.deftu.omnicore.api.OmniCore;
import dev.deftu.omnicore.api.client.events.ScreenEvent;
import dev.deftu.omnicore.api.client.render.OmniRenderingContext;
import dev.deftu.omnicore.api.client.render.stack.OmniPoseStack;
import dev.deftu.omnicore.api.client.render.stack.OmniPoseStacks;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderer.class)
public class Mixin_ScreenEvent$Render {
    @Redirect(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;drawScreen(IIF)V"))
    private void omnicore$onRenderScreen(GuiScreen instance, int mouseX, int mouseY, float tickDelta) {
        OmniPoseStack pose = OmniPoseStacks.create();
        OmniCore.getEventBus().post(new ScreenEvent.Render.Pre(instance, new OmniRenderingContext(pose), tickDelta));
        instance.drawScreen(mouseX, mouseY, tickDelta);
        OmniCore.getEventBus().post(new ScreenEvent.Render.Post(instance, new OmniRenderingContext(pose), tickDelta));
    }
}
//#endif
