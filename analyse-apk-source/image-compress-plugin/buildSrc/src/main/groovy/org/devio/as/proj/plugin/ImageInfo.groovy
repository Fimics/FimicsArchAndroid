package org.devio.as.proj.plugin

class ImageInfo {
    ImageInfo(String name, String path, String fileSize, String compressedSize, String md5) {
        this.name = name
        this.path = path
        this.fileSize = fileSize
        this.compressedSize = compressedSize
        this.md5 = md5
    }
    String name  //图片名称
    String path  //图片路径
    String fileSize //压缩前大小kb,mb
    String compressedSize//压缩后的大小kb，mb
    String md5 //md5校验，用于比对两个文件是否是同一个文件，防止重复压缩使用的
}