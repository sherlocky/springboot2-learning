package com.sherlocky.springboot2.shirojwt.shiro.config;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * <p>实现自定义无状态 WebSubjectFactory</p>
 * 对于无状态的TOKEN不创建session 这里都不使用session
 */
public class StatelessWebSubjectFactory extends DefaultWebSubjectFactory {
    @Override
    public Subject createSubject(SubjectContext context) {
        // 这里都不创建session
        context.setSessionCreationEnabled(Boolean.FALSE);
        return super.createSubject(context);
    }
}
