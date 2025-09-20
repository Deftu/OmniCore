package dev.deftu.omnicore.internal.mixins.client.events;

//#if FABRIC
import dev.deftu.omnicore.api.OmniCore;
import dev.deftu.omnicore.api.client.events.ScreenEvent;
import dev.deftu.omnicore.api.client.render.OmniRenderingContext;
import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack;
import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStacks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public class Mixin_ScreenEvent$Render {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(IIF)V"))
    private void omnicore$onRenderScreen(Screen instance, int mouseX, int mouseY, float tickDelta) {
        OmniMatrixStack matrixStack = OmniMatrixStacks.create();
        OmniCore.getEventBus().post(new ScreenEvent.Render.Pre(instance, new OmniRenderingContext(matrixStack), tickDelta));
        instance.render(mouseX, mouseY, tickDelta);
        OmniCore.getEventBus().post(new ScreenEvent.Render.Post(instance, new OmniRenderingContext(matrixStack), tickDelta));
    }
}
//#endif
