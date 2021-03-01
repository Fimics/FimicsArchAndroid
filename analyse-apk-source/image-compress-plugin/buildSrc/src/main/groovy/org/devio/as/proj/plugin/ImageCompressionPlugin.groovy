package org.devio.as.proj.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class ImageCompressionPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        //插件开发的第一步，就是创建一个plugin,让他实现自gradle 提供的跑plugin接口。这个泛型一般来说都是指gradle project
        //那我们这个图片压缩插件在运行时，是需要一些参数的，比如tinypng的apikey，whitelist不需要压缩的图片白名单，limitsize是指文件大小小于某个值也不需要压缩等参数。
        // 这些属性呢，我们希望是使用方来配置，那么我就可以在这里创建一个groovy  class. 相当于java bean的 对象。
        // 这个class非常简单，老师在备课的时候提前准备好了，我们把直播有限的时间了留给下面的干货，可以理解吧~
        //
        //然后咱们通过project.extenstions.create把这个扩展属性注册进去
        //第一各参数是扩展属性的名称，第二参数是扩展属性的class类，这样子的话，我们就可以在gradle中去配置它了。
        //
        //第二步，我们希望当在terminal中执行某个任务的时候，就可以触发我们这个图片压缩插件的运行，
        //所以此时我们还需要 创建一个task,一般来说所都是继承自defaultTask的。接着咱们使用project.tasks.create就可以把这个任务注册进去，
        // 第一参数是任务的名称，第二个参数的是任务的实现类。
        project.extensions.create("imgCompression", CompressionExt.class)
        project.tasks.create("imgCompress", CompressionTask.class)
    }
}