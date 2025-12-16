package dev.deftu.omnicore.api.resources

import dev.deftu.textile.Text

public data class ResourcePackInfo(
    public val id: String,
    public val title: Text,
    public val description: Text?,
)
