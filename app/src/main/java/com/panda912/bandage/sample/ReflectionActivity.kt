package com.panda912.bandage.sample

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import org.joor.Reflect
import java.lang.reflect.Field

class ReflectionActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ReflectionActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reflection)

        Log.d(
            TAG,
            "onCreate:version-sdk:${Build.VERSION.SDK_INT},fingerprint:${Build.FINGERPRINT}"
        )

        findViewById<TextView>(R.id.tv_sdk_info).text =
            "version-sdk:${Build.VERSION.SDK_INT},fingerprint:${Build.FINGERPRINT}"

        findViewById<TextView>(R.id.btn_test_VMRuntime).setOnClickListener {
            try {
                val clazz = Class.forName("dalvik.system.VMRuntime")
                val method =
                    clazz.getDeclaredMethod("setHiddenApiExemptions", Array<String>::class.java)
                method.isAccessible = true
                Log.d(TAG, "call setHiddenApiExemptions success.")
            } catch (e: Throwable) {
                Log.e(TAG, "Exception:$e")
            }
        }


        findViewById<TextView>(R.id.btn_test_activityThread_h).setOnClickListener {
            try {
                val hClazz = Class.forName("android.app.ActivityThread\$H")
                val fields: Array<Field> = hClazz.fields
                for (field in fields) {
                    Log.d(TAG, "field:${field}")
                }
            } catch (e: Throwable) {
                Log.e(TAG, "Exception:$e")
            }
        }

        findViewById<TextView>(R.id.btn_test_activityThread).setOnClickListener {
            try {
                val clazz = Class.forName("android.app.ActivityThread")

                // 获取静态变量sCurrentActivityThread
                val declaredField =
                    clazz.getDeclaredField("sCurrentActivityThread").apply { isAccessible = true }

//                val handler1 = declaredField.get(null) as Handler


                // ? 这里调用了静态方法currentActivityThread，返回的是ActivityThread实例
                val invoke = clazz.getDeclaredMethod("currentActivityThread").invoke(null)
                /**
                 * 这里有个疑问？
                 *
                 * 为什么这里获取的invoke是ActivityThread实例，而sCurrentActivityThread获取的却是ActivityThread的静态变量
                 *
                 * 1. 需要注意的是：
                 * 获取mH变量的对象后，需要再访问mH变量。即mH.get(null)
                 */
                Log.d(TAG, "invoke:${invoke},\ndeclaredField:${declaredField.get(null)}")

                val mHField = clazz.getDeclaredField("mH").apply {
                    isAccessible = true
                }
                val handler = mHField[invoke] as Handler
                Log.d(TAG, "call getCurrentActivityThread success.:${handler}")
            } catch (e: Throwable) {
                Log.e(TAG, "Exception:$e")
            }
        }

        /**
         * 通过Joor反射库获取ActivityThread的mH变量
         */
        findViewById<TextView>(R.id.btn_test_activityThread_joor).setOnClickListener {
            try {
                val mainHandler =
                    Reflect.onClass("android.app.ActivityThread").field("sCurrentActivityThread")
                        .field("mH").get<Handler>()
                Log.d(TAG, " get mainHandler success:${mainHandler}")

            } catch (e: Throwable) {
                e.printStackTrace()
                Log.e(TAG, "Exception:$e")
            }
        }
    }
}