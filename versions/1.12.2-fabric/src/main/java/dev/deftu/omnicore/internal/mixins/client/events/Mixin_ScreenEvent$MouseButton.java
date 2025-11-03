package dev.deftu.omnicore.internal.mixins.client.events;

//#if FABRIC
import dev.deftu.omnicore.api.OmniCore;
import dev.deftu.omnicore.api.client.events.ScreenEvent;
import dev.deftu.omnicore.api.client.input.OmniMouseButtons;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiScreen.class)
public abstract class Mixin_ScreenEvent$MouseButton {
    @Shadow protected abstract void mouseClicked(int mouseX, int mouseY, int button);
    @Shadow protected abstract void mouseReleased(int mouseX, int mouseY, int button);

    @Redirect(method = "handleMouseInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;mouseClicked(III)V"))
    private void omnicore$onMouseClick(GuiScreen instance, int mouseX, int mouseY, int button) {
        ScreenEvent.MouseClick.Pre event = new ScreenEvent.MouseClick.Pre(instance, OmniMouseButtons.from(button), mouseX, mouseY);
        OmniCore.getEventBus().post(event);
        if (!event.isCancelled()) {
            mouseClicked(mouseX, mouseY, button);
            OmniCore.getEventBus().post(new ScreenEvent.MouseClick.Post(instance, OmniMouseButtons.from(button), mouseX, mouseY));
        }
    }

    @Redirect(method = "handleMouseInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;mouseReleased(III)V"))
    private void omnicore$onMouseRelease(GuiScreen instance, int mouseX, int mouseY, int button) {
        ScreenEvent.MouseRelease.Pre event = new ScreenEvent.MouseRelease.Pre(instance, OmniMouseButtons.from(button), mouseX, mouseY);
        OmniCore.getEventBus().post(event);
        if (!event.isCancelled()) {
            mouseReleased(mouseX, mouseY, button);
            OmniCore.getEventBus().post(new ScreenEvent.MouseRelease.Post(instance, OmniMouseButtons.from(button), mouseX, mouseY));
        }
    }
}
//#endif
