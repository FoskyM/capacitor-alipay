package top.fosky.plugins.alipay

import com.getcapacitor.Logger

class Alipay {
    fun echo(value: String?): String? {
        Logger.info("Echo", value)
        return value
    }
}
