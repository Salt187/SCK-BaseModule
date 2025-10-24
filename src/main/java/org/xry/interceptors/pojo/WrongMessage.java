package org.xry.interceptors.pojo;

public class WrongMessage {
    //空参异常
    public final static String NULL_PARAM = "参数为空！建议检查前端参数校验逻辑";

    //未知异常
    public final static String UNKNOWN = "服务器未知异常，请稍后重试！";

    //令牌过期异常
    public final static String TOKEN_EXPIRE = "令牌过期或失效";

}
