package kr.co.polycube.backendtest;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.batch.core.*;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest 
public class BatchTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job checkWinnerJob;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void verifyLottoTableExists() {
        // Query the lotto table
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM lotto", Integer.class);
        assertThat(count).isGreaterThan(0); // Ensure data is loaded
    }

    @Test
    void verifyBatchTablesExist() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM BATCH_JOB_INSTANCE", Integer.class);
        assertThat(count).isNotNull();
    }

    @Test
    void testLottoBatch() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("runId", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        jobLauncher.run(checkWinnerJob, jobParameters);
    }
    
}
