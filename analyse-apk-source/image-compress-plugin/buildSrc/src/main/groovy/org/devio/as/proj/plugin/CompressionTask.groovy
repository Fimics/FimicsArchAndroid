package org.devio.as.proj.plugin

import com.android.build.gradle.BaseExtension
import com.tinify.Tinify
import groovy.json.JsonException
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.impldep.org.sonatype.aether.util.ChecksumUtils
import sun.security.provider.MD5

import java.text.DecimalFormat
import java.util.concurrent.*

class CompressionTask extends DefaultTask {
    CompressionExt imgCompression
    ExecutorService executorService

    CompressionTask() {
        imgCompression = project.imgCompression
        executorService = Executors.newCachedThreadPool()
    }

    /**
     * 对于gradle task,它是没有默认的入口方法的。如果我们想给他增加一个入口方法，那我们可以随意定义一个方法，
     * 在方法上标记@taskAction注解，当我们执行任务时，他就会自动的执行这个方法了。
     *
     * 我们这个任务在运行时还需要拿到在gradle中配置的compression对象，此时咱们通过project.compression就可以拿到了。
     * 接下来是核心逻辑的编写了。
     */
    @TaskAction
    void run() {
        //方法的开头我们要打一个日志 -----这样子的话别人使用我们这该插件的时候，也可以知道到底有米有在运行
        println("compressionTask run")

        //1. 参数校验---把apikey传递到tinypng用于验证权限
        if (imgCompression.apiKey == null) {
            println("you must set apiKey for CompressionTask")
        }
        try {
            Tinify.setKey(imgCompression.apiKey)
            Tinify.validate()
        } catch (Exception ex) {
            ex.printStackTrace()
            println("apiKey is not available!!!")
        }

        //2.查找图片资源所在的目录----资源目录，源码目录，同志们可层还记得实际上我们是可以在gradle文件中通过sourceset来配置的
        //我们要拿到资源目录，就先要拿到android这个extension扩展对象。我们可以通过findByType。
        //application下面的android 它的实现类是BaseAppModuleExtension
        //但是library里面的android extension 它的实现类是LibraryExtension,
        //但是他们都继承自BaseExtension
        def androidExt = project.extensions.findByType(BaseExtension.class)
        //3.获取main sourceSet---通过androidExt拿到的sourceset它是一个集合，那是因为我们在gradle
        //通过souceset配置资源目录的时候，是可以配置多个的，比如main就代表的是我们这个main 资源目录，test代表的是test 这个目录，还有android test
        //我们这里需要那到底自然是main source set
        def sourcesSet = androidExt.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        //4.获取main/res目录---这里我们取first 就是了
        def resDir = sourcesSet.res.srcDirs.first()

        //5. 加载已压缩图片的缓存记录----因为我们会对已经压缩的图片，把压缩前后的体积，图片md5,图片路径等信息，写入本地文件防止 二次压缩
        //我们来写个方法用于加载这个json文件
        def compressedList = loadCompressedResources()

        //6. 过滤res目录下的文件夹名称，只取drawable- 和 mipmap-的
        //并过滤出 有效的文件，方便下面使用多线程压缩，做多线程同步
        def drawablePattern = "drawable-[a-z]*"
        def mipmapPattern = "mipmap-[a-z]*"
        def validFiles = new ArrayList<File>()
        //对res目录下所有文件夹进行遍历过滤，、
        resDir.listFiles().each { dir ->
            if (dir.name.matches(drawablePattern) || dir.name.matches(mipmapPattern)) {
                dir.listFiles().each { file ->
                    //并对每一个图片文件进行校验，本次是否可以压缩
                    if (validateFile(file, compressedList)) {
                        validFiles.add(file)
                    }
                }
            }
        }

        //7.多线程压缩与 结果同步----多线程的同步，我们有使用那些手段啊？
        //最常用的就是CountDownLatch，光知道这一个事不过的，还有很多昂。比如sephmore,信号量 cyclebarrie
        CountDownLatch latch = new CountDownLatch(validFiles.size())

        //遍历每一个文件开启线程对图片进行压缩，并收集压缩结果信息
        validFiles.each { file ->
            executorService.execute(new Runnable() {
                @Override
                void run() {
                    def ret = compress(file)
                    compressedList.add(ret)
                    latch.countDown()
                    println("remain count:" + latch.count)
                }
            })
        }

        //在做多线程同步的时候，我们要小心一点，万一上面发生了意外，这里的代码很可能之心不到，所以我们给一个超时时间
        //不至于发生意外了永久等待下去，这就体现的是逻辑严谨，不至于出现意外了，程序永远结束不了。对不对
        latch.await(10, TimeUnit.MINUTES)
        //8.压缩结果写入文件
        writeCompressedResources(compressedList)
        println("compressionTask finished")
    }

    //在gradle中解析一个json文件相当的简单
    def loadCompressedResources() {
        def compressedFile = new File(project.projectDir, "compressed-resource.json")
        if (!compressedFile.exists()) {
            compressedFile.createNewFile()
        } else {
            try {
                //我们只需要通过JsonSlurper的parse方法就可以自动的把一个文件的内容解析并序列化出来
                //这个文件里面的对象咱们需要创建一个类似于javabean 的class.
                // 就是这个 ImageInfo。这个也非常简单
                //这个咱事先已经备课创建好了
                def ret = new JsonSlurper().parse(compressedFile, "utf-8")
                if (ret instanceof List<ImageInfo>) {
                    return ret
                }
            } catch (JsonException ex) {
                //ex.printStackTrace()
            }
        }
        //那如果解析失败或者文件不存在，那我们也返回一个集合，
        //由于待会我们会使用多线程并发压缩，所以我们需要考虑多线程的同步问题
        //关于线程池的相关技术哈，比如线程池线程复用原理，线程池状态设计原理，线程同步，线程安全等等也是常考de。
        //这些内容，咱们体系课里面也是相当全面的
        return new CopyOnWriteArrayList<ImageInfo>()
    }

    def validateFile(File file, List<ImageInfo> compressedList) {
        //因为我们会把每一个目录下的文件压缩后写入到该目录下面的一个compressed文件架下面
        //而不是直接覆盖原图，最好不要直接覆盖原图，即便压缩效果你觉得不满意，那你还有回旋的余地
        //当然你也可以在gradle 增加一个参数，用于声明压缩后是否直接覆盖原图
        if (file.isDirectory()) {
            return false
        }
        //如果当前文件是白名单 列表里面的，则忽略
        if (imgCompression.whiteList.contains(file.name)) {
            println("skip file ${file.path} which is in white-list")
            return false
        }

        //如果当前文件.9图 也是忽略的
        if (file.name.contains('.9')) {
            println("skip file ${file.path} which is .9")
            return false
        }

        //如果当前文件的体积小于 limitsize 那也是不用压缩的
        def fileSize = file.size()
        if (fileSize < imgCompression.limitSize) {
            println("skip file ${file.path} which file size is less than limitSize: ${imgCompression.limitSize}")
            return false
        }
        //接下来，我们需要计算下，这个文件之前是否已经压缩过了
        //首先需要拿到当前文件的md5 ，
        def md5 = DigestUtils.md5Hex(file.newInputStream())
        //遍历compressedList 每一个元素，和md5进行比对，如果return的值为true，那么这个for循环结束
        def match = compressedList.any { imgInfo ->
            return imgInfo.md5 == md5
        }
        if (match) {
            println("skip file ${file.path} which has compressed")
            return false
        }
        //能够 走到最后  就说明这个文件是一个需要被压缩的图片
        return true
    }

    def compress(File file) {
        println("compress ${file.name} start")

        //我们需要把压缩后的图片存放到当前文件夹的compressed 文件夹下
        //创建目录
        def dstFileDir = new File(file.parent, "compressed")
        if (!dstFileDir.exists()) {
            dstFileDir.mkdirs()
        }
        //创建目标文件
        def destFile = new File(dstFileDir, file.name)
        if (!destFile.exists()) {
            destFile.createNewFile()
        }

        //调用Tinify api来执行压缩
        def tSource = Tinify.fromFile(file.path)
        //这一步是吧压缩结果 写入磁盘文件
        tSource.toFile(destFile.path)

        //然后组装压缩结果ImageInfo对象
        println("compress ${file.name} finished")
        return new ImageInfo(file.name, file.path, formatSize(file.size()), formatSize(destFile.size()), DigestUtils.md5Hex(file.newInputStream()))
    }


    def formatSize(long size) {
        //对文件的体积进行格式化--转换成kb,mb就行了，不会出现gb对吧
        //那这里我们可以对数值进行一个格式化，我们可以使用DecimalFormat保留一位小数
        def df = new DecimalFormat("#.0")
        if (size < 1024) {
            return "${size}b"
        } else if (size < 1024 * 1024) {
            return df.format(size * 1.0f / 1024) + "k"
        } else {
            return df.format(size * 1.0f / 1024 / 1024) + "m"
        }
    }

    def writeCompressedResources(List<ImageInfo> compressedList) {
        //首先创建json文件，我门把图片压缩信息写入到当前模块的根目录下，比如APP 模块
        // 那么图片压缩信息的结果，我们就把他写入到这个目录下
        def compressedFile = new File(project.projectDir, "compressed-resource.json")
        //在groovy 里面数据结构 以json的形式写入文件也是十分的简单
        //这里我们需要使用JsonOutput
        def jsonOutput = new JsonOutput()
        def json = jsonOutput.toJson(compressedList)
        compressedFile.write(jsonOutput.prettyPrint(json), "utf-8")

        //到这里程序就算是执行完了，但是很重要的一步同学们都很容易忽略
        //那就是程序执行的结果，我们需要输出出来，这样也方便使用方观察，对不对，
        //你写一个sdk,里面没有任何日志，错误信息，那它运行的时候就跟个黑盒一样，出了问题，别人都不知道啥情况
        println("======================")
        println("compress ${compressedList.size()} files")
        def totalFileSize = 0f
        def totalCompressedSize = 0f
        compressedList.each { imgInfo ->
            println("fileName:" + imgInfo.name + ",fileSize:" + imgInfo.fileSize + ",compressedSize:" + imgInfo.compressedSize)
            def file = new File(imgInfo.path)
            totalFileSize += file.size()
            totalCompressedSize += new File(file.parent + "/compressed", file.name).size()
        }

        def df = new DecimalFormat("#.0")
        println("total size: ${df.format(totalFileSize / 1024 / 1024)}M")
        println("compressed total size: ${df.format(totalCompressedSize / 1024 / 1024)}M")
        println("compression percent: ${df.format((1 - totalCompressedSize / totalFileSize) * 100)}%")
        println("======================")
    }
}