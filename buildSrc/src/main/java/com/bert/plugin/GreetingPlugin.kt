package com.bert.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 简单搞一个插件
 *
 * @author cheng.wang
 * @date 2023年12月14日 14:33
 */
class GreetingPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.task("hello") {
            doLast {
                println("Hello from the GreetingPlugin")
            }
        }
    }
}