package dev.deftu.omnicore.api.commands.types.color

import com.mojang.brigadier.StringReader
import dev.deftu.omnicore.api.color.NamedColor
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.commands.ArgumentTypeVariant

public object NamedColorTypeArgument : ArgumentTypeVariant<OmniColor> {
    override fun parse(reader: StringReader): OmniColor? {
        return NamedColor.from(reader.readString())?.color
    }
}
