package com.kairlec.utils

import java.io.File

val File?.content: String
    get() {
        if (this == null || !this.exists()) {
            return ""
        }
        return this.readText(Charsets.UTF_8)
    }
