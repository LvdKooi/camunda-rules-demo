package nl.kooi.camundarules.persistence;

import nl.kooi.camundarules.enums.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerLabelRepository extends JpaRepository<CustomerLabel, Long> {

    @Query("Select distinct customerId from CustomerLabel")
    List<Long> findAllCustomerIds();

    @Query("Select distinct label from CustomerLabel")
    List<Label> findLabelsByCustomerId(Long customerId);

    Optional<CustomerLabel> findByCustomerIdAndLabel(Long customerId, Label label);
}


