package kr.co.polycube.backendtest.service;

import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class LottoGenerationService {

    private Random rand;
    private final int SIZE = 6;

    public LottoGenerationService() {
        this.rand = new Random();
    }

    public int[] getLotto() {
        //digits from csv file to increase hit rate
        int[] digits = {7,28,33,2,45,19,26,14,41,3,22,35,15,29,38,6,44,21,31,16,42,9,23,36,17,30,39,10,45,24};
        int[] lotto = new int[SIZE];
        for (int i = 0; i<SIZE; i++) {
            lotto[i] = digits[rand.nextInt(30)];
        }
        return lotto;
    }
    
}
