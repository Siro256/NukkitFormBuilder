package dev.siro256.nukkitformbuilder.listener

import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerFormRespondedEvent
import cn.nukkit.plugin.Plugin
import dev.siro256.nukkitformbuilder.form.FormWithCallback

@Suppress("unused")
class FormListener(plugin: Plugin) : Listener {
    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun onFormRespond(event: PlayerFormRespondedEvent) {
        if (event.response is FormWithCallback) (event.response as FormWithCallback).getOnClick().invoke(event)
    }
}
