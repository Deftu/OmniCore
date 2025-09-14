package dev.deftu.omnicore.api.client.chat

import dev.deftu.eventbus.on
import dev.deftu.omnicore.api.client.events.ClientTickEvent
import dev.deftu.omnicore.api.client.player
import dev.deftu.omnicore.api.eventBus
import java.util.concurrent.ConcurrentLinkedQueue

public object OmniClientChatQueue {
    private data class Entry(val message: String, val delay: Int)

    private val queue = ConcurrentLinkedQueue<Entry>()
    private var tickCounter = 0L

    public fun initialize() {
        eventBus.on<ClientTickEvent.Pre> {
            tickCounter++
            if (queue.isEmpty()) {
                tickCounter = 0
                return@on
            }

            val player = player ?: return@on
            val entry = queue.peek() ?: return@on
            if (entry.delay % tickCounter != 0L) {
                return@on
            }

            //#if MC >= 1.19.4
            player.networkHandler?.sendChatMessage(entry.message)
            //#elseif MC >= 1.19.2
            //$$ player.chatSigned(entry.message, null)
            //#else
            //$$ player.sendChatMessage(entry.message)
            //#endif

            queue.remove()
            tickCounter = 0
        }
    }

    @JvmStatic
    @JvmOverloads
    public fun queue(message: String, delay: Int = 0) {
        require(delay >= 0) { "Delay must be non-negative" }
        queue.add(Entry(message, delay))
    }
}
