package uk.co.danielbryant.djshopping.shopfront.handler;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import uk.co.danielbryant.djshopping.shopfront.entiy.JsonResult;
import uk.co.danielbryant.djshopping.shopfront.entiy.ResultCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class AuthInterceptor  implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    //接受两个参数
        String username = (String)request.getParameter("username");
        String password = (String)request.getParameter("password");
        System.out.println(username+":"+password);
    //如果username = admin并且password = 123时，返回SC_OK，即http状态码200.
        if (username != null && password != null) {
            if (username.equals("admin") && password.equals("123")) {
                response.reset();
                response.setStatus(HttpServletResponse.SC_OK);

                PrintWriter pw = response.getWriter();
                pw.flush();
                pw.close();

                return false;
            }
        }
        //否则，返回SC_FORBIDDEN，即http状态码403.
                response.reset();
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("applcation/json;chartset=UTF-8");
        PrintWriter pw = response.getWriter();

        pw.write(JSONObject.toJSONString(new JsonResult(ResultCode.NOT_LOGIN.getCode(), "forbidden", "auth failure.")));
        pw.flush();
        pw.close();

        return false;    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
