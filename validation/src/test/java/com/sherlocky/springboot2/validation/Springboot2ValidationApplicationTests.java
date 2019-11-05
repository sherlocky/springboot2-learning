package com.sherlocky.springboot2.validation;

import com.alibaba.fastjson.JSON;
import com.sherlocky.springboot2.validation.domain.ServerResponse;
import com.sherlocky.springboot2.validation.domain.UserBO;
import com.sherlocky.springboot2.validation.domain.UserRelationBO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class Springboot2ValidationApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void registerNoUser() throws Exception {
        MvcResult mr = this.mvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json(new UserBO()))
        ).andExpect(status().isOk())
                .andReturn();
        System.out.println(mr.getResponse().getContentAsString());
    }

    @Test
    public void registerFullUser() throws Exception {
        MvcResult mr = this.mvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json(fullUser()))
        ).andExpect(status().isOk())
                .andExpect(content().json(json(ServerResponse.success())))
                .andReturn();
        System.out.println(mr.getResponse().getContentAsString());
    }

    private UserBO fullUser() {
        return new UserBO()
                .setName("zhangsan")
                .setPhone("13400001234")
                .setEmail("xxx@yy.com");
    }

    private UserBO fullUserWithRel() {
        return new UserBO()
                .setName("zhangsan")
                .setPhone("13400001234")
                .setEmail("xxx@yy.com")
                .setRelation(new UserRelationBO("zhangyi", "lisi"));
    }

    private String json(Object obj) {
        return JSON.toJSONString(obj);
    }
}