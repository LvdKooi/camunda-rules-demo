package nl.kooi.camundarules.delegate;

import lombok.RequiredArgsConstructor;
import nl.kooi.camundarules.domain.CustomerTopLabelService;
import nl.kooi.camundarules.enums.Label;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class SaveTopLevelDataInBulkDelegate implements JavaDelegate {

    private final CustomerTopLabelService customerTopLabelService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var customerIds = (List<Long>) delegateExecution.getVariable("customerIds");

        customerIds.parallelStream().peek(System.out::println).forEach(id -> customerTopLabelService.saveCustomerTopLabel(id, Label.LABEL_1));
    }
}
