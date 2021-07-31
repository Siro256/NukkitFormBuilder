package dev.siro256.nukkitformbuilder.form

import cn.nukkit.event.player.PlayerFormRespondedEvent
import cn.nukkit.form.response.FormResponseModal
import cn.nukkit.form.window.FormWindowModal
import com.google.gson.annotations.SerializedName
import dev.siro256.nukkitformbuilder.ModalFormBuilder

@Suppress("unused")
class FormWindowModalWithCallback(
    title: String,
    content: String,
    private var trueButtonTextWithCallback: Pair<String, (PlayerFormRespondedEvent) -> Unit>,
    private var falseButtonTextWithCallback: Pair<String, (PlayerFormRespondedEvent) -> Unit>
): FormWindowModal(title, content, trueButtonTextWithCallback.first, falseButtonTextWithCallback.first) {
    private var response: FormResponseModalWithCallback? = null

    override fun getResponse(): FormResponseModalWithCallback? {
        return response
    }

    override fun setResponse(data: String?) {
        when {
            data.equals("null") -> {
                closed = true
                return
            }
            data.equals("true") -> {
                response = FormResponseModalWithCallback(0, button1, trueButtonTextWithCallback.second)
            }
            else -> {
                response = FormResponseModalWithCallback(1, button2, falseButtonTextWithCallback.second)
            }
        }
    }

    class Builder: ModalFormBuilder() {
        private var trueButtonWithCallback: Pair<String, (PlayerFormRespondedEvent) -> Unit> = "" to {}
        private var falseButtonWithCallback: Pair<String, (PlayerFormRespondedEvent) -> Unit> = "" to {}

        override fun build(): FormWindowModalWithCallback {
            return FormWindowModalWithCallback(title, displayMessage, trueButtonWithCallback, falseButtonWithCallback)
        }

        override fun setTrueMessage(message: String): Builder {
            trueButtonWithCallback = message to {}
            return this
        }

        fun setTrueMessage(message: String, callback: (PlayerFormRespondedEvent) -> Unit): Builder {
            trueButtonWithCallback = message to callback
            return this
        }

        override fun setFalseMessage(message: String): Builder {
            falseButtonWithCallback = message to {}
            return this
        }

        fun setFalseMessage(message: String, callback: (PlayerFormRespondedEvent) -> Unit): Builder {
            falseButtonWithCallback = message to callback
            return this
        }
    }
}

data class FormResponseModalWithCallback(
    @get:JvmName("getClickedButtonId_") @SerializedName("clickedButtonId_") val clickedButtonId: Int,
    @get:JvmName("getClickedButtonText_") @SerializedName("clickedButtonText_") val clickedButtonText: String,
    val onClick: (PlayerFormRespondedEvent) -> Unit
): FormResponseModal(clickedButtonId, clickedButtonText)
