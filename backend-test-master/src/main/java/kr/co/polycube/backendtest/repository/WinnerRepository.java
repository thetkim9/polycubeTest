package kr.co.polycube.backendtest.repository;

import kr.co.polycube.backendtest.model.Winner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WinnerRepository extends JpaRepository<Winner, Long> {
}