package kr.co.polycube.backendtest.controller;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import kr.co.polycube.backendtest.service.LottoGenerationService;
import kr.co.polycube.backendtest.view.LottoView;

@RestController
@RequestMapping("/lottos")
public class LottoController {

    private final LottoGenerationService lottoService;

    //@Autowired
    public LottoController(LottoGenerationService lottoService) {
        this.lottoService = lottoService;
    }

    @PostMapping
    public LottoView postLotto() {
        LottoView lottoView = new LottoView();
        lottoView.setNumbers(lottoService.getLotto());
        return lottoView;
    }
    
}