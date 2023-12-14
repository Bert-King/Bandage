package com.bert.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.create

/**
 * 做一个可配置的插件
 *
 * https://docs.gradle.org/7.5.1/userguide/custom_plugins.html#sec:getting_input_from_the_build
 *
 * @author cheng.wang
 * @date 2023年12月14日 18:24
 */
class GreetingPlugin2 : Plugin<Project> {
    override fun apply(target: Project) {
        // Add the 'greeting' extension object
        val extension = target.extensions.create<GreetingPluginExtension>("greeting")
        // Add a task that uses configuration from the extension object
        target.task("hello2") {
            doLast {
                println(extension.message.get())
            }
        }
    }
}


abstract class GreetingPluginExtension {
    abstract val message: Property<String>

    init {
        message.convention("Hello from GreetingPlugin")
    }
}
