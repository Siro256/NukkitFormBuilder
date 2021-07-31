package dev.siro256.nukkitformbuilder.form

import cn.nukkit.event.player.PlayerFormRespondedEvent

interface FormWithCallback {
    fun getOnClick(): (PlayerFormRespondedEvent) -> Unit
}
