package com.bert.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.create

/**
 * todo 待增加类描述
 *
 * @author cheng.wang
 * @date 2023年12月14日 18:44
 */

interface GreetingPluginExtension2 {
    val message: Property<String>
    val greeter: Property<String>
}

class GreetingPlugin3 : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create<GreetingPluginExtension2>("greeting2")
        project.task("hello3") {
            doLast {
                println("${extension.message.get()}  \n " +
                        "from ${extension.greeter.get()}")
            }
        }
    }

}