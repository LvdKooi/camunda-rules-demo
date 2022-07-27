package nl.kooi.camundarules.delegate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kooi.camundarules.domain.CustomerTopLabelService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SaveOutputDelegate implements JavaDelegate {

    private final CustomerTopLabelService customerTopLabelService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        var outputOutcome =  delegateExecution.getVariable("output_strategy").toString();
        var customerId = (Long) delegateExecution.getVariable("customerId");

        log.info("Determined output strategy: {} for customer: {}", outputOutcome, customerId);

        customerTopLabelService
                .addOutputStrategyToCustomerTopLabel(customerId, outputOutcome);
    }
}
