package dev.deftu.omnicore.api.client.render.errors

public class ForwardedOpenGlException(error: GlError) : RuntimeException("OpenGL error occurred: $error")
