package dev.deftu.omnicore.api.client.chat

import dev.deftu.eventbus.on
import dev.deftu.omnicore.api.client.events.ClientTickEvent
import dev.deftu.omnicore.api.client.player
import dev.deftu.omnicore.api.eventBus
import java.util.concurrent.ConcurrentLinkedQueue

public object OmniClientChatSender {
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

            player ?: return@on
            val entry = queue.peek() ?: return@on
            if (entry.delay % tickCounter != 0L) {
                return@on
            }

            send(entry.message)
            queue.remove()
            tickCounter = 0
        }
    }

    /**
     * Queues a chat message to be sent once any previously queued messages have been sent.
     * Respects the queue per message; FIFO.
     *
     * @param message The message to send.
     * @param delay The delay in ticks between sending this message and the previous one.
     *             A delay of 0 means the message will be sent as soon as possible.
     *             Must be non-negative.
     */
    @JvmStatic
    @JvmOverloads
    public fun queue(message: String, delay: Int = 0) {
        require(delay >= 0) { "Delay must be non-negative" }
        queue.add(Entry(message, delay))
    }

    /**
     * Sends a chat message immediately, regardless of any queued messages.
     */
    @JvmStatic
    public fun send(message: String) {
        //#if MC >= 1.19.4
        player?.networkHandler?.sendChatMessage(message)
        //#elseif MC >= 1.19.2
        //$$ player?.chatSigned(message, null)
        //#else
        //$$ player?.sendChatMessage(message)
        //#endif
    }
}
