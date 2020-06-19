package com.kairlec.intf;

import com.fasterxml.jackson.annotation.JsonIgnore
import com.kairlec.utils.LocalConfig.Companion.toJSON


interface ResponseDataInterface {
    val code: Int
    val msg: String
    val data: Any?


    val json
        @JsonIgnore
        get() = String.toJSON(this)

}
