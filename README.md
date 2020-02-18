#### 任何地方都可以申请权限
```
request(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
) { result ->
    Log.d("TAG", result.toString())
}
```