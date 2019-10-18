package com.sherlocky.springboot2.shirojwt.shiro.provider.impl;

import com.google.common.collect.Lists;
import com.sherlocky.springboot2.shirojwt.shiro.provider.ShiroFilterRulesProvider;
import com.sherlocky.springboot2.shirojwt.shiro.rule.RolePermRule;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
public class ShiroFilterRulesProviderImpl implements ShiroFilterRulesProvider {
    /*
    @Autowired
    private AuthResourceMapper authResourceMapper;
    */
    @Override
    public List<RolePermRule> loadRolePermRules() {
        //return authResourceMapper.selectRoleRules();
        //TODO
        return Lists.newArrayList();
    }
}
