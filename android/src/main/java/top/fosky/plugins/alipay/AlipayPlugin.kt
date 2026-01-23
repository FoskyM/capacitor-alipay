package top.fosky.plugins.alipay

import com.getcapacitor.JSObject

@CapacitorPlugin(name = "Alipay")
class AlipayPlugin : Plugin() {
    private val implementation = Alipay()

    @PluginMethod
    fun echo(call: PluginCall) {
        val value: String? = call.getString("value")

        val ret: JSObject = JSObject()
        ret.put("value", implementation.echo(value))
        call.resolve(ret)
    }
}
