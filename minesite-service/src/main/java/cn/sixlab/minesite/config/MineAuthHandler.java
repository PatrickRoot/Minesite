package cn.sixlab.minesite.config;

import cn.sixlab.minesite.models.MsUser;
import cn.sixlab.minesite.service.UserService;
import cn.sixlab.minesite.utils.*;
import cn.sixlab.minesite.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class MineAuthHandler implements AuthenticationProvider, AuthenticationSuccessHandler, AuthenticationFailureHandler {
    @Autowired
    UserService userService;

    @Override
    public boolean supports(Class<?> authentication) {
        log.info("================");
        return true;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("================");
        String username = authentication.getName().trim();
        String password = (String) authentication.getCredentials();

        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
            UserDetails userDetails = userService.loadUserByUsername(username);

            if (null != userDetails) {
                if (BCrypt.checkpw(password, userDetails.getPassword())) {
                    return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
                } else {
                    throw new AuthenticationServiceException(I18nUtils.get("login.wrong.password"));
                }
            }

            throw new UsernameNotFoundException(I18nUtils.get("login.not.exist"));
        }

        throw new AuthenticationServiceException(I18nUtils.get("login.empty"));
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        log.info("================");
        String message;
        if (exception instanceof BadCredentialsException) {
            message = "login.bad.credentials";
        } else {
            message = exception.getMessage();
        }

        WebUtils.writeJson(response, ResultUtils.error(Err.AUTH, message).toString());
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("================");
        Object principal = authentication.getPrincipal();
        LoginUser loginUser = (LoginUser) principal;
        MsUser msUser = loginUser.getMsUser();

        String token = UserUtils.login(msUser);

        WebUtils.writeJson(response, ResultUtils.success(token).toString());
    }
}