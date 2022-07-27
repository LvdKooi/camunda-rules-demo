package nl.kooi.camundarules.domain;

import lombok.RequiredArgsConstructor;
import nl.kooi.camundarules.enums.Label;
import nl.kooi.camundarules.persistence.CustomerTopLabel;
import nl.kooi.camundarules.persistence.CustomerTopLabelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class CustomerTopLabelService {
    private final CustomerTopLabelRepository repository;

    public CustomerTopLabel saveCustomerTopLabel(Long customerId, Label topLabel) {
        var customerTopLabel = new CustomerTopLabel();
        customerTopLabel.setCustomerId(customerId);
        customerTopLabel.setLabel(topLabel);
        return repository.save(customerTopLabel);
    }

    @Transactional(readOnly = true)
    public CustomerTopLabel getCustomerTopLabel(Long customerId) {
        return repository.findByCustomerId(customerId).orElseThrow();
    }

    public CustomerTopLabel addOutputStrategyToCustomerTopLabel(Long customerId, String outputStrategy) {
        var customerTopLabel = repository.findByCustomerId(customerId).orElseThrow();
        customerTopLabel.setOutputStrategy(outputStrategy);
        return repository.save(customerTopLabel);
    }

    public void clearAll() {
        repository.deleteAll();
    }
}
