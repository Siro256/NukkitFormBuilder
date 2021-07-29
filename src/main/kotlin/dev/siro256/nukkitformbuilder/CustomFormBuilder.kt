package dev.siro256.nukkitformbuilder

import cn.nukkit.form.element.Element
import cn.nukkit.form.element.ElementButtonImageData
import cn.nukkit.form.window.FormWindowCustom

@Suppress("unused")
open class CustomFormBuilder: FormBuilder() {
    private val contents = mutableListOf<Element>()
    protected var iconType: FormIconType? = null
    protected var iconData: Any? = null

    override fun build(): FormWindowCustom {
        if (iconType == null) return FormWindowCustom(title, contents)
        return when(iconType!!) {
            FormIconType.IMAGE_DATA -> FormWindowCustom(title, contents, iconData as ElementButtonImageData)
            FormIconType.STRING -> FormWindowCustom(title, contents, iconData as String)
        }
    }

    open fun addContent(content: Element): CustomFormBuilder {
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
