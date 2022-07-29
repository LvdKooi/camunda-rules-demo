package nl.kooi.camundarules.delegate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kooi.camundarules.domain.CustomerLabelService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class GetCustomersDelegate implements JavaDelegate {
    private final CustomerLabelService service;

    @Override
    public void execute(DelegateExecution delegateExecution) {

        var ids = service.getCustomerIds();

        log.info("Received {} customers.", ids.size());
        delegateExecution.setVariable("customerIds", ids);
    }
}
