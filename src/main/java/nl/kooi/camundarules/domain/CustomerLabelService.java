package nl.kooi.camundarules.domain;

import lombok.RequiredArgsConstructor;
import nl.kooi.camundarules.enums.Label;
import nl.kooi.camundarules.persistence.CustomerLabel;
import nl.kooi.camundarules.persistence.CustomerLabelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerLabelService {
    private final CustomerLabelRepository repository;

    public CustomerLabel saveCustomerLabel(Long customerId, Label label, boolean isThrottled) {
        var customerLabel = new CustomerLabel();
        customerLabel.setCustomerId(customerId);
        customerLabel.setLabel(label);
        customerLabel.setThrottled(isThrottled);
        return repository.save(customerLabel);
    }

    @Transactional(readOnly = true)
    public List<CustomerLabel> getCustomerLabels() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Long> getCustomerIds() {
        return repository.findAllCustomerIds();
    }

    @Transactional(readOnly = true)
    public List<Label> getLabelsForCustomer(Long customerId) {
        return repository.findLabelsByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public boolean isLabelThrottledForCustomer(Long cutomerId, Label label) {
        var customerLabel = repository.findByCustomerIdAndLabel(cutomerId, label);

        return customerLabel.map(CustomerLabel::isThrottled).orElse(false);

    }

    public void clearAll() {
        repository.deleteAll();
    }
}
