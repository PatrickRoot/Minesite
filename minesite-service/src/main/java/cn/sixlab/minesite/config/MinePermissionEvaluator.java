package cn.sixlab.minesite.config;

import cn.sixlab.minesite.dao.MsUserDao;
import cn.sixlab.minesite.models.MsUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class MinePermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private MsUserDao msUserDao;

    @Value("${minesite.username}")
    private String username;

    @Override
    public boolean hasPermission(Authentication authentication, Object adminAuth, Object userAuth) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof MsUser) {
            MsUser msUser = (MsUser) principal;
            Integer userId = msUser.getId();

            String adminAuthCode = String.valueOf(adminAuth);
            String userAuthCode = String.valueOf(userAuth);

            if(StringUtils.isEmpty(adminAuthCode) && StringUtils.isEmpty(adminAuthCode)){
                return true;
            }

            if(msUser.getUsername().startsWith(username+"-")){
                // 管理员
                if (StringUtils.isEmpty(adminAuthCode) || "permitAll".equals(adminAuthCode)) {
                    return true;
                } else if ("denyAll".equals(userAuthCode)) {
                    return false;
                } else {
                    String[] codes = StringUtils.split(adminAuthCode, ",");
                    int i = msUserDao.countUserRole(userId, codes);

                    return i > 0;
                }

            }else{
                // 普通用户
                if (StringUtils.isEmpty(userAuthCode) || "permitAll".equals(userAuthCode)) {
                    return true;
                } else if ("denyAll".equals(userAuthCode)) {
                    return false;
                } else {
                    String[] codes = StringUtils.split(userAuthCode, ",");
                    // TODO
                    return codes != null;
                }
            }
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String adminAuth, Object userAuth) {
        return hasPermission(authentication, adminAuth, userAuth);
    }
}
