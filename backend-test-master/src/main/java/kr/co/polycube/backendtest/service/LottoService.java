package kr.co.polycube.backendtest.service;

import kr.co.polycube.backendtest.model.Lotto;
import kr.co.polycube.backendtest.repository.LottoRepository;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LottoService {
    
    private final LottoRepository lottoRepository;

    //@Autowired
    public LottoService(LottoRepository lottoRepository) {
        this.lottoRepository = lottoRepository;
    }

    public List<Lotto> getLottos() {
        return lottoRepository.findAll();
    }

}
