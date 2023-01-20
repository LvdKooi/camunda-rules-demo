package nl.kooi.camundarules.domain;

import lombok.RequiredArgsConstructor;
import nl.kooi.camundarules.enums.Label;
import nl.kooi.camundarules.persistence.CustomerLabel;
import nl.kooi.camundarules.persistence.CustomerLabelRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerLabelService {
    private final CustomerLabelRepository repository;

    public CustomerLabel saveCustomerLabel(CustomerLabel customerLabel) {
        return repository.save(customerLabel);
    }

    @Async
    public void saveAmountOfCustomerLabels(int number) {
        IntStream.rangeClosed(1, number)
                .mapToObj(this::createCustomerLabel)
                .forEach(repository::save);
    }

    private CustomerLabel createCustomerLabel(int i) {
        System.out.println(i);
        var label = new CustomerLabel();
        label.setLabel(Label.LABEL_3);
        label.setThrottled(true);
        label.setCustomerId((long) i);
        return label;
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
