package com.panda912.bandage.sample

import android.app.Application
import android.content.Context
import com.panda912.bandage.Bandage
import com.panda912.bandage.internal.data.DynamicBandageData
import com.panda912.bandage.sample.bandage.BandageConfig
import me.weishu.reflection.Reflection

/**
 * Created by panda on 2021/12/7 14:38
 */
class SampleApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Reflection.unseal(base)
    }

    override fun onCreate() {
        super.onCreate()
        Bandage.install(BandageConfig(this))
        createDynamicBandageData()
    }

    private fun createDynamicBandageData() {
        // click button 'crash 10 / 0'
        val stackOfArithmeticException =
            "at com.panda912.bandage.sample.MainActivity.onCreate\$lambda-0(MainActivity.kt:14)\n" +
                    "at com.panda912.bandage.sample.MainActivity.\$r8\$lambda\$DDlE6wDlIjCyOonj-NgrVOem29Q(Unknown Source:0)\n" +
                    "at com.panda912.bandage.sample.MainActivity\$\$ExternalSyntheticLambda0.onClick(Unknown Source:0)\n" +
                    "at android.view.View.performClick(View.java:7506)"
        val dataOfArithmeticException = DynamicBandageData(
            process = "all",
            exceptionMatch = DynamicBandageData.ExceptionMatch("java.lang.ArithmeticException", ""),
            stacks = stackOfArithmeticException.lines()
                .map { it.trim().replace(Regex("(at |\\([^\\)]*\\))"), "") },
            causes = null,
            closeCurActivity = true,
            loadPatch = false,
        )

        val stackOfNullPointerException =
            " at com.panda912.bandage.sample.MainActivity.onCreate\$lambda-1(MainActivity.kt:19)\n" +
                    "        at com.panda912.bandage.sample.MainActivity.\$r8\$lambda\$1D6Egf26udScyLg1CMAP2WCQtC0(Unknown Source:0)\n" +
                    "        at com.panda912.bandage.sample.MainActivity\$\$ExternalSyntheticLambda1.onClick(Unknown Source:2)\n" +
                    "        at android.view.View.performClick(View.java:7753)"

        val dataOfNullPointerException = DynamicBandageData(
            process = "all",
            exceptionMatch = DynamicBandageData.ExceptionMatch(
                "java.lang.NullPointerException",
                ""
            ),
            stacks = stackOfNullPointerException.lines()
                .map { it.trim().replace(Regex("(at |\\([^\\)]*\\))"), "") },
            causes = null,
            closeCurActivity = false,
            loadPatch = false,
        )


        val list =
            arrayListOf<DynamicBandageData>(dataOfNullPointerException, dataOfArithmeticException)



        Bandage.addDynamicBandageData(list)
    }
}