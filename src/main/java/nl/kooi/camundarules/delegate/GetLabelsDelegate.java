package nl.kooi.camundarules.delegate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kooi.camundarules.domain.CustomerLabelService;
import nl.kooi.camundarules.enums.Label;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class GetLabelsDelegate implements JavaDelegate {

    private final CustomerLabelService service;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var customerId = delegateExecution.getVariable("customerId");
        var labels = service.getLabelsForCustomer((Long) customerId);
        var labelsAsString = labels.stream().map(Label::toString).toList();

        delegateExecution.setVariable("labels", labelsAsString);
    }
}
