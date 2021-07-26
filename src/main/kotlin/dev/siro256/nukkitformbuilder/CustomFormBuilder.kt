package dev.siro256.nukkitformbuilder

import cn.nukkit.form.element.Element
import cn.nukkit.form.element.ElementButtonImageData
import cn.nukkit.form.window.FormWindow
import cn.nukkit.form.window.FormWindowCustom

@Suppress("unused")
class CustomFormBuilder: FormBuilder() {
    private val contents = mutableListOf<Element>()
    private var iconType: FormIconType? = null
    private var iconData: Any? = null

    override fun build(): FormWindow {
        if (iconType == null) return FormWindowCustom(title, contents)
        return when(iconType!!) {
            FormIconType.IMAGE_DATA -> FormWindowCustom(title, contents, iconData as ElementButtonImageData)
            FormIconType.STRING -> FormWindowCustom(title, contents, iconData as String)
        }
    }

    fun addContent(content: Element): CustomFormBuilder {
        contents.add(content)
        return this
    }

    fun setIconType(type: FormIconType): CustomFormBuilder {
        iconType = type
        return this
    }

    fun setIconData(data: Any): CustomFormBuilder {
        iconData = data
        return this
    }
}

enum class FormIconType {
    IMAGE_DATA,
    STRING
}