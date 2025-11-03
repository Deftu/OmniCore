package dev.deftu.omnicore.internal.mixins;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CommandSourceStack.class)
public interface Mixin_AccessCommandSource {
    @Accessor CommandSource getSource();
}
