package org.xry.interceptors.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.xry.interceptors.exception.Exceptions.tokenValidationException;
import org.xry.interceptors.pojo.RedisConst;
import org.xry.interceptors.utils.Jwt;
import org.xry.interceptors.utils.RedisUtils.RedisCacheUtil;
import org.xry.interceptors.utils.ThreadLocalUtils.UserId;
import reactor.util.annotation.NonNull;


import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RefreshInterceptor implements HandlerInterceptor {
    private final RedisCacheUtil redisCacheUtil;

    public RefreshInterceptor(RedisCacheUtil redisCacheUtil) {
        this.redisCacheUtil = redisCacheUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response,@NonNull Object handler)throws tokenValidationException {
        //先判断是否是 OPTIONS 预检请求，若是直接放行（无需验证 token）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true; // 放行预检请求，让浏览器继续发真实请求
        }

        String token = request.getHeader("Authorization");      //1*获取请求头中的token
        if(token == null)return true;

        //解析令牌并获取核心id
        Map<String, Object> claims  = Jwt.parseToken(token);
        Integer id =(Integer) claims.get("id");


        //验证Redis中的token
        List<String> tokens = redisCacheUtil.getList(RedisConst.TOKEN + id);

        if(tokens.contains(token)){
            UserId.setId(id);
        } else {
            return true;
        }

        redisCacheUtil.expire(RedisConst.TOKEN +id,RedisConst.TOKEN_EXPIRE, TimeUnit.MINUTES);
        return true;

    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull Object handler, Exception ex){
        UserId.remove();                                   //2*清理ThreadLocal的数据
    }
}
