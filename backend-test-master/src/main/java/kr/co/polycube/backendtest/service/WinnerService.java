package kr.co.polycube.backendtest.service;

import kr.co.polycube.backendtest.model.Winner;
import kr.co.polycube.backendtest.repository.WinnerRepository;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WinnerService {

    private final WinnerRepository winnerRepository;

    //@Autowired
    public WinnerService(WinnerRepository winnerRepository) {
        this.winnerRepository = winnerRepository;
    }

    public Winner saveWinner(Long lotto_id, int rank) {
        Winner winner = new Winner();
        winner.setLotto_id(lotto_id);
        winner.setRank(rank);
        return winnerRepository.save(winner);
    }

    public void deleteWinners() {
        winnerRepository.deleteAll();
    }
}
