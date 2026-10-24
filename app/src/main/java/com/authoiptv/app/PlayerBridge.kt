package com.authoiptv.app

import android.webkit.JavascriptInterface

class PlayerBridge(
    private val onLog: (String) -> Unit = {}
) {
    @Volatile var currentUrl: String = ""
    @Volatile var currentHeaders: Map<String, String> = emptyMap()
    @Volatile var clearKeyKid: String = ""
    @Volatile var clearKeyKey: String = ""
    @Volatile var isDash: Boolean = false

    @JavascriptInterface
    fun getStreamConfig(): String {
        val headersJson = currentHeaders.entries.joinToString(",") { (k, v) ->
            "\"${k.replace("\"", "\\\"")}\":\"${v.replace("\"", "\\\"")}\""
        }
        val kid = if (clearKeyKid.isBlank()) "" else "\"$clearKeyKid\""
        val key = if (clearKeyKey.isBlank()) "" else "\"$clearKeyKey\""
        return """
            {
              "url": "${currentUrl.replace("\"", "\\\"")}",
              "headers": { $headersJson },
              "isDash": $isDash,
              "clearKey": { "kid": $kid, "key": $key }
            }
        """.trimIndent()
    }

    @JavascriptInterface
    fun log(msg: String) { onLog(msg) }
}
