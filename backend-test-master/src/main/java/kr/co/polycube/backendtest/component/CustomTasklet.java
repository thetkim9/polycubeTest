package kr.co.polycube.backendtest.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.common.lang.NonNull;
import kr.co.polycube.backendtest.model.Lotto;
import kr.co.polycube.backendtest.model.Winner;
import kr.co.polycube.backendtest.service.LottoService;
import kr.co.polycube.backendtest.service.WinnerService;

@Component
public class CustomTasklet implements Tasklet {

    private final LottoService lottoService;
    private final WinnerService winnerService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String url;
    private final int digits;

    public CustomTasklet(LottoService lottoService, WinnerService winnerService, 
    RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.lottoService = lottoService;
        this.winnerService = winnerService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        url = "http://localhost:8080/lottos";
        digits = 6;
    }

    @Override
    public RepeatStatus execute(@NonNull StepContribution contribution, @NonNull ChunkContext chunkContext) throws Exception {
        //read
        List<Lotto> lottos = lottoService.getLottos();

        //process
        //get lotto answer
        int[] answer = new int[digits];
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
        JsonNode body = objectMapper.readTree(response.getBody());
        JsonNode numbers = body.get("numbers");
        for (int j = 0; j<digits; j++) {
            answer[j] = numbers.get(j).asInt();
        }
        //rank lottos
        Map<Long, Integer> counts = new HashMap<>();
        for (Lotto lotto : lottos) {
            int count = 0;
            Long lotto_id = lotto.getId();
            if (lotto.getNumber_1()==answer[0])
                count++;
            if (lotto.getNumber_2()==answer[1])
                count++;
            if (lotto.getNumber_3()==answer[2])
                count++;
            if (lotto.getNumber_4()==answer[3])
                count++;
            if (lotto.getNumber_5()==answer[4])
                count++;
            if (lotto.getNumber_6()==answer[5])
                count++;
            counts.put(lotto_id, count);
        }
        List<Map.Entry<Long, Integer>> list = new ArrayList<>(counts.entrySet());
        list.sort(Map.Entry.comparingByValue());
        //winners
        List<Winner> winners = new ArrayList<>();
        int rank = list.size();
        for (Map.Entry<Long, Integer> entry : list) {
            Winner winner = new Winner();
            winner.setLotto_id(entry.getKey());
            winner.setRank(rank);
            rank--;
            winners.add(winner);
        }

        //write
        winnerService.deleteWinners();
        for (Winner winner : winners) {
            winnerService.saveWinner(winner.getLotto_id(), winner.getRank());
        }

        //System.out.println("executed!!");
        return RepeatStatus.FINISHED; 
    }
}
