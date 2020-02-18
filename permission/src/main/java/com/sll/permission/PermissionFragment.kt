package com.sll.permission

import android.os.Bundle
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * @author Shenlinliang
 * @date 2020/2/18
 */
class PermissionFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            val request = RequestPool.get(getInt(REQUEST_CODE))!!
            realRequest(request)
        }
    }

    fun realRequest(request: Request) {
        if (checkPermissions(request.permissions)) {
            request.callback(Result(true))
        } else {
            requestPermissions(request.permissions, request.requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        var isGranted = true
        var alwaysDenied = false
        for (i in grantResults.indices) {
            if (grantResults[i] != PERMISSION_GRANTED) {
                isGranted = false
                alwaysDenied = !shouldShowRequestPermissionRationale(permissions[i])
                break
            }
        }

        val request = RequestPool.get(requestCode) ?: return
        request.callback(Result(isGranted, alwaysDenied = alwaysDenied))
    }

    companion object {
        private const val TAG = "VirtualFragment"
        private const val REQUEST_CODE = "CODE"

        fun open(fm: FragmentManager, request: Request) {
            val fragment = fm.findFragmentByTag(TAG)

            if (fragment != null) {
                (fragment as PermissionFragment).realRequest(request)
            } else {
                PermissionFragment().apply {
                    arguments = Bundle().apply {
                        putInt(REQUEST_CODE, request.requestCode)
                    }
                }.also {
                    fm.beginTransaction().add(it, TAG).commitAllowingStateLoss()
                }
            }
        }
    }
}