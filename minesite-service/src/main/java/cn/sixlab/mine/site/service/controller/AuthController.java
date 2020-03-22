package cn.sixlab.mine.site.service.controller;

import cn.sixlab.mine.site.common.vo.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/auth")
public class AuthController {

    @PreAuthorize("hasPermission('', 'denyAll')")
    @ResponseBody
    @RequestMapping("/userAuth")
    public ResultJson userAuth() {

        return ResultJson.success();
    }

}
