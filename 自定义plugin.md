
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

