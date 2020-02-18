package com.sll.permissiondemo

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sll.permission.request
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_storage.setOnClickListener {
            request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) {
                Log.d("TAG", it.toString())
            }
        }
        btn_camera.setOnClickListener {
            request(Manifest.permission.CAMERA) {
                Log.d("TAG", it.toString())
            }
        }
    }
}
