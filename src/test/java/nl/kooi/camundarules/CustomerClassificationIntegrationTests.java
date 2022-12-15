package nl.kooi.camundarules;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nl.kooi.camundarules.api.CustomerClassificationController;
import nl.kooi.camundarules.api.CustomerLabelController;
import nl.kooi.camundarules.api.dto.ClassificationOutcome;
import nl.kooi.camundarules.api.dto.CustomerLabelDto;
import nl.kooi.camundarules.enums.Label;
import org.camunda.bpm.engine.RuntimeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
class CustomerClassificationIntegrationTests {

    private static final String POST_CUSTOMER_LABEL_ENDPOINT = "/customer-labels";
    private static final String GET_OUTCOME_ENDPOINT = "/classification-outcome";

    @Autowired
    private CustomerClassificationController customerClassificationController;

    @Autowired
    private CustomerLabelController customerLabelController;

    @Autowired
    private RuntimeService camundaRuntime;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private List<ClassificationOutcome> outcomeList;

    private String currentProcessId;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(customerClassificationController, customerLabelController).build();
    }

    @AfterEach
    void cleanUp() {
        customerClassificationController.clearAllClassifications();
        cleanUpCamundaProcesses();
    }

    @Test
    @DisplayName("When a customer has label 2, then label 2 is prioritized and output strategy is selected accordingly.")
    void testProcess_label2StaysLabel2AfterPrioritizing() {
        //        given
        givenThereIsACustomerWith(1, Label.LABEL_2, false);

        //        when
        whenImStartingTheClassificationProcess();
        andImRetrievingTheClassificationOutcome();

        //        then
        thenTheOutcomeContainsCustomerWithLabelAndStrategy(1, Label.LABEL_2, "[whoosh]");
    }

    @Test
    @DisplayName("When a customer has label 1 and 2, then label 1 is prioritized and output strategy is selected accordingly.")
    void testProcess_label1IsPrioritizedOverLabel2() {
        //        given
        givenThereIsACustomerWith(1, Label.LABEL_1, false);
        andThereIsACustomerWith(1, Label.LABEL_2, false);

        //        when
        whenImStartingTheClassificationProcess();
        andImRetrievingTheClassificationOutcome();

        //        then
        thenTheOutcomeContainsCustomerWithLabelAndStrategy(1, Label.LABEL_1, "[letter, whoosh, current_affair]");
    }

    @Test
    @DisplayName("Multiple customers are handled and prioritized according to their own labels.")
    void testProcess_multipleCustomers() {
        //        given (customer 1 with label 1 and 3, customer 2 with label 1 and 2)
        givenThereIsACustomerWith(1, Label.LABEL_1, false);
        andThereIsACustomerWith(1, Label.LABEL_3, false);

        andThereIsACustomerWith(2, Label.LABEL_1, false);
        andThereIsACustomerWith(2, Label.LABEL_2, false);

        //        when
        whenImStartingTheClassificationProcess();
        andImRetrievingTheClassificationOutcome();

        //        then
        thenTheOutcomeContainsCustomerWithLabelAndStrategy(1, Label.LABEL_3, "[letter]");
        andTheOutcomeContainsCustomerWithLabelAndStrategy(2, Label.LABEL_1, "[letter, whoosh, current_affair]");
    }

    @Test
    @DisplayName("When a customer has label 1 and 3, then label 3 is prioritized and output strategy is selected accordingly.")
    void testProcess_label3IsPrioritizedOverLabel1() {
        //        given
        givenThereIsACustomerWith(1, Label.LABEL_1, false);
        andThereIsACustomerWith(1, Label.LABEL_3, false);

        //        when
        whenImStartingTheClassificationProcess();
        andImRetrievingTheClassificationOutcome();

        //        then
        thenTheOutcomeContainsCustomerWithLabelAndStrategy(1, Label.LABEL_3, "[letter]");
    }

    @Test
    @DisplayName("When a customer is throttled, then output strategy us always [no].")
    void testProcess_throttledIsAlwaysOutputStrategyNo() {
        //        given
        givenThereIsACustomerWith(1, Label.LABEL_1, true);

        //        when
        whenImStartingTheClassificationProcess();
        andImRetrievingTheClassificationOutcome();

        //        then
        thenTheOutcomeContainsCustomerWithLabelAndStrategy(1, Label.LABEL_1, "[no]");
    }

    @SneakyThrows
    void givenThereIsACustomerWith(long customerNumber, Label label, boolean isThrottled) {
        var customerLabelDto = new CustomerLabelDto();
        customerLabelDto.setCustomerId(customerNumber);
        customerLabelDto.setLabel(label);
        customerLabelDto.setThrottled(isThrottled);

        mockMvc.perform(post(POST_CUSTOMER_LABEL_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerLabelDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    void andThereIsACustomerWith(int customerNumber, Label label, boolean isThrottled) {
        givenThereIsACustomerWith(customerNumber, label, isThrottled);
    }

    @SneakyThrows
    void andImRetrievingTheClassificationOutcome() {
        var outcomeString = mockMvc.perform(get(GET_OUTCOME_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        var typeRef = new TypeReference<List<ClassificationOutcome>>() {
        };

        outcomeList = objectMapper.readValue(outcomeString, typeRef);
    }

    @SneakyThrows
    void whenImStartingTheClassificationProcess() {
        currentProcessId = camundaRuntime.startProcessInstanceByKey("classification_process").getProcessInstanceId();
        var query = camundaRuntime.createProcessInstanceQuery().processInstanceId(currentProcessId);
        var maxInstant = Instant.now().plusSeconds(10);
        var nextTry = Instant.now().plusSeconds(1);
        var processInstance = query.active().singleResult();

        do {
            if (nextTry.isBefore(Instant.now())) {
                log.info("Checking if process with id {} has ended.", currentProcessId);
                processInstance = query.active().singleResult();
                nextTry = Instant.now().plusSeconds(1);
            }

        }
        while (processInstance != null && Instant.now().isBefore(maxInstant));


        if (query.active().singleResult() != null) {
            fail(String.format("Process %s didn't finish in time.", currentProcessId));
        }
    }

    void thenTheOutcomeContainsCustomerWithLabelAndStrategy(long customerId, Label label, String strategy) {
        var isPresent = outcomeList.stream()
                .map(ClassificationOutcome::getOutcome)
                .filter(outcome -> outcome.getCustomerId().equals(customerId))
                .filter(outcome -> outcome.getLabel() == label)
                .anyMatch(outcome -> outcome.getOutputStrategy().equals(strategy));

        assertThat(isPresent).isTrue();
    }

    void andTheOutcomeContainsCustomerWithLabelAndStrategy(long customerId, Label label, String strategy) {
        thenTheOutcomeContainsCustomerWithLabelAndStrategy(customerId, label, strategy);
    }

    void cleanUpCamundaProcesses() {
        camundaRuntime.deleteProcessInstanceIfExists(currentProcessId, "testing", true, true, true, true);
        currentProcessId = null;
    }
}
