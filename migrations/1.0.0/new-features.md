# New Features

## Audiences
Audiences have been added to the API, allowing for more flexible and powerful ways to send messages, titles, action bars, and other forms of communication to players.
This could be a single player, all players, players in a specific world, players on a specific team, your own client player, or any combination thereof.
All of this, of course, depends on the implementation of the `Audience` interface you choose to use. This abstraction comes with the massive benefit of being able to broadcast
messages regardless of what the target audience is, without any knowledge of how many players there are, what environment they're in, etc.

## Item Creation
A new item creation API has been added, allowing for the quick and easy creation and registration of item entries, especially in Kotlin.
This API is designed to be as flexible as possible, down to the addition of your items to either your own, other mods' or built-in item groups (creative tabs).
The currently working Kotlin DSL looks something like this:
```kotlin
val exampleItem = buildItem {
    maxStackSize = 16
    maxDurability = 250
    isFireResistant = true
    tab = OmniCreativeTabs.buildingBlocks
    food = buildFood(nutrition = 4, saturation = 2.4f) {
        isMeat = true
        isAlwaysEdible = true
    }
}
```

## Item Groups
And, building off of the above, a new item group (creative tab) API has been added, allowing for the quick and easy creation, registration, modification and access of item groups.
Very similarly to the existing vanilla-bound keybinding API, you can simply create your own item group, then register it with a function call right on the variable declaration.
```kotlin
val exampleTab = OmniCreativeTab(
    id = identifierOrThrow("examplemod", "example_tab"),
    icon = ItemStack(exampleItem)
).register()
```

If you want to go as far as adding other mods' or vanilla items to your tab, or register your items manually, you can do that too!
```kotlin
val exampleTab = OmniCreativeTab(
    id = identifierOrThrow("examplemod", "example_tab"),
    icon = ItemStack(exampleItem)
).apply {
    append(exampleItem)
    append(Items.DIAMOND)
}.register()
```

## Client Chat Queueing
A new client chat queueing system has been added, allowing for the queuing of chat messages to be sent to the server when the player is able to.
This is especially useful for when several mods the player uses require a command to be sent to the server at join, avoiding chat spam and potential server kicks for spamming.
This also comes with the ability to delay messages by a certain amount of ticks, allowing for more complex chat interactions.
```kotlin
val message = "Hello, everyone!"
OmniClientChatQueue.queue(message)
OmniClientChatQueue.queue(message, delay = 20) // Delay by 1 second (20 ticks)
```

Sending delayed messages does come with the caveat that if you, or another mod, queues a delayed message, the queue will be backed up until that delayed message is sent.
This means that if you queue a message with a delay of 100 ticks, and then queue another message without a delay, the second message will not be sent until the first message is sent.
The queue works in a first-in, first-out (FIFO) manner, so messages will be sent in the order they were queued.
