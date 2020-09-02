package uk.co.danielbryant.djshopping.shopfront.ratelimit;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import uk.co.danielbryant.djshopping.shopfront.entiy.Token;

import java.util.ArrayList;
import java.util.List;

public class RateLimiter {


    private RedisTemplate<String, Object> redisTemplate;
    private RedisScript<Long> redisScript;

    public RateLimiter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        redisScript = newRedisScript();
    }

    private RedisScript<Long> newRedisScript() {
        DefaultRedisScript<Long> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("rate-limit.lua")));
        defaultRedisScript.setResultType(Long.class);
        return defaultRedisScript;
    }

    public Token acquireToken(String key, String apiId) {
        return acquireToken(key, apiId, 1);
    }

    private Token acquireToken(String key, String apiId, Integer permits) {
        Token token = null;
        try {
            List<String> keyList = new ArrayList<>(1);
            keyList.add(key);
            //基于redis获取当前时间 避免不同服务器时间不一致
            Long execute = redisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.time());
            Long execute1 = redisTemplate.execute(redisScript, keyList, permits.toString(), execute.toString(), apiId);
            if (execute1 == 0) {
                token = Token.AUTH_FAILD;
            }
            if (execute1 == 1) {
                token = Token.PASS;
            }
            if (execute1 == -1) {
                token = Token.CONFUSE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            token = Token.ACCESS_REDIS_FAIL;
        }
        return token;
    }
}
