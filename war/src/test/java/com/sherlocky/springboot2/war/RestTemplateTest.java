package com.sherlocky.springboot2.war;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestTemplateTest {
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testGet() {
        String uri = "https://halo.sherlocky.com/api/content/options/comment";
        System.out.println(get(uri));
    }

    /**
     * restTemplate get请求
     * @param url
     * @return 结果
     */
    protected String get(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "XXX");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                // TODO 这样直接error，是否太简单粗暴了？
                return true;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                // response.getStatusText()
                // response.getRawStatusCode()
            }
        });
        ResponseEntity<String> resultEntity = null;
        try {
            resultEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return resultEntity.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * restTemplate post请求
     *
     * @param url
     * @param params       post请求参数
     * @return 结果
     * @throws URISyntaxException
     */
    protected String post(String url, Map<String, Object> params) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "XXX");
        MultiValueMap<String, Object> reqParam = new LinkedMultiValueMap<String, Object>();
        params.forEach((k, v) -> {
            reqParam.add(k, v);
        });
        RequestEntity request = new RequestEntity(reqParam, headers, HttpMethod.POST, new URI(url));
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return true;

            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                //
            }
        });
        Long startTime = System.currentTimeMillis();
        ResponseEntity<String> resultEntity = null;
        try {
            resultEntity = restTemplate.exchange(request, new ParameterizedTypeReference<String>() {
            });
            return resultEntity.getBody();
        } catch (Exception e) {
            return null;
        }
    }
}

