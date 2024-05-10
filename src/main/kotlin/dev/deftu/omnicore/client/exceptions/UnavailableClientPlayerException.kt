package dev.deftu.omnicore.client.exceptions

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side

@GameSide(Side.CLIENT)
public class UnavailableClientPlayerException : Exception("The client player is not currently available.")
