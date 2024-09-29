package kr.co.polycube.backendtest.component;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

@Component
@EnableScheduling
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job checkWinnersJob;

    public BatchScheduler(JobLauncher jobLauncher, Job checkWinnersJob) {
        this.jobLauncher = jobLauncher;
        this.checkWinnersJob = checkWinnersJob;
    }

    //@Scheduled(cron = "15 * * * * *") //for testing batch process
    @Scheduled(cron = "0 0 0 * * SUN")
    public void runJob() throws Exception{
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("runId", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(checkWinnersJob, jobParameters);
    }
}
