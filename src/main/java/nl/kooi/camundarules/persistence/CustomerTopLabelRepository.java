package nl.kooi.camundarules.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerTopLabelRepository extends JpaRepository<CustomerTopLabel, Long> {

    Optional<CustomerTopLabel> findByCustomerId(Long customerId);
}


