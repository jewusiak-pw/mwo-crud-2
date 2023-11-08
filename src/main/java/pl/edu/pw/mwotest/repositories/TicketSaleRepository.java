package pl.edu.pw.mwotest.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pw.mwotest.models.TicketSale;

public interface TicketSaleRepository extends JpaRepository<TicketSale, Integer> {
    int countAllByConcert_Id(Integer concertId);
}
