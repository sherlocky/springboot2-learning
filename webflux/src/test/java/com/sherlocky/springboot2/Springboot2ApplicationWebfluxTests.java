package com.sherlocky.springboot2;

import com.sherlocky.springboot2.dao.UserRepository;
import com.sherlocky.springboot2.web.mvc.MyMVCController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MyMVCController.class)
public class Springboot2ApplicationWebfluxTests {
    //mvc 环境对象
    @Autowired
    private MockMvc mvc;

    // 其实应该注入相关Service
    @MockBean
    private UserRepository userRepository;

    @Test
    public void testListUsers() throws Exception {
        // perform：执行一个RequestBuilder请求，会自动执行SpringMVC的流程并映射到相应的控制器执行处理；
        String responseContext = this.mvc.perform(get("/mvc/users").contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk()) // andExpect：添加ResultMatcher验证规则，验证控制器执行完成后结果是否正确；
                // andDo：添加ResultHandler结果处理器，比如调试时打印结果到控制台；
                .andReturn().getResponse().getContentAsString()
                // andReturn：最后返回相应的MvcResult；然后进行自定义验证/进行下一步的异步处理；
        ;
        System.out.println(responseContext);
    }
}