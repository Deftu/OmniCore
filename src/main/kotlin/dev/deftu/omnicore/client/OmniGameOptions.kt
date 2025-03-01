package dev.deftu.omnicore.client

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side

//#if MC >= 1.18.2
import net.minecraft.client.render.ChunkBuilderMode
//#endif

//#if MC >= 1.16.5
import net.minecraft.client.option.CloudRenderMode
import net.minecraft.client.option.GraphicsMode
import net.minecraft.client.util.InputUtil
//#else
//$$ import net.minecraft.client.settings.GameSettings
//#endif

@GameSide(Side.CLIENT)
public object OmniGameOptions {

    @GameSide(Side.CLIENT)
    public object OmniVideoOptions {

        // Supported features
        @GameSide(Side.CLIENT)
        public val isSimulationDistanceSupported: Boolean
            //#if MC >= 1.18.2
            get() = true
            //#else
            //$$ get() = false
            //#endif

        @GameSide(Side.CLIENT)
        public val isEntityDistanceSupported: Boolean
            //#if MC >= 1.17.1
            get() = true
            //#else
            //$$ get() = false
            //#endif

        // Settings
        @GameSide(Side.CLIENT)
        public val viewDistance: Int
            get() = OmniClient.getInstance().options.viewDistance
                //#if MC >= 1.19.2
                .value
                //#endif

        @GameSide(Side.CLIENT)
        public val simulationDistance: Int
            get() {
                //#if MC >= 1.18.2
                return OmniClient.getInstance().options.simulationDistance
                    //#if MC >= 1.19.2
                    .value
                    //#endif
                //#else
                //$$ return -1
                //#endif
            }

        @GameSide(Side.CLIENT)
        public val entityDistance: Double
            get() {
                //#if MC >= 1.17.1
                return OmniClient.getInstance().options.entityDistanceScaling
                    //#if MC >= 1.19.2
                    .value
                    //#elseif MC >= 1.17.1
                    //$$ .toDouble()
                    //#endif
                //#else
                //$$ return -1.0
                //#endif
            }

        @GameSide(Side.CLIENT)
        public val maxFramerate: Int
            get() = OmniClient.getInstance().options.maxFps
                //#if MC >= 1.19.2
                .value
            //#endif

        @GameSide(Side.CLIENT)
        public val cloudRenderingMode: CloudRenderingMode
            get() {
                val value = OmniClient.getInstance().options.cloudRenderMode
                    //#if MC >= 1.19.2
                    .value
                    //#endif
                return CloudRenderingMode.fromVanilla(value)
            }

        @GameSide(Side.CLIENT)
        public val graphicsMode: RenderMode
            get() {
                //#if MC >= 1.16.5
                val value = OmniClient.getInstance().options.graphicsMode
                    //#if MC >= 1.19.2
                    .value
                    //#endif
                return RenderMode.fromVanilla(value)
                //#else
                //$$ return if (OmniClient.getInstance().gameSettings.fancyGraphics) RenderMode.FANCY else RenderMode.FAST
                //#endif
            }

        @GameSide(Side.CLIENT)
        public val smoothLightingMode: SmoothLightingMode
            get() {
                //#if MC >= 1.19.4
                return if (OmniClient.getInstance().options.ao.value) SmoothLightingMode.MAXIMUM else SmoothLightingMode.OFF
                //#elseif MC >= 1.16.5
                //$$ val value = OmniClient.getInstance().options.ambientOcclusion()
                     //#if MC >= 1.19.2
                     //$$ .get()
                     //$$ .id
                     //#else
                     //$$ .id
                     //#endif
                //$$ return SmoothLightingMode.fromVanilla(value)
                //#else
                //$$ return SmoothLightingMode.fromVanilla(OmniClient.getInstance().gameSettings.ambientOcclusion)
                //#endif
            }

        @GameSide(Side.CLIENT)
        public val chunkBuildingMode: ChunkBuildingMode
            get() {
                //#if MC >= 1.18.2
                val value = OmniClient.getInstance().options.chunkBuilderMode
                    //#if MC >= 1.19.2
                    .value
                    //#endif
                return ChunkBuildingMode.fromVanilla(value)
                //#else
                //$$ return ChunkBuildingMode.NEARBY // Default behavior in older versions
                //#endif
            }

        @GameSide(Side.CLIENT)
        public val mipmapLevels: Int
            get() = OmniClient.getInstance().options.mipmapLevels
                //#if MC >= 1.19.2
                .value
                //#endif

        // TODO: Attack indicator
        // TODO: Biome blend

        @GameSide(Side.CLIENT)
        public val isVsyncEnabled: Boolean
            get() = OmniClient.getInstance().options.enableVsync
                //#if MC >= 1.19.2
                .value
                //#endif

        @GameSide(Side.CLIENT)
        public val isEntityShadowEnabled: Boolean
            get() = OmniClient.getInstance().options.entityShadows
                //#if MC >= 1.19.2
                .value
                //#endif

        @GameSide(Side.CLIENT)
        public val isForceUnicodeFontEnabled: Boolean
            get() = OmniClient.getInstance().options.forceUnicodeFont
                //#if MC >= 1.19.2
                .value
                //#endif

        @GameSide(Side.CLIENT)
        public val isFullscreen: Boolean
            get() = OmniClient.getInstance().options.fullscreen
                //#if MC >= 1.19.2
                .value
                //#endif

        @GameSide(Side.CLIENT)
        public val isViewBobbingEnabled: Boolean
            get() = OmniClient.getInstance().options.bobView
                //#if MC >= 1.19.2
                .value
                //#endif

    }

    @GameSide(Side.CLIENT)
    public object OmniAccessibilityOptions {

        // Supported features
        @GameSide(Side.CLIENT)
        public val isMonochromeLogoSupported: Boolean
            get() =
                //#if MC >= 1.17.1
                true
                //#else
                //$$ false
                //#endif

        @GameSide(Side.CLIENT)
        public val isHideLightningFlashesSupported: Boolean
            get() =
                //#if MC >= 1.18.2
                true
                //#else
                //$$ false
                //#endif

        @GameSide(Side.CLIENT)
        public val isToastDisplayTimeSupported: Boolean
            get() =
                //#if MC >= 1.19.4
                true
                //#else
                //$$ false
                //#endif

        @GameSide(Side.CLIENT)
        public val isHighContrastSupported: Boolean
            get() =
                //#if MC >= 1.19.4
                true
                //#else
                //$$ false
                //#endif

        @GameSide(Side.CLIENT)
        public val isPanoramaSpeedSupported: Boolean
            get() =
                //#if MC >= 1.19.3
                true
                //#else
                //$$ false
                //#endif

        // Settings
        @GameSide(Side.CLIENT)
        public val isMonochromeLogo: Boolean
            get() {
                //#if MC >= 1.17.1
                return OmniClient.getInstance().options.monochromeLogo
                    //#if MC >= 1.19.2
                    .value
                    //#endif
                //#else
                //$$ return false
                //#endif
            }

        @GameSide(Side.CLIENT)
        public val isHideLightningFlashesEnabled: Boolean
            get() {
                //#if MC >= 1.18.2
                return OmniClient.getInstance().options.hideLightningFlashes
                    //#if MC >= 1.19.2
                    .value
                    //#endif
                //#else
                //$$ return false
                //#endif
            }

        @GameSide(Side.CLIENT)
        public val toastDisplayTime: Double
            get() {
                //#if MC >= 1.19.4
                return OmniClient.getInstance().options.notificationDisplayTime.value
                //#else
                //$$ return -1.0
                //#endif
            }

        @GameSide(Side.CLIENT)
        public val isReduceDebugInfoEnabled: Boolean
            get() = OmniClient.getInstance().options.reducedDebugInfo
                //#if MC >= 1.19.2
                .value
                //#endif

        @GameSide(Side.CLIENT)
        public val isHighContrast: Boolean
            get() {
                //#if MC >= 1.19.4
                return OmniClient.getInstance().options.highContrast.value
                //#else
                //$$ return false
                //#endif
            }

        @GameSide(Side.CLIENT)
        public val panoramaSpeed: Double
            get() {
                //#if MC >= 1.19.3
                return OmniClient.getInstance().options.panoramaSpeed.value
                //#else
                //$$ return -1.0
                //#endif
            }

    }

    @GameSide(Side.CLIENT)
    public object OmniChatSettings {

        // Supported features
        @GameSide(Side.CLIENT)
        public val isChatLineSpacingSupported: Boolean
            //#if MC >= 1.16.5
            get() = true
            //#else
            //$$ get() = false
            //#endif

        @GameSide(Side.CLIENT)
        public val isTextBackgroundOpacitySupported: Boolean
            //#if MC >= 1.16.5
            get() = true
            //#else
            //$$ get() = false
            //#endif

        @GameSide(Side.CLIENT)
        public val isChatDelaySupported: Boolean
            //#if MC >= 1.16.5
            get() = true
            //#else
            //$$ get() = false
            //#endif

        @GameSide(Side.CLIENT)
        public val isCommandSuggestionOptionSupported: Boolean
            //#if MC >= 1.16.5
            get() = true
            //#else
            //$$ get() = false
            //#endif

        // Settings
        @GameSide(Side.CLIENT)
        public val chatOpacity: Double
            get() = OmniClient.getInstance().options.chatOpacity
                //#if MC >= 1.19.2
                .value
                //#elseif MC <= 1.12.2
                //$$ .toDouble()
                //#endif

        // TODO: Chat visibility

        @GameSide(Side.CLIENT)
        public val chatLineSpacing: Double
            get() {
                //#if MC >= 1.16.5
                return OmniClient.getInstance().options.chatLineSpacing
                    //#if MC >= 1.19.2
                    .value
                    //#endif
                //#else
                //$$ return -1.0
                //#endif
            }

        @GameSide(Side.CLIENT)
        public val textBackgroundOpacity: Double
            get() {
                //#if MC >= 1.16.5
                return OmniClient.getInstance().options.textBackgroundOpacity
                    //#if MC >= 1.19.2
                    .value
                    //#endif
                //#else
                //$$ return -1.0
                //#endif
            }

        @GameSide(Side.CLIENT)
        public val chatScale: Double
            get() = OmniClient.getInstance().options.chatScale
                //#if MC >= 1.19.2
                .value
                //#elseif MC <= 1.12.2
                //$$ .toDouble()
                //#endif

        @GameSide(Side.CLIENT)
        public val chatWidth: Double
            get() = OmniClient.getInstance().options.chatWidth
                //#if MC >= 1.19.2
                .value
                //#elseif MC <= 1.12.2
                //$$ .toDouble()
                //#endif

        @GameSide(Side.CLIENT)
        public val chatHeightUnfocused: Double
            get() = OmniClient.getInstance().options.chatHeightUnfocused
                //#if MC >= 1.19.2
                .value
                //#elseif MC <= 1.12.2
                //$$ .toDouble()
                //#endif

        @GameSide(Side.CLIENT)
        public val chatHeightFocused: Double
            get() = OmniClient.getInstance().options.chatHeightFocused
                //#if MC >= 1.19.2
                .value
                //#elseif MC <= 1.12.2
                //$$ .toDouble()
                //#endif

        @GameSide(Side.CLIENT)
        public val chatDelay: Double
            get() {
                //#if MC >= 1.16.5
                return OmniClient.getInstance().options.chatDelay
                    //#if MC >= 1.19.2
                    .value
                    //#endif
                //#else
                //$$ return -1.0
                //#endif
            }

        @GameSide(Side.CLIENT)
        public val isCommandSuggestingEnabled: Boolean
            get() {
                //#if MC >= 1.16.5
                return OmniClient.getInstance().options.autoSuggestions
                    //#if MC >= 1.19.2
                    .value
                    //#endif
                //#else
                //$$ return false
                //#endif
            }

        @GameSide(Side.CLIENT)
        public val isChatColorEnabled: Boolean
            get() = OmniClient.getInstance().options.chatColors
                //#if MC >= 1.19.2
                .value
                //#endif

        @GameSide(Side.CLIENT)
        public val isChatLinksEnabled: Boolean
            get() = OmniClient.getInstance().options.chatLinks
                //#if MC >= 1.19.2
                .value
                //#endif

        @GameSide(Side.CLIENT)
        public val isChatLinksPromptEnabled: Boolean
            get() = OmniClient.getInstance().options.chatLinksPrompt
                //#if MC >= 1.19.2
                .value
                //#endif

    }

    @GameSide(Side.CLIENT)
    public object OmniMouseSettings {

        // Supported features
        @GameSide(Side.CLIENT)
        public val isMouseWheelSensitivitySupported: Boolean
            //#if MC >= 1.16.5
            get() = true
            //#else
            //$$ get() = false
            //#endif

        @GameSide(Side.CLIENT)
        public val isRawMouseInputSupported: Boolean
            //#if MC >= 1.16.5
            get() = true
            //#else
            //$$ get() = false
            //#endif

        @GameSide(Side.CLIENT)
        public val isDiscreteScrollSupported: Boolean
            //#if MC >= 1.16.5
            get() = true
            //#else
            //$$ get() = false
            //#endif

        // Settings

        @GameSide(Side.CLIENT)
        public val mouseSensitivity: Double
            get() = OmniClient.getInstance().options.mouseSensitivity
                //#if MC >= 1.19.2
                .value
                //#elseif MC <= 1.12.2
                //$$ .toDouble()
                //#endif

        @GameSide(Side.CLIENT)
        public val mouseWheelSensitivity: Double
            get() {
                //#if MC >= 1.16.5
                return OmniClient.getInstance().options.mouseWheelSensitivity
                    //#if MC >= 1.19.2
                    .value
                    //#endif
                //#else
                //$$ return -1.0
                //#endif
            }

        @GameSide(Side.CLIENT)
        public val isRawMouseInputEnabled: Boolean
            get() {
                //#if MC >= 1.16.5
                return OmniClient.getInstance().options.rawMouseInput
                    //#if MC >= 1.19.2
                    .value
                    //#endif
                //#else
                //$$ return false
                //#endif
            }

        @GameSide(Side.CLIENT)
        public val isMouseYInverted: Boolean
            get() = OmniClient.getInstance().options.invertYMouse
                //#if MC >= 1.19.2
                .value
                //#endif

        @GameSide(Side.CLIENT)
        public val isDiscreteScroll: Boolean
            get() {
                //#if MC >= 1.16.5
                return OmniClient.getInstance().options.discreteMouseScroll
                    //#if MC >= 1.19.2
                    .value
                    //#endif
                //#else
                //$$ return false
                //#endif
            }

        @GameSide(Side.CLIENT)
        public val isTouchscreenMode: Boolean
            get() = OmniClient.getInstance().options.touchscreen
                //#if MC >= 1.19.2
                .value
                //#endif

    }

    @GameSide(Side.CLIENT)
    public object OmniKeyBindingSettings {

        // Supported features
        @GameSide(Side.CLIENT)
        public val isAutoJumpSupported: Boolean
            //#if MC >= 1.12.2
            get() = true
            //#else
            //$$ get() = false
            //#endif

        @GameSide(Side.CLIENT)
        public val isToggleSneakSupported: Boolean
            //#if MC >= 1.16.5
            get() = true
            //#else
            //$$ get() = false
            //#endif

        @GameSide(Side.CLIENT)
        public val isToggleSprintSupported: Boolean
            //#if MC >= 1.16.5
            get() = true
            //#else
            //$$ get() = false
            //#endif

        // Settings
        @GameSide(Side.CLIENT)
        public val isAutoJumpEnabled: Boolean
            get() {
                //#if MC >= 1.12.2
                return OmniClient.getInstance().options.autoJump
                    //#if MC >= 1.19.2
                    .value
                    //#endif
                //#else
                //$$ return false
                //#endif
            }

        @GameSide(Side.CLIENT)
        public val isOperatorItemsTabEnabled: Boolean
            get() {
                //#if MC >= 1.19.3
                return OmniClient.getInstance().options.operatorItemsTab
                    //#if MC >= 1.19.2
                    .value
                //#endif
                //#else
                //$$ return false
                //#endif
            }

        @GameSide(Side.CLIENT)
        public val isToggleSneakEnabled: Boolean
            get() {
                //#if MC >= 1.16.5
                return OmniClient.getInstance().options.sneakToggled
                    //#if MC >= 1.19.2
                    .value
                    //#endif
                //#else
                //$$ return false
                //#endif
            }

        @GameSide(Side.CLIENT)
        public val isToggleSprintEnabled: Boolean
            get() {
                //#if MC >= 1.16.5
                return OmniClient.getInstance().options.sprintToggled
                    //#if MC >= 1.19.2
                    .value
                    //#endif
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @JvmOverloads
        @GameSide(Side.CLIENT)
        public fun getDisplayName(code: Int, scanCode: Int = -1): String {
            val name =
                //#if MC >= 1.16.5
                InputUtil.fromKeyCode(code, scanCode).toString()
                //#else
                //$$ GameSettings.getKeyDisplayString(code) ?: return "Unknown"
                //#endif

            return if (name.length == 1) name.first().uppercase() else name
        }

    }

    // TODO: Resourcepacks
    // TODO: Player model parts
    // TODO: Main arm
    // TODO: Sounds

    @GameSide(Side.CLIENT)
    public enum class CloudRenderingMode {
        DISABLED,
        FAST,
        FANCY;

        internal companion object {

            //#if MC >= 1.16.5
            fun fromVanilla(vanilla: CloudRenderMode): CloudRenderingMode = when (vanilla) {
                CloudRenderMode.OFF -> DISABLED
                CloudRenderMode.FAST -> FAST
                CloudRenderMode.FANCY -> FANCY
            }
            //#else
            //$$ fun fromVanilla(vanilla: Int): CloudRenderingMode = when (vanilla) {
            //$$     0 -> DISABLED
            //$$     1 -> FAST
            //$$     2 -> FANCY
            //$$     else -> throw IllegalArgumentException("Unknown cloud rendering mode: $vanilla")
            //$$ }
            //#endif

        }
    }

    @GameSide(Side.CLIENT)
    public enum class RenderMode {
        FAST,
        FANCY,
        FABULOUS;

        //#if MC >= 1.16.5
        internal companion object {

            fun fromVanilla(vanilla: GraphicsMode): RenderMode = when (vanilla) {
                GraphicsMode.FAST -> FAST
                GraphicsMode.FANCY -> FANCY
                //#if MC >= 1.16.5
                GraphicsMode.FABULOUS -> FABULOUS
                //#endif
            }

        }
        //#endif
    }

    @GameSide(Side.CLIENT)
    public enum class SmoothLightingMode {
        OFF,
        MINIMUM,
        MAXIMUM;

        public companion object {

            internal fun fromVanilla(vanilla: Int): SmoothLightingMode = when (vanilla) {
                0 -> OFF
                1 -> MINIMUM
                2 -> MAXIMUM
                else -> throw IllegalArgumentException("Unknown smooth lighting mode: $vanilla")
            }

        }
    }

    @GameSide(Side.CLIENT)
    public enum class ChunkBuildingMode {
        NONE,
        PLAYER_AFFECTED,
        NEARBY;

        //#if MC >= 1.18.2
        internal companion object {

            fun fromVanilla(vanilla: ChunkBuilderMode): ChunkBuildingMode = when (vanilla) {
                ChunkBuilderMode.NONE -> NONE
                ChunkBuilderMode.PLAYER_AFFECTED -> PLAYER_AFFECTED
                ChunkBuilderMode.NEARBY -> NEARBY
            }

        }
        //#endif
    }

}
