package kr.co.polycube.backendtest.service;

import com.opencsv.CSVReader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class CsvLoaderService {

    private final JdbcTemplate jdbcTemplate;

    public CsvLoaderService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void loadLottosFromCsv(InputStream inputStream) throws Exception {
        CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream));
        csvReader.readNext();
        String[] values;
        while ((values = csvReader.readNext()) != null) {
            Long id = Long.parseLong(values[0]);
            int number_1 = Integer.parseInt(values[1]);
            int number_2 = Integer.parseInt(values[2]);
            int number_3 = Integer.parseInt(values[3]);
            int number_4 = Integer.parseInt(values[4]);
            int number_5 = Integer.parseInt(values[5]);
            int number_6 = Integer.parseInt(values[6]);
            jdbcTemplate.update("INSERT INTO lotto (id,number_1,number_2,number_3,number_4,number_5,number_6) VALUES " 
            + "(?, ?, ?, ?, ?, ?, ?)", id, number_1, number_2, number_3, number_4, number_5, number_6);
        }
        csvReader.close();
    }
}