package top.fosky.plugins.alipay

import com.alipay.sdk.app.AlipayApi
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@CapacitorPlugin(name = "Alipay")
class AlipayPlugin : Plugin() {
    private val implementation = Alipay()
    private val scope = CoroutineScope(Dispatchers.Main)

    @PluginMethod
    fun register(call: PluginCall) {
        val appId = call.getString("appId")
        if (appId.isNullOrEmpty()) {
            call.reject("appId is required")
            return
        }

        val context = context
        if (context == null) {
            call.reject("Context is not available")
            return
        }

        try {
            AlipayApi.registerApp(context, appId)
            call.resolve()
        } catch (e: Exception) {
            call.reject("Register failed: ${e.message}", e)
        }
    }

    @PluginMethod
    fun pay(call: PluginCall) {
        val orderInfo = call.getString("orderInfo")
        if (orderInfo.isNullOrEmpty()) {
            call.reject("orderInfo is required")
            return
        }

        val activity = activity
        if (activity == null) {
            call.reject("Activity is not available")
            return
        }

        scope.launch {
            try {
                val result = implementation.pay(activity, orderInfo)
                val ret = JSObject()
                ret.put("resultStatus", result["resultStatus"] ?: "")
                ret.put("result", result["result"] ?: "")
                ret.put("memo", result["memo"] ?: "")
                call.resolve(ret)
            } catch (e: Exception) {
                call.reject("Payment failed: ${e.message}", e)
            }
        }
    }
}
