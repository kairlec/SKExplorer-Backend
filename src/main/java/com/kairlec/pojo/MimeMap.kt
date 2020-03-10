package com.kairlec.pojo

import com.kairlec.annotation.NoArg

@NoArg
data class MimeMap(
        var ext: String,
        var mime: String
)
