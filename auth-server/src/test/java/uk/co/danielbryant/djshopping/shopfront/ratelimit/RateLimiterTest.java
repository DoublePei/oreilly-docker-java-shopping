package uk.co.danielbryant.djshopping.shopfront.ratelimit;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;

public class RateLimiterTest {

    @Test
    public void rateLimit() {

        RateLimiter rateLimiter = RateLimiter.create(1);

        for (int i = 1; i <= 1; i++) {
            double acquire = rateLimiter.acquire(i);
            System.out.println("acquire: " + i + " waiting for time:" + acquire);
        }


    }

}