package com.dmarco.seckill.config;

import com.dmarco.seckill.domain.SeckillUser;
import com.dmarco.seckill.service.SeckillUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Dmarco
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    SeckillUserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz=methodParameter.getParameterType();
        return clazz== SeckillUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request=nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response=nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        String paramToken = request.getParameter(SeckillUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request,SeckillUserService.COOKIE_NAME_TOKEN);
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            return null;
        }
        String token=StringUtils.isEmpty(paramToken)? cookieToken:paramToken;

        return  userService.getByToken(response,token);
    }

    private String getCookieValue(HttpServletRequest request,String cookieName){
        Cookie[] cookies=request.getCookies();
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(cookieName)){
                return cookie.getValue();
            }
        }
        return null;
    }

}
