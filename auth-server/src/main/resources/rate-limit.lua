---这里是基于redis数据已经初始化，需要初始化curr_permits  max_permits  rate  api_id 这几个字段的信息。不然获取不到 初始化可以在web端进行
local key = KEYS[1]
local permits = ARGV[1]
local curr_mill_second = ARGV[2]
local appId = ARGV[3]

local rate_limit_info = redis.pcall("HMGET", key, "last_mill_second", "curr_permits", "max_permits", "rate", "api_id")
local last_mill_second = rate_limit_info[1]
local curr_permits = tonumber(rate_limit_info[2])
local max_permits = tonumber(rate_limit_info[3])
local rate = rate_limit_info[4]
local api_id = rate_limit_info[5]
--- 权限验证，通过key 获取到app_id 看和传进来的是否一致
if type(api_id) == 'boolean' or api_id == nil or api_id ~= appId then
  ---  验证失败
    return 0
end


local local_curr_permits = max_permits;
---不是第一次请求
if (type(last_mill_second) ~= 'boolean'  and last_mill_second ~= nil) then
    ---时间间隔 * 速率 = 需要放入多少令牌
    local reverse_permits = math.floor(((curr_mill_second - last_mill_second) / 1000) * rate)
    --- 当前当前还剩多少令牌，从redis获取到令牌+需要放进去的令牌数
    local expect_curr_permits = reverse_permits + curr_permits;
    --- 当前令牌数和最大令牌数比较 取小值
    local_curr_permits = math.min(expect_curr_permits, max_permits);
    --- 如果需要放入的令牌数大于零 将时间设为当前，因为需要写令牌
    if (reverse_permits > 0) then
        redis.pcall("HSET", key, "last_mill_second", curr_mill_second)
    end
else---第一次请求
    redis.pcall("HSET", key, "last_mill_second", curr_mill_second)
end


local result = -1
---里面还有令牌
if (local_curr_permits - permits >= 0) then
    result = 1
    redis.pcall("HSET", key, "curr_permits", local_curr_permits - permits)
else
    redis.pcall("HSET", key, "curr_permits", local_curr_permits)
end

return result