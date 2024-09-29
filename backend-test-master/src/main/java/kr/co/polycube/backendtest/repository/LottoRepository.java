package kr.co.polycube.backendtest.repository;

import kr.co.polycube.backendtest.model.Lotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LottoRepository extends JpaRepository<Lotto, Long> {
}
