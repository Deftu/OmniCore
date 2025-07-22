package dev.deftu.omnicore.client.options

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side

@GameSide(Side.CLIENT)
public object OmniChatSettings {

    @GameSide(Side.CLIENT)
    public object Capabilities {

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isChatLineSpacingSupported: Boolean
            get() {
                //#if MC >= 1.16.5
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isTextBackgroundOpacitySupported: Boolean
            get() {
                //#if MC >= 1.16.5
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isChatDelaySupported: Boolean
            get() {
                //#if MC >= 1.16.5
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isCommandSuggestionToggleSupported: Boolean
            get() {
                //#if MC >= 1.16.5
                return true
                //#else
                //$$ return false
                //#endif
            }

    }

    // TODO - Chat visibility

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val chatOpacity: Double
        get() {
            return unwrap(options.chatOpacity).toDouble()
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val chatLineSpacing: Double
        get() {
            //#if MC >= 1.16.5
            return unwrap(options.chatLineSpacing)
            //#else
            //$$ return 0.0
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val textBackgroundOpacity: Double
        get() {
            //#if MC >= 1.16.5
            return unwrap(options.textBackgroundOpacity)
            //#else
            //$$ return 0.0
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val chatScale: Double
        get() = unwrap(options.chatScale).toDouble()

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val chatWidth: Double
        get() = unwrap(options.chatWidth).toDouble()

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val chatHeightUnfocused: Double
        get() = unwrap(options.chatHeightUnfocused).toDouble()

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val chatHeightFocused: Double
        get() = unwrap(options.chatHeightFocused).toDouble()

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val chatDelay: Double
        get() {
            //#if MC >= 1.16.5
            return unwrap(options.chatDelay)
            //#else
            //$$ return 0.0
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isCommandSuggestingEnabled: Boolean
        get() {
            //#if MC >= 1.16.5
            return unwrap(options.autoSuggestions)
            //#else
            //$$ return true
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isChatColorEnabled: Boolean
        get() = unwrap(options.chatColors)

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isChatLinksEnabled: Boolean
        get() = unwrap(options.chatLinks)

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isChatLinksPromptEnabled: Boolean
        get() = unwrap(options.chatLinksPrompt)

}
