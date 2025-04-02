package dev.deftu.omnicore.mixins.client;

//#if FABRIC && MC <= 1.12.2
//$$ import dev.deftu.omnicore.OmniCore;
//$$ import dev.deftu.omnicore.client.events.ScreenEvent;
//$$ import dev.deftu.omnicore.client.render.OmniMatrixStack;
//$$ import net.minecraft.client.gui.screen.Screen;
//$$ import net.minecraft.client.render.GameRenderer;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Redirect;
//$$
//$$ @Mixin(GameRenderer.class)
//$$ public class Mixin_GameRenderer_RenderScreenEvent {
//$$
//$$     @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(IIF)V"))
//$$     private void omnicore$onRenderScreen(Screen instance, int mouseX, int mouseY, float tickDelta) {
//$$         OmniMatrixStack matrixStack = new OmniMatrixStack();
//$$         OmniCore.getEventBus().post(new ScreenEvent.Render.Pre(instance, matrixStack, tickDelta));
//$$         instance.render(mouseX, mouseY, tickDelta);
//$$         OmniCore.getEventBus().post(new ScreenEvent.Render.Post(instance, matrixStack, tickDelta));
//$$     }
//$$
//$$ }
//#endif
