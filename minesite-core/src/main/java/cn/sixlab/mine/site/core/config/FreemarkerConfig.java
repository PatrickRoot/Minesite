package cn.sixlab.mine.site.core.config;

import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FreemarkerConfig {

    @Autowired
    public void setSharedVariable(Configuration configuration,
                                  FrameInfoDirective infoDirective) {
        configuration.setSharedVariable("FrameInfo", infoDirective);

        List<String> includes = new ArrayList<>();
        includes.add("frame/index.ftlh");

        configuration.setAutoIncludes(includes);
    }

}