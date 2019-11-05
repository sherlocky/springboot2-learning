# XSS 
Cross-site scripting（XSS）是Web应用程序中常见的一种计算机安全漏洞，XSS 使攻击者能够将客户端脚本注入其他用户查看的网页中。攻击者可能会使用跨站点脚本漏洞绕过访问控制，例如同源策略。截至2007年，Symantec（赛门铁克） 在网站上执行的跨站脚本占据了所有安全漏洞的 84％ 左右。2017年，XSS 仍被视为主要威胁载体，XSS 影响的范围从轻微的麻烦到重大的安全风险，影响范围的大小，取决于易受攻击的站点处理数据的敏感性方式以及站点所有者实施对数据处理的安全策略。

# 解决方案
XSS 问题需要多种方案的配合使用：

- 1.前端做表单数据合法性校验

- 2.后端做数据过滤与替换

- 3.持久层数据编码规范，比如使用 Mybatis。

# 针对后端过滤，Springboot 常见方法

## Spring AOP
使用 Spring AOP 横切所有 API 入口，貌似可以很轻松的实现，但是 RESTful API 设计并不是统一的入参格式，有 GET 请求的 RequestParam 的入参，也有 POST 请求RequestBody的入参，不同的入参很难进行统一处理，所以这并不是很好的方式。

## HttpMessageConverter
请求的 JSON 数据都要过``HttpMessageConverter``进行转换，通常我们可以通过添加 ``MappingJackson2HttpMessageConverter``并重写``readInternal``方法：

```java
@Override
protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
    return super.readInternal(clazz, inputMessage);
}
```
获取到转换过后的 Java 对象后对当前对象做处理，但这种方式没有办法处理 GET 请求，所以也不是一个很好的方案。

## Filter
通过``Filter``可以过滤 HTTP Request，我们可以拿到请求的所有信息，所以我们可以在这里大做文章。

我们有两种方式自定义我们的``Filter``

- 1.实现``javax.servlet.Filter``接口
- 2.Spring 环境下继承``org.springframework.web.filter.OncePerRequestFilter``抽象类

这里采用第 2 种实现：

- 1.先实现一个[``GlobalSecurityFilter``](src/main/java/com/sherlocky/springboot2/xss/config/GlobalSecurityFilter.java)
- 2.然后注册[``Filter``](rc/main/java/com/sherlocky/springboot2/xss/Springboot2XssApplication.java)

这种方案貌似可以很简单粗暴的解决，但会有以下几个问题：

- 抛出异常，没有统一 RESTful 消息返回格式，抛出异常后导致流程不可达
- 调用``request.getInputStream()``读取流，只能读取一次，调用责任链后续 filter 会导致``request.getInputStream()``内容为空，即便这是``Filter``责任链中的最后一个 filter，程序运行到``HttpMessageConverter``时也会抛出异常。
- HtmlUtils.htmlEscape(...) 可替换的内容有限，不够丰富。


我们还需要通过 HttpServletRequestWrapper 完成流的多次读取。

- 3.实现一个[``GlobalSecurityRequestWrapper``](rc/main/java/com/sherlocky/springboot2/xss/config/GlobalSecurityRequestWrapper.java)
