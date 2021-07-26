package dev.siro256.nukkitformbuilder

import cn.nukkit.form.window.FormWindow

abstract class FormBuilder {
    internal var title = ""

    abstract fun build(): FormWindow
}

fun <T: FormBuilder> T.setTitle(title: String): T {
    this.title = title
    return this
}
