package kr.co.polycube.backendtest.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import kr.co.polycube.backendtest.service.LottoService;
import kr.co.polycube.backendtest.service.WinnerService;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.polycube.backendtest.component.CustomTasklet;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    
    @Autowired
    LottoService lottoService;

    @Autowired
    WinnerService winnerService;

    @Bean
    public Job checkWinnersJob(JobRepository jobRepository, Step checkWinnersStep) {
        return new JobBuilder("checkWinnersJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(checkWinnersStep)
                .end()
                .build();
    }

    @Bean
    public Step checkWinnersStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("checkWinnersStep", jobRepository)
                .tasklet(customTasklet(restTemplate(), objectMapper()), transactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public CustomTasklet customTasklet(RestTemplate restTemplate, ObjectMapper objectMapper) {
        return new CustomTasklet(lottoService, winnerService, restTemplate, objectMapper);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}