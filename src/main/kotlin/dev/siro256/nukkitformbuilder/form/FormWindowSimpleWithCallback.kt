package dev.siro256.nukkitformbuilder.form

import cn.nukkit.event.player.PlayerFormRespondedEvent
import cn.nukkit.form.element.ElementButton
import cn.nukkit.form.response.FormResponseSimple
import cn.nukkit.form.window.FormWindowSimple
import com.google.gson.annotations.SerializedName
import dev.siro256.nukkitformbuilder.SimpleFormBuilder

@Suppress("unused")
class FormWindowSimpleWithCallback(
    title: String,
    content: String,
    buttonsWithCallback: List<Pair<ElementButton, (PlayerFormRespondedEvent) -> Unit>>
): FormWindowSimple(title, content, buttonsWithCallback.map { it.first }) {
    constructor(title: String, content: String): this(title, content, emptyList<Pair<ElementButton, (PlayerFormRespondedEvent) -> Unit>>())

    @Suppress("UNCHECKED_CAST") @get:JvmName("getButtons_")
    val buttons = buttonsWithCallback.map { it.first } as MutableList<ElementButton>
    private val callbacks = buttonsWithCallback.map { it.second }
    private var callback: (PlayerFormRespondedEvent) -> Unit = {}
    private var response: FormResponseSimpleWithCallback? = null

    override fun getButtons(): List<ElementButton> {
        return buttons
    }

    override fun addButton(button: ElementButton) {
        buttons.add(button)
    }

    override fun getResponse(): FormResponseSimpleWithCallback? {
        return if (response == null) null else FormResponseSimpleWithCallback(
            response!!.clickedButtonId,
            response!!.clickedButton,
            callback
        )
    }

    override fun setResponse(data: String?) {
        if (data.equals("null")) {
            closed = true
            return
        }

        callback = if (wasClosed()) {
            {}
        } else {
            data?.toIntOrNull()?.let { callbacks.getOrNull(it) } ?: {}
        }

        val buttonId = data?.toIntOrNull() ?: return
        response = FormResponseSimpleWithCallback(buttonId, buttons.getOrNull(buttonId), callback)
    }

    class Builder: SimpleFormBuilder() {
        private val buttonsWithCallback = mutableListOf<Pair<ElementButton, (PlayerFormRespondedEvent) -> Unit>>()

        override fun build(): FormWindowSimpleWithCallback {
            return FormWindowSimpleWithCallback(title, displayMessage, buttonsWithCallback)
        }

        override fun addButton(button: ElementButton): Builder {
            buttonsWithCallback.add(button to {})
            return this
        }

        fun addButton(button: ElementButton, callback: (PlayerFormRespondedEvent) -> Unit): Builder {
            buttonsWithCallback.add(button to callback)
            return this
        }
    }
}

data class FormResponseSimpleWithCallback(
    @get:JvmName("getClickedButtonId_") @SerializedName("clickedButtonId_") val clickedButtonId: Int,
    @get:JvmName("getClickedButton_") @SerializedName("clickedButton_") val clickedButton: ElementButton?,
    val onClick: (PlayerFormRespondedEvent) -> Unit
): FormResponseSimple(clickedButtonId, clickedButton)
