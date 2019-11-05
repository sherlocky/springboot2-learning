package com.sherlocky.springboot2.validation.controller;

import com.sherlocky.springboot2.validation.domain.ServerResponse;
import com.sherlocky.springboot2.validation.domain.UserBO;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 简单接口 -- 仅作为参数校验演示用
 * @author: zhangcx
 * @date: 2019/11/5 15:26
 * @since:
 */
@RestController
public class ValidationController {
    /**
     * <p>
     *   <code>@Valid</code>表明需要 spring 对 UserBO 参数进行校验，
     *   而校验的信息会存放到其后的 BindingResult 中。【注意，必须相邻】
     * </p>
     *   如果有多个参数需要校验，形式可以如下：
     *   <p>
     *   <code>
     *   demo(@Valid UserBO ub, BindingResult bindingResult，@Valid Bar bar, BindingResult barBindingResult);
     *   </code>
     *   </p>
     *   即一个校验对象对应一个校验结果。
     *
     *  <p>还可以使用 @Validated ，具体区别可参考 README.md</p>
     * @param ub
     * @param bindingResult
     * @return
     */
    @RequestMapping("/register")
    public ServerResponse register(@RequestBody @Valid UserBO ub, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errorList = bindingResult.getFieldErrors();
            // list不为空
            if (!CollectionUtils.isEmpty(errorList)) {
                return ServerResponse.illegalArgument(errorList.get(0).getDefaultMessage());
            }
        }
        // 其他一堆校验过程,调用service ...
        return ServerResponse.success();
    }
}
