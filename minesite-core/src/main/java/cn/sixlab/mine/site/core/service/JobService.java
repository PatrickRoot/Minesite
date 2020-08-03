package cn.sixlab.mine.site.core.service;

import cn.sixlab.mine.site.core.utils.Ctx;
import cn.sixlab.mine.site.core.mapper.MsJobMapper;
import cn.sixlab.mine.site.core.models.MsJob;
import cn.sixlab.mine.site.core.api.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    @Autowired
    public MsJobMapper jobMapper;

    public void run(String jobClz) {
        MsJob msJob = jobMapper.selectByClz(jobClz);

        if (msJob != null && 1 == msJob.getStatus()) {
            Job job = Ctx.getBean(Job.class, jobClz);
            if (null != job) {
                job.run();
            }else {
                System.out.println(job.toString());
            }
        }
    }
}