package uk.co.danielbryant.djshopping.shopfront.entiy;

import lombok.val;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.data.redis.core.script.RedisScript;

/**
 * 两种生成模式的一种
 * 废弃了
 */
@Deprecated
public class RedisReteLimitScript implements RedisScript<String> {

    private static final String SCRIPT = "local val=KEYS[1]..' '..redis.call('get',ARGV[1]) return val";
    @Override
    public String getSha1() {
        return DigestUtils.sha1DigestAsHex(SCRIPT);
    }

    @Override
    public Class<String> getResultType() {
        return String.class;
    }

    @Override
    public String getScriptAsString() {
        return SCRIPT;
    }
}
