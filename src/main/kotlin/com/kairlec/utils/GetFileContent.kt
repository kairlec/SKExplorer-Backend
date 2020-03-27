package com.kairlec.utils

import java.io.File;

fun File?.content(): String {
    if (this == null || !this.exists()) {
        return ""
    }
    return this.readText()
}
