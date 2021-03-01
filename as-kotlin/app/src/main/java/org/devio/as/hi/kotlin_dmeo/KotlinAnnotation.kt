package org.devio.`as`.hi.kotlin_dmeo

fun main() {
    fire(ApiGetArticles())
}

//和一般的声明很类似，只是在class前面加上了annotation修饰符
@Target(AnnotationTarget.CLASS)
annotation class ApiDoc(val value: String)

/**
 * 自定义注解实现API调用时的请求方法检查
 */

@ApiDoc("修饰类")
class Box {
    //    @ApiDoc("修饰字段")
    val size = 6

    //    @ApiDoc("修饰方法")
    fun test() {

    }
}

public enum class Method {
    GET,
    POST
}

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class HttpMethod(val method: Method)

interface Api {
    val name: String
    val version: String
        get() = "1.0"
}

@HttpMethod(Method.POST)
class ApiGetArticles : Api {
    override val name: String
        get() = "/api.articles"
}

fun fire(api: Api) {
    val annotations = api.javaClass.annotations
    val method = annotations.find { it is HttpMethod } as? HttpMethod
    println("通过注解得知该接口需需要通过：${method?.method} 方式请求")

}