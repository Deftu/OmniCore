package dev.deftu.omnicore.client.options

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side

@GameSide(Side.CLIENT)
public object OmniVideoSettings {

    @GameSide(Side.CLIENT)
    public object Capabilities {

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isSimulationDistanceSupported: Boolean
            get() {
                //#if MC >= 1.18.2
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isEntityDistanceSupported: Boolean
            get() {
                //#if MC >= 1.17.1
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isAttackIndicatorSupported: Boolean
            get() {
                //#if MC >= 1.9
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isBiomeBlendSupported: Boolean
            get() {
                //#if MC >= 1.13
                return true
                //#else
                //$$ return false
                //#endif
            }

    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val renderDistance: Int
        get() = unwrap(options.viewDistance)

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val simulationDistance: Int
        get() {
            //#if MC >= 1.18.2
            return unwrap(options.simulationDistance)
            //#else
            //$$ return -1 // Inconsistent between singleplayer and multiplayer so we'll just return -1
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val entityDistance: Double
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.entityDistanceScaling)
            //#elseif MC >= 1.17.1
            //$$ return options.entityDistanceScaling.toDouble()
            //#else
            //$$ return -1 // Inconsistent between singleplayer and multiplayer so we'll just return -1
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val maxFps: Int
        get() = unwrap(options.maxFps)

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val cloudRenderMode: CloudRenderMode
        get() = CloudRenderMode.from(unwrap(options.cloudRenderMode))

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val renderMode: RenderMode
        get() = RenderMode.from(unwrap(options.graphicsMode))

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val smoothLightingMode: SmoothLightingMode
        get() {
            //#if MC >= 1.19.4
            return if (unwrap(options.ao)) {
                SmoothLightingMode.MAXIMUM
            } else {
                SmoothLightingMode.OFF
            }
            //#elseif MC >= 1.16.5
            //$$ return SmoothLightingMode.from(unwrap(options.ambientOcclusion.id))
            //#else
            //$$ return SmoothLightingMode.from(options.ambientOcclusion)
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val chunkBuildingMode: ChunkBuildingMode
        get() {
            //#if MC >= 1.18.2
            return ChunkBuildingMode.from(unwrap(options.chunkBuilderMode))
            //#else
            //$$ return ChunkBuildingMode.NEARBY
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val mipmapLevels: Int
        get() = unwrap(options.mipmapLevels)

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val attackIndicator: AttackIndicatorPosition
        get() {
            //#if MC >= 1.9
            return AttackIndicatorPosition.from(unwrap(options.attackIndicator))
            //#else
            //$$ return AttackIndicatorPosition.OFF // Not supported in versions before 1.9
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val biomeBlendRadius: Int
        get() {
            //#if MC >= 1.13
            return unwrap(options.biomeBlendRadius)
            //#else
            //$$ return 0 // Not supported in versions before 1.13
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isVsyncEnabled: Boolean
        get() = unwrap(options.enableVsync)

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isEntityShadowEnabled: Boolean
        get() = unwrap(options.entityShadows)

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isForceUnicodeFont: Boolean
        get() = unwrap(options.forceUnicodeFont)

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isFullscreen: Boolean
        get() = unwrap(options.fullscreen)

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isViewBobbingEnabled: Boolean
        get() = unwrap(options.bobView)

}
