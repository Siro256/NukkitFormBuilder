package dev.siro256.nukkitformbuilder

import cn.nukkit.form.window.FormWindowModal

class ModalFormBuilder: FormBuilder() {
    private var displayMessage = ""
    private var trueButtonMessage = ""
    private var falseButtonMessage = ""

    override fun build(): FormWindowModal {
        return FormWindowModal(title, displayMessage, trueButtonMessage, falseButtonMessage)
    }

    fun setDisplayMessage(message: String): ModalFormBuilder {
        displayMessage = message
        return this
    }

    fun setTrueMessage(message: String): ModalFormBuilder {
        trueButtonMessage = message
        return this
    }

    fun setFalseMessage(message: String): ModalFormBuilder {
        falseButtonMessage = message
        return this
    }
}
