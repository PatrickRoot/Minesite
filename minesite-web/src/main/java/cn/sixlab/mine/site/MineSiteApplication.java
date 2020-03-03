package cn.sixlab.mine.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
// @SpringBootApplication(scanBasePackages = {
//         "cn.sixlab.mine.site",
// })
@EntityScan(basePackages = {
        "cn.sixlab.mine.site.data.models",
})
@EnableJpaRepositories(basePackages = {
        "cn.sixlab.mine.site.data.dao",
})
public class MineSiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(MineSiteApplication.class, args);
    }

}