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
            //#if MC >= 1.19.2
            return unwrap(options.chatOpacity()).toDouble()
            //#else
            //$$ return unwrap(options.chatOpacity).toDouble()
            //#endif
        }

    @JvmStatic
    public val chatLineSpacing: Double
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.chatLineSpacing())
            //#elseif MC >= 1.16.5
            //$$ return unwrap(options.chatLineSpacing)
            //#else
            //$$ return 0.0
            //#endif
        }

    @JvmStatic
    public val textBackgroundOpacity: Double
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.textBackgroundOpacity())
            //#elseif MC >= 1.16.5
            //$$ return unwrap(options.textBackgroundOpacity)
            //#else
            //$$ return 0.0
            //#endif
        }

    @JvmStatic
    public val chatScale: Double
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.chatScale()).toDouble()
            //#else
            //$$ return unwrap(options.chatScale).toDouble()
            //#endif
        }

    @JvmStatic
    public val chatWidth: Double
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.chatWidth()).toDouble()
            //#else
            //$$ return unwrap(options.chatWidth).toDouble()
            //#endif
        }

    @JvmStatic
    public val chatHeightUnfocused: Double
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.chatHeightUnfocused()).toDouble()
            //#else
            //$$ return unwrap(options.chatHeightUnfocused).toDouble()
            //#endif
        }

    @JvmStatic
    public val chatHeightFocused: Double
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.chatHeightFocused()).toDouble()
            //#else
            //$$ return unwrap(options.chatHeightFocused).toDouble()
            //#endif
        }

    @JvmStatic
    public val chatDelay: Double
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.chatDelay())
            //#elseif MC >= 1.16.5
            //$$ return unwrap(options.chatDelay)
            //#else
            //$$ return 0.0
            //#endif
        }

    @JvmStatic
    public val isCommandSuggestingEnabled: Boolean
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.autoSuggestions())
            //#elseif MC >= 1.16.5
            //$$ return unwrap(options.autoSuggestions)
            //#else
            //$$ return true
            //#endif
        }

    @JvmStatic
    public val isChatColorEnabled: Boolean
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.chatColors())
            //#else
            //$$ return unwrap(options.chatColors)
            //#endif
        }

    @JvmStatic
    public val isChatLinksEnabled: Boolean
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.chatLinks())
            //#else
            //$$ return unwrap(options.chatLinks)
            //#endif
        }

    @JvmStatic
    public val isChatLinksPromptEnabled: Boolean
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.chatLinksPrompt())
            //#else
            //$$ return unwrap(options.chatLinksPrompt)
            //#endif
        }
}
