## GradleTest
集成Tinker的打包校验、备份和维护打包历史的Gradle脚本

以module形式导入TinkerLib到你的项目中，在project的build.gradle加入以下代码：
```groovy
apply from: "${rootDir}/TinkerLib/TinkerLib.gradle"
```

具体各参数配置可查看TinkerLib/TinkerLib.gradle文件


博客地址: [android 打造自己的gradle构建脚本(以集成Tinker为例)](https://blog.csdn.net/anyfive/article/details/80160279)
