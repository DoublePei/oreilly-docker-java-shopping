package uk.co.danielbryant.djshopping.shopfront.handler;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import uk.co.danielbryant.djshopping.shopfront.entiy.JsonResult;
import uk.co.danielbryant.djshopping.shopfront.entiy.ResultCode;
import uk.co.danielbryant.djshopping.shopfront.entiy.Token;
import uk.co.danielbryant.djshopping.shopfront.ratelimit.RateLimiter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthInterceptor implements HandlerInterceptor {

    private RedisTemplate redisTemplate;

    public AuthInterceptor(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //接受两个参数
        String key = request.getParameter("key");
        String apiId = request.getParameter("api_id");
        if (key != null || apiId != null) {
            RateLimiter rateLimiter = new RateLimiter(redisTemplate);
            Token token = rateLimiter.acquireToken(key, apiId);
            System.out.println(token);
            if (token.isAuthFaild()) {
                getFaildResponse(response, ResultCode.NOT_LOGIN.getCode(), "认证错误,请正确使用appCode...");
                return false;
            }
            if (token.isPass() || token.isAccessRedisFail()) {
                getPassResponse(response);
                return false;
            }
            if (token.isConfuse()) {
                getFaildResponse(response, HttpStatus.TOO_MANY_REQUESTS.value(), "too many request...");
                return false;
            }
        }
        //否则，返回SC_FORBIDDEN，即http状态码403.
        getFaildResponse(response, HttpStatus.FORBIDDEN.value(), "请求参数错误,请正确使用appCode和ApiId...");

        return false;
    }

    private void getPassResponse(HttpServletResponse response) throws IOException {
        response.reset();
        response.setStatus(HttpStatus.OK.value());
        PrintWriter pw = response.getWriter();
        pw.flush();
        pw.close();
    }

    private void getFaildResponse(HttpServletResponse response, int code, String message) throws IOException {
        response.reset();
        response.setStatus(code);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("applcation/json;chartset=UTF-8");
        PrintWriter pw = response.getWriter();

        pw.write(JSONObject.toJSONString(new JsonResult(code, "", message)));
        pw.flush();
        pw.close();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
