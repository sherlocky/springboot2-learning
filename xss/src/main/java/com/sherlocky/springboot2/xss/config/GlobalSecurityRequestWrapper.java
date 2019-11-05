package com.sherlocky.springboot2.xss.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.HTMLEntityCodec;
import org.owasp.esapi.codecs.MySQLCodec;
import org.owasp.esapi.codecs.PercentCodec;
import org.owasp.esapi.reference.DefaultEncoder;

import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 定义自己的Wrapper，并重写相关方法。
 * <p>实现 XSS 过滤</p>
 * <p>HttpServletRequestWrapper 继承 ServletRequestWrapper 并实现了 HttpServletRequest 接口</p>
 *
 * @author: zhangcx
 * @date: 2019/11/4 16:32
 * @since:
 */
@Slf4j
public class GlobalSecurityRequestWrapper extends HttpServletRequestWrapper {

    //将读取的流内容存储在 body 字符串中
    private final String body;

    //定义Pattern数组，用于正则匹配，可添加其他pattern规则至此
    private static Pattern[] patterns = new Pattern[]{
            // Script fragments
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            // src='...'
            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // lonely script tags
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // eval(...)
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // expression(...)
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // javascript:...
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            // vbscript:...
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            // onload(...)=...
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            Pattern.compile("<iframe>(.*?)</iframe>", Pattern.CASE_INSENSITIVE),

            Pattern.compile("</iframe>", Pattern.CASE_INSENSITIVE),

            Pattern.compile("<iframe(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
            Pattern.compile("oninput(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("onerror(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("onclick(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("confirm(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("onfocus(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("alert(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("onabort(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("onblur(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            Pattern.compile("onchange(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            Pattern.compile("ondblclick(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            Pattern.compile("onkeydown(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            Pattern.compile("onkeypress(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            Pattern.compile("onkeyup(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            Pattern.compile("onmousedown(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            Pattern.compile("onmousemove(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            Pattern.compile("onmouseout(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            Pattern.compile("onmouseover(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            Pattern.compile("onmouseup(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            Pattern.compile("onreset(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            Pattern.compile("onresize(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            Pattern.compile("onselect(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            Pattern.compile("onsubmit(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            Pattern.compile("onunload(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            // add other patterns here
    };


    /**
     * 通过构造函数装饰 HttpServletRequest，同时将流内容存储在 body 字符串中
     */
    public GlobalSecurityRequestWrapper(HttpServletRequest servletRequest) throws IOException {
        super(servletRequest);

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = servletRequest.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        //将requestBody内容以字符串形式存储在变量body中
        body = stringBuilder.toString();
        log.info("过滤和替换前，requestBody 内容为: 【{}】", body);
    }

    /**
     * 将 body 字符串重新转换为ServletInputStream, 用于request.inputStream 读取流
     *
     * @return
     * @throws IOException
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        String encodedBody = stripXSSRequestBody(body);
        log.info("过滤和替换后，requestBody 内容为: 【{}】", encodedBody);

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(encodedBody.getBytes());
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
        return servletInputStream;
    }


    @Override
    public Part getPart(String name) throws IOException, ServletException {
        return super.getPart(name);
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return super.getParts();
    }

    /**
     * 调用该方法，可以多次获取 requestBody 内容
     *
     * @return
     */
    public String getBody() {
        return this.body;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    /**
     * 获取多个请求参数，多个参数返回 String[] 数组
     *
     * @param parameter
     * @return
     */
    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);

        if (values == null) {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = stripXSS(values[i]);
        }

        return encodedValues;
    }

    /**
     * 获取单个请求参数
     *
     * @param parameter
     * @return
     */
    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        return stripXSS(value);
    }

    /**
     * 获取请求头信息
     *
     * @param name
     * @return
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return stripXSS(value);
    }

    /**
     * 标准过滤和替换方法
     * <p>解析带有转义字符的json数据会出异常</p>
     *
     * @param value
     * @return
     */
    private String stripXSS(String value) {
        if (value != null) {
            /**
             * ESAPI 最简单的使用方式，主要防止 encoded 的代码进行 XSS 攻击。
             * <p>用在 GET 请求中没有问题，但如果是 POST 请求，
             *      requestBody 中数据有 "", 会被替换掉，这样就破坏了json 的结构，导致后续解析出错。</p>
             * {@link #stripXSSRequestBody(String)}
             */
            value = ESAPI.encoder().canonicalize(value, false, false);
            value = patternReplace(value);
        }
        return value;
    }

    /**
     * 防Sql注入，多用于带参数查询
     *
     * @param value
     * @return
     */
    private String stripXSSSql(String value) {
        Codec MYSQL_CODEC = new MySQLCodec(MySQLCodec.Mode.STANDARD);
        if (value != null) {
            // 使用 ESAPI 避免 encoded 的代码攻击
            value = ESAPI.encoder().canonicalize(value, false, false);
            value = ESAPI.encoder().encodeForSQL(MYSQL_CODEC, value);
        }
        return value;
    }


    /**
     * 请求体处理，多用于json数据，自定义encoder，排除掉javascriptcodec
     * <p>{@link DefaultEncoder} 中 javaScriptCodec 是按照 JavaScript 标准将 "" 替换成 "", 所以我们需要做定制改变。</p>
     *
     * @see #getInputStream()
     * @param value
     * @return
     */
    private String stripXSSRequestBody(String value) {
        // 通过 DefaultEncoder 带参数构造器构造自己的 encoder
        if (value != null) {
            List codecs = new ArrayList(4);
            codecs.add(new HTMLEntityCodec());
            codecs.add(new PercentCodec());
            DefaultEncoder defaultEncoder = new DefaultEncoder(Arrays.asList("HTMLEntityCodec", "PercentCodec"));
            // 使用 ESAPI 避免 encoded 的代码攻击
            value = defaultEncoder.canonicalize(value, false, false);
            value = patternReplace(value);
        }
        return value;
    }

    /**
     * 根据 Pattern 替换字符
     */
    private String patternReplace(String value) {
        if (StringUtils.isNotBlank(value)) {
            // 避免null
            value = value.replaceAll("\0", "");

            // 根据Pattern匹配到的字符，做""替换
            for (Pattern scriptPattern : patterns) {
                value = scriptPattern.matcher(value).replaceAll("");
            }
        }
        return value;
    }

}