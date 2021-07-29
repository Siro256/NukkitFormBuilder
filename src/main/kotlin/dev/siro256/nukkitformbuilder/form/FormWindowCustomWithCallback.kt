package dev.siro256.nukkitformbuilder.form

import cn.nukkit.event.player.PlayerFormRespondedEvent
import cn.nukkit.form.element.*
import cn.nukkit.form.response.FormResponseCustom
import cn.nukkit.form.response.FormResponseData
import cn.nukkit.form.window.FormWindowCustom
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.siro256.nukkitformbuilder.CustomFormBuilder
import dev.siro256.nukkitformbuilder.FormIconType
import kotlin.reflect.KProperty1

@Suppress("unused")
class FormWindowCustomWithCallback(
    title: String,
    contentsWithCallback: List<Pair<Element, (PlayerFormRespondedEvent) -> Unit>>,
    icon: ElementButtonImageData?
): FormWindowCustom(title, contentsWithCallback.map { it.first }, icon) {
    constructor(title: String): this(title, emptyList())
    constructor(title: String, contentsWithCallback: List<Pair<Element, (PlayerFormRespondedEvent) -> Unit>>): this(title, contentsWithCallback, null)
    constructor(title: String, contentsWithCallback: List<Pair<Element, (PlayerFormRespondedEvent) -> Unit>>, icon: String): this(title, contentsWithCallback, ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_URL, icon))

    @Suppress("UNCHECKED_CAST")
    private val contents = let { this::class.members.first { it.name == "content" } as KProperty1<Any, *> }.get(this) as List<Element>
    private val callbacks = contentsWithCallback.map { it.second }
    private var callback: (PlayerFormRespondedEvent) -> Unit = {}
    private var response: FormResponseCustomWithCallback? = null

    override fun getResponse(): FormResponseCustomWithCallback? {
        return if (response == null) null else FormResponseCustomWithCallback(
            response!!.responses,
            response!!.dropdownResponses,
            response!!.inputResponses,
            response!!.sliderResponses,
            response!!.stepSliderResponses,
            response!!.toggleResponses,
            response!!.labelResponses,
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

        val elementResponses = Gson().fromJson<List<String>>(data, object: TypeToken<List<String>>() {}.type)

        val responses = hashMapOf<Int, Any>()
        val dropdownResponses = hashMapOf<Int, FormResponseData>()
        val inputResponses = hashMapOf<Int, String>()
        val sliderResponses = hashMapOf<Int, Float>()
        val stepSliderResponses = hashMapOf<Int, FormResponseData>()
        val toggleResponses = hashMapOf<Int, Boolean>()
        val labelResponses = hashMapOf<Int, String>()
        var i = 0

        run loop@{
            elementResponses.forEach {
                if (i >= contents.size) return@loop

                when (val element =  contents[i]) {
                    is ElementLabel -> {
                        element.text.apply {
                            labelResponses[i] = this
                            responses[i] = this
                        }
                    }
                    is ElementDropdown -> {
                        element.options[it.toInt()].apply {
                            dropdownResponses[i] = FormResponseData(it.toInt(), this)
                            responses[i] = this
                        }
                    }
                    is ElementInput -> {
                        inputResponses[i] = it
                        responses[i] = it
                    }
                    is ElementSlider -> {
                        it.toFloat().apply {
                            sliderResponses[i] = this
                            responses[i] = this
                        }
                    }
                    is ElementStepSlider -> {
                        element.steps[it.toInt()].apply {
                            stepSliderResponses[i] = FormResponseData(it.toInt(), this)
                            responses[i] = this
                        }
                    }
                    is ElementToggle -> {
                        it.toBoolean().apply {
                            toggleResponses[i] = this
                            responses[i] = this
                        }
                    }
                }

                i++
            }
        }

        response = FormResponseCustomWithCallback(responses, dropdownResponses, inputResponses, sliderResponses, stepSliderResponses, toggleResponses, labelResponses, callback)
    }

    class Builder: CustomFormBuilder() {
        private val contentsWithCallback = mutableListOf<Pair<Element, (PlayerFormRespondedEvent) -> Unit>>()

        override fun build(): FormWindowCustomWithCallback {
            if (iconType == null) return FormWindowCustomWithCallback(title, contentsWithCallback)
            return when(iconType!!) {
                FormIconType.IMAGE_DATA -> FormWindowCustomWithCallback(title, contentsWithCallback, iconData as ElementButtonImageData)
                FormIconType.STRING -> FormWindowCustomWithCallback(title, contentsWithCallback, iconData as String)
            }
        }

        override fun addContent(content: Element): Builder {
            contentsWithCallback.add(content to {})
            return this
        }

        fun addContent(content: Element, callback: (PlayerFormRespondedEvent) -> Unit): Builder {
            contentsWithCallback.add(content to callback)
            return this
        }
    }
}

data class FormResponseCustomWithCallback(
    @get:JvmName("getResponses_") val responses: HashMap<Int, Any>,
    @get:JvmName("getDropdownResponses_") val dropdownResponses: HashMap<Int, FormResponseData>,
    @get:JvmName("getInputResponses_") val inputResponses: HashMap<Int, String>,
    @get:JvmName("getSliderResponses_") val sliderResponses: HashMap<Int, Float>,
    @get:JvmName("getStepSliderResponses_") val stepSliderResponses: HashMap<Int, FormResponseData>,
    @get:JvmName("getToggleResponses_") val toggleResponses: HashMap<Int, Boolean>,
    @get:JvmName("getLabelResponses_") val labelResponses: HashMap<Int, String>,
    val onClick: (PlayerFormRespondedEvent) -> Unit
): FormResponseCustom(responses, dropdownResponses, inputResponses, sliderResponses, stepSliderResponses, toggleResponses, labelResponses)
