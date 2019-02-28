package com.seta.common.http

import com.seta.common.logs.LogX
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by SETA_WORK on 2017/7/3.
 */
class NetworkInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response? {
        val request = chain.request()
//        val request = original.newBuilder().build()
        val response = chain.proceed(request) ?: throw IOException("Response null.")
        try {
            val source = response.body()?.source()
            val buffer = source?.buffer()?.clone() ?: return response
            val string = buffer.readUtf8()

//            val body = response.body()
//            val contentType = body?.contentType()
//            val charSet = if (contentType != null) contentType.charset(Util.UTF_8) else Util.UTF_8
//            val source = body?.source()
//            Util.bomAwareCharset(source, charSet)
//            source?.readString(charset)

//            val string = response.body()?.string()
//            val jsonObject = JSONObject(string)
//            if(jsonObject.has("errno") && jsonObject["errno"]=="410010"){ //未登录
//                EventBus.getDefault().post(LogoutEvent())
//            }
            LogX.v("response: $string")
        } catch (t: Throwable) {

        }
        return response
    }
}


//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

//class NetworkInterceptor(private val logger: HttpLoggingInterceptor.Logger = HttpLoggingInterceptor.Logger.DEFAULT) : Interceptor {
//    var level: HttpLoggingInterceptor.Level? = null
//
//    init {
//        this.level = HttpLoggingInterceptor.Level.NONE
//    }
//
//    fun setLevel(level: HttpLoggingInterceptor.Level): NetworkInterceptor {
//        if (level == null) {
//            throw NullPointerException("level == null. Use Level.NONE instead.")
//        } else {
//            this.level = level
//            return this
//        }
//    }
//
////    fun getLevel(): HttpLoggingInterceptor.Level? {
////        return this.level
////    }
//
//    @Throws(IOException::class)
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val level = this.level
//        val request = chain.request()
//        if (level == HttpLoggingInterceptor.Level.NONE) {
//            return chain.proceed(request)
//        } else {
//            val logBody = level == HttpLoggingInterceptor.Level.BODY
//            val logHeaders = logBody || level == HttpLoggingInterceptor.Level.HEADERS
//            val requestBody = request.body()
//            val hasRequestBody = requestBody != null
//            val connection = chain.connection()
//            var requestStartMessage = "--> " + request.method() + ' '.toString() + request.url() + if (connection != null) " " + connection.protocol() else ""
//            if (!logHeaders && hasRequestBody) {
//                requestStartMessage = requestStartMessage + " (" + requestBody!!.contentLength() + "-byte body)"
//            }
//
//            this.logger.log(requestStartMessage)
//            if (logHeaders) {
//                if (hasRequestBody) {
//                    if (requestBody!!.contentType() != null) {
//                        this.logger.log("Content-Type: " + requestBody.contentType()!!)
//                    }
//
//                    if (requestBody.contentLength() != -1L) {
//                        this.logger.log("Content-Length: " + requestBody.contentLength())
//                    }
//                }
//
//                val headers = request.headers()
//                var i = 0
//
//                val count = headers.size()
//                while (i < count) {
//                    val name = headers.name(i)
//                    if (!"Content-Type".equals(name, ignoreCase = true) && !"Content-Length".equals(name, ignoreCase = true)) {
//                        this.logger.log(name + ": " + headers.value(i))
//                    }
//                    ++i
//                }
//
//                if (logBody && hasRequestBody) {
//                    if (this.bodyHasUnknownEncoding(request.headers())) {
//                        this.logger.log("--> END " + request.method() + " (encoded body omitted)")
//                    } else {
//                        val buffer = Buffer()
//                        requestBody!!.writeTo(buffer)
//                        var charset: Charset? = UTF8
//                        val contentType = requestBody.contentType()
//                        if (contentType != null) {
//                            charset = contentType.charset(UTF8)
//                        }
//
//                        this.logger.log("")
//                        if (isPlaintext(buffer)) {
//                            this.logger.log(buffer.readString(charset!!))
//                            this.logger.log("--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)")
//                        } else {
//                            this.logger.log("--> END " + request.method() + " (binary " + requestBody.contentLength() + "-byte body omitted)")
//                        }
//                    }
//                } else {
//                    this.logger.log("--> END " + request.method())
//                }
//            }
//
//            val startNs = System.nanoTime()
//
//            val response: Response
//            try {
//                response = chain.proceed(request)
//            } catch (var27: Exception) {
//                this.logger.log("<-- HTTP FAILED: $var27")
//                throw var27
//            }
//
//            val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
//            val responseBody = response.body()
//            val contentLength = responseBody!!.contentLength()
//            val bodySize = if (contentLength != -1L) contentLength.toString() + "-byte" else "unknown-length"
//            this.logger.log("<-- " + response.code() + (if (response.message().isEmpty()) "" else ' ' + response.message()) + ' '.toString() + response.request().url() + " (" + tookMs + "ms" + (if (!logHeaders) ", $bodySize body" else "") + ')'.toString())
//            if (logHeaders) {
//                val headers = response.headers()
//                var i = 0
//
//                val count = headers.size()
//                while (i < count) {
//                    this.logger.log(headers.name(i) + ": " + headers.value(i))
//                    ++i
//                }
//
//                if (logBody && HttpHeaders.hasBody(response)) {
//                    if (this.bodyHasUnknownEncoding(response.headers())) {
//                        this.logger.log("<-- END HTTP (encoded body omitted)")
//                    } else {
//                        val source = responseBody.source()
//                        source.request(9223372036854775807L)
//                        var buffer = source.buffer()
//                        var gzippedLength: Long? = null
//                        if ("gzip".equals(headers.get("Content-Encoding")!!, ignoreCase = true)) {
//                            gzippedLength = buffer.size()
//                            var gzippedResponseBody: GzipSource? = null
//
//                            try {
//                                gzippedResponseBody = GzipSource(buffer.clone())
//                                buffer = Buffer()
//                                buffer.writeAll(gzippedResponseBody)
//                            } finally {
//                                gzippedResponseBody?.close()
//
//                            }
//                        }
//
//                        var charset: Charset? = UTF8
//                        val contentType = responseBody.contentType()
//                        if (contentType != null) {
//                            charset = contentType.charset(UTF8)
//                        }
//
//                        if (!isPlaintext(buffer)) {
//                            this.logger.log("")
//                            this.logger.log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)")
//                            return response
//                        }
//
//                        if (contentLength != 0L) {
//                            this.logger.log("")
//                            this.logger.log(buffer.clone().readString(charset!!))
//                        }
//
//                        if (gzippedLength != null) {
//                            this.logger.log("<-- END HTTP (" + buffer.size() + "-byte, " + gzippedLength + "-gzipped-byte body)")
//                        } else {
//                            this.logger.log("<-- END HTTP (" + buffer.size() + "-byte body)")
//                        }
//                    }
//                } else {
//                    this.logger.log("<-- END HTTP")
//                }
//            }
//
//            return response
//        }
//    }
//
//    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
//        val contentEncoding = headers.get("Content-Encoding")
//        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true) && !contentEncoding.equals("gzip", ignoreCase = true)
//    }
//
//    interface Logger {
//
//        fun log(var1: String)
//
//        companion object {
//            val DEFAULT: HttpLoggingInterceptor.Logger = object : HttpLoggingInterceptor.Logger {
//                override fun log(message: String) {
//                    Platform.get().log(4, message, null as Throwable?)
//                }
//            }
//        }
//    }
//
//    enum class Level private constructor() {
//        NONE,
//        BASIC,
//        HEADERS,
//        BODY
//    }
//
//    companion object {
//        private val UTF8 = Charset.forName("UTF-8")
//
//        internal fun isPlaintext(buffer: Buffer): Boolean {
//            try {
//                val prefix = Buffer()
//                val byteCount = if (buffer.size() < 64L) buffer.size() else 64L
//                buffer.copyTo(prefix, 0L, byteCount)
//
//                var i = 0
//                while (i < 16 && !prefix.exhausted()) {
//                    val codePoint = prefix.readUtf8CodePoint()
//                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
//                        return false
//                    }
//                    ++i
//                }
//
//                return true
//            } catch (var6: EOFException) {
//                return false
//            }
//
//        }
//    }
//}
