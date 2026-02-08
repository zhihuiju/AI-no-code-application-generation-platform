package com.tao.taoaicodemother.aop;

import com.tao.taoaicodemother.annotation.AuthCheck;
import com.tao.taoaicodemother.exception.BusinessException;
import com.tao.taoaicodemother.exception.ErrorCode;
import com.tao.taoaicodemother.model.entity.User;
import com.tao.taoaicodemother.model.enums.UserRoleEnum;
import com.tao.taoaicodemother.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Request;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
public class AuthInterceptor {

    @Resource
    private UserService userService;

    @Around("@annotation(authCheck)")
    public Object doIntercept(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttribute = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttribute).getRequest();
        //获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        //不需要权限直接放行
        if(mustRoleEnum == null){
            return joinPoint.proceed();
        }

        //以下的代码，必须有这个权限才能通过
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        //没用权限直接拒绝
        if(userRoleEnum == null){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //要求必须有管理员权限，但当前用户没用
        if(userRoleEnum.ADMIN.equals(mustRoleEnum) && !userRoleEnum.ADMIN.equals(userRoleEnum)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //通过普通用户的权限检验，放行
        return joinPoint.proceed();
    }
}
