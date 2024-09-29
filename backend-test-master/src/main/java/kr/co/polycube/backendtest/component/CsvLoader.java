package kr.co.polycube.backendtest.component;
import java.io.InputStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import kr.co.polycube.backendtest.service.CsvLoaderService;
import jakarta.annotation.PostConstruct;

@Component
public class CsvLoader {

    private final CsvLoaderService csvDataLoaderService;

    public CsvLoader(CsvLoaderService csvDataLoaderService) {
        this.csvDataLoaderService = csvDataLoaderService;
    }

    @PostConstruct
    public void init() throws Exception {

        ClassPathResource resource = new ClassPathResource("lotto.csv");
        InputStream inputStream = resource.getInputStream();
        csvDataLoaderService.loadLottosFromCsv(inputStream);
        
    }
}
