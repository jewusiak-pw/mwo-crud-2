package pl.edu.pw.mwotest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pw.mwotest.models.Concert;

public interface ConcertRepository extends JpaRepository<Concert, Integer> {
}
