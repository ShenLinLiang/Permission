package com.sll.permission

import androidx.core.content.PermissionChecker
import androidx.fragment.app.FragmentActivity
import com.sll.library.ContextProvider.Companion.application
import com.sll.library.ContextProvider.Companion.currentActivity

/**
 * @author Shenlinliang
 * @date 2020/2/18
 */

internal fun exec(request: Request) {
    when (val currentActivity = currentActivity()) {
        null -> request.callback(Result(false, "Activity not found!"))
        !is FragmentActivity -> request.callback(
            Result(
                false,
                "Activity should inherit FragmentActivity!"
            )
        )
        else -> {
            val fm = currentActivity.supportFragmentManager
            if (fm.isDestroyed) {
                request.callback(Result(false, "Activity already finished!"))
            } else {
                PermissionFragment.open(fm, request)
            }
        }
    }
}

internal fun checkPermissions(permissions: Array<out String>): Boolean {
    var result = true
    permissions.forEach {
        if (!checkPermission(it)) {
            result = false
        }
    }
    return result
}

internal fun checkPermission(permission: String): Boolean {
    return PermissionChecker.checkSelfPermission(
        application,
        permission
    ) == PermissionChecker.PERMISSION_GRANTED
}