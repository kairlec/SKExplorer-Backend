package com.kairlec.model.vo

data class ExtraInfo(
        var description: String,
        var redirect: Boolean,
        var redirectContent: String
) {
    companion object {
        val Default: ExtraInfo
            get() = ExtraInfo("", false, "")
    }
}