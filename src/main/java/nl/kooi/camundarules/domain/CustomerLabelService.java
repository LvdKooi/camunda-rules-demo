package nl.kooi.camundarules.domain;

import lombok.RequiredArgsConstructor;
import nl.kooi.camundarules.persistence.CustomerLabel;
import nl.kooi.camundarules.persistence.CustomerLabelRepository;
import nl.kooi.camundarules.enums.Label;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerLabelService {
    private final CustomerLabelRepository repository;

    public CustomerLabel saveCustomerLabel(Long customerId, Label label) {
        var customerLabel = new CustomerLabel();
        customerLabel.setCustomerId(customerId);
        customerLabel.setLabel(label);
        return repository.save(customerLabel);
    }

    public List<CustomerLabel> getCustomerLabels() {
        return repository.findAll();
    }
}
