package tech.minesoft.mine.site.common.directive;


import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.*;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.minesoft.mine.site.mysql.models.MsMenu;
import tech.minesoft.mine.site.common.service.MsMenuService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class MenuInfoDirective implements TemplateDirectiveModel {

    @Autowired
    public void setSharedVariable(Configuration configuration) {
        configuration.setSharedVariable("MenuInfo", this);
    }

    @Autowired
    private MsMenuService menuService;

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        String position = MapUtils.getString(params, "position");

        //获取网站配置信息,
        List<MsMenu> menuList = menuService.loadPostion(position);

        //将网站配置信息添加到全局变量
        env.setVariable("menuInfo",new BeansWrapperBuilder(Configuration.VERSION_2_3_29).build().wrap(menuList));

        if (body != null) {
            body.render(env.getOut());
        }
    }
}
