package dev.deftu.omnicore.internal.client.exceptions

import dev.deftu.omnicore.internal.client.framebuffer.FramebufferStatus

public class FramebufferStatusException(
    public val statusCode: Int,
    public val status: FramebufferStatus
) : RuntimeException("Framebuffer is incomplete: $status (${statusCode.toString(16)})")
