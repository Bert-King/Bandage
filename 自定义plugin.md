
## 自定义插件

[官方文档](https://docs.gradle.org/7.5.1/userguide/custom_plugins.html)


根据官方的介绍，主要有3种包装插件的方式：

1. Build script
2. buildSrc project
3. standalone project

参考自：[Packaging a plugin](https://docs.gradle.org/7.5.1/userguide/custom_plugins.html)


---

这里我们主要介绍第二种方式，即buildSrc project。

#### 1.创建buildSrc项目
>首先，在项目根目录下创建buildSrc目录，然后创建build.gradl。
>可以使用以下两种方式：
> * 选择File -> New Module-> Java or Kotlin Library
> * 选择File -> New Module-> Android Library (不推荐)
>   * 多了一堆无用的test目录文件
>   * 需要修改build.gradle文件的内容。
> 
按照以上的操作，大家很可能遇到一个问题：
>'buildSrc' cannot be used as a project name as it is a reserved name.

>解决办法：找到settings.gradle文件，将buildSrc的注释去掉。


为了方便，我们考虑使用KSL的方式
>在Android Studio中的build.gradle.kts文件中，plugins { kotlin-dsl }的作用是启用Kotlin DSL。 
> Kotlin DSL是Gradle构建文件的一种替代语法，使用Kotlin编程语言编写构建脚本。
> 相比于Groovy DSL，Kotlin DSL提供了更好的代码完整性和错误提示，以及更简洁和可读的代码结构。  
> 通过在build.gradle.kts文件中添加plugins { kotlin-dsl }，您可以使用Kotlin编写构建脚本，而不是使用Groovy。
> 这将使您能够利用Kotlin的特性和语法优势来编写构建文件，提高构建文件的可读性和可维护性。

---
不一定需要在build.gradle.kts文件中显式声明plugins {.kotlin-dsl}。
实际上，kotlin-dsl插件是默认启用的，如果你使用的是支持Kotlin DSL的Gradle版本。  
从Gradle 7.0开始，默认情况下，Gradle会假设你正在使用Kotlin DSL，并且会自动加载kotlin-dsl插件。
因此，如果你使用的是Gradle 7.0或更高版本，并且使用.kt文件作为构建脚本，则不需要在build.gradle.kts文件中声明plugins { .kotlin-dsl }。 

但是，如果你使用的是早期版本的Gradle或是在build.gradle.kts之前使用了Groovy DSL写的build.gradle文件，
则需要在build.gradle.kts中显式声明plugins { .kotlin-dsl }以启用Kotlin DSL。  
总之，是否需要在build.gradle.kts中声明plugins { .kotlin-dsl }取决于你使用的Gradle版本和构建脚本的类型。
在Gradle 7.0及更高版本中，如果你使用Kotlin DSL，则无需显式声明。

