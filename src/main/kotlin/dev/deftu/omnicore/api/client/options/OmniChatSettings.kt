package dev.deftu.omnicore.api.client.options

public object OmniChatSettings {
    public object Capabilities {
        @JvmStatic
        public val isChatLineSpacingSupported: Boolean
            get() {
                //#if MC >= 1.16.5
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        public val isTextBackgroundOpacitySupported: Boolean
            get() {
                //#if MC >= 1.16.5
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        public val isChatDelaySupported: Boolean
            get() {
                //#if MC >= 1.16.5
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
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
    public val chatOpacity: Double
        get() {
            return unwrap(options.chatOpacity).toDouble()
        }

    @JvmStatic
    public val chatLineSpacing: Double
        get() {
            //#if MC >= 1.16.5
            return unwrap(options.chatLineSpacing)
            //#else
            //$$ return 0.0
            //#endif
        }

    @JvmStatic
    public val textBackgroundOpacity: Double
        get() {
            //#if MC >= 1.16.5
            return unwrap(options.textBackgroundOpacity)
            //#else
            //$$ return 0.0
            //#endif
        }

    @JvmStatic
    public val chatScale: Double
        get() = unwrap(options.chatScale).toDouble()

    @JvmStatic
    public val chatWidth: Double
        get() = unwrap(options.chatWidth).toDouble()

    @JvmStatic
    public val chatHeightUnfocused: Double
        get() = unwrap(options.chatHeightUnfocused).toDouble()

    @JvmStatic
    public val chatHeightFocused: Double
        get() = unwrap(options.chatHeightFocused).toDouble()

    @JvmStatic
    public val chatDelay: Double
        get() {
            //#if MC >= 1.16.5
            return unwrap(options.chatDelay)
            //#else
            //$$ return 0.0
            //#endif
        }

    @JvmStatic
    public val isCommandSuggestingEnabled: Boolean
        get() {
            //#if MC >= 1.16.5
            return unwrap(options.autoSuggestions)
            //#else
            //$$ return true
            //#endif
        }

    @JvmStatic
    public val isChatColorEnabled: Boolean
        get() = unwrap(options.chatColors)

    @JvmStatic
    public val isChatLinksEnabled: Boolean
        get() = unwrap(options.chatLinks)

    @JvmStatic
    public val isChatLinksPromptEnabled: Boolean
        get() = unwrap(options.chatLinksPrompt)
}
