package nl.kooi.camundarules.delegate;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SayHelloDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println(" HELLO!");

        System.out.println(delegateExecution.getVariable("variable1"));
        System.out.println(delegateExecution.getVariable("variable2"));

    }
}
