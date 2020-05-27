local c
-- 通过KEYS[1] 获取传入的key参数
c = redis.call('get',KEYS[1])

-- 如果调用已超过最大值，则直接返回(通过ARGV[1]获取传入的limit参数)
if c and tonumber(c) > tonumber(ARGV[1]) then
    return c;
end

-- 执行计数器累加
c = redis.call('incr',KEYS[1])
if tonumber(c) == 1 then
    -- 从第一次调用开始限流，设置对应键值的过期时间(即限流指定的时间范围 ARGV[2])
    redis.call('expire',KEYS[1],ARGV[2])
end

return c;