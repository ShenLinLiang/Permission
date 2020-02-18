package com.sll.permission

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Shenlinliang
 * @date 2020/2/18
 */
fun request(
    vararg permission: String,
    callback: (Result) -> Unit
) {
    val request = Request(permission, callback)
    RequestPool.add(request)
    exec(request)
}

data class Result(
    val isGranted: Boolean,
    val message: String = "",
    val alwaysDenied: Boolean = false
) {
    override fun toString(): String {
        return "Result(isGranted=$isGranted, message='$message', alwaysDenied=$alwaysDenied)"
    }
}

data class Request(
    val permissions: Array<out String>,
    val callback: (Result) -> Unit,
    val requestCode: Int = CODE.incrementAndGet()
) {

    companion object {
        private val CODE = AtomicInteger(0)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false

        if (!permissions.contentEquals(other.permissions)) return false
        if (requestCode != other.requestCode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = permissions.contentHashCode()
        result = 31 * result + requestCode
        return result
    }
}

object RequestPool {
    private val map = ConcurrentHashMap<Int, Request>()

    fun add(request: Request) {
        map[request.requestCode] = request
    }

    fun get(requestCode: Int): Request? {
        return map[requestCode]
    }

    fun remove(requestCode: Int) {
        map.remove(requestCode)
    }
}
