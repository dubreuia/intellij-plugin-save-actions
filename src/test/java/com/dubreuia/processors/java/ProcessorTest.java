package com.dubreuia.processors.java;

import com.dubreuia.model.Action;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.dubreuia.model.ActionType.java;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class ProcessorTest {

    @Test
    public void should_java_processor_have_no_duplicate_action() {
        List<Action> actions = Processor.stream().map(Processor::getAction).collect(toList());
        assertThat(actions).doesNotHaveDuplicates();
    }

    @Test
    public void should_java_processor_have_valid_type_and_inspection() {
        Processor.stream().forEach(processor -> assertThat(processor.getAction().getType()).isEqualTo(java));
        Processor.stream().forEach(processor -> assertThat(processor.getInspection()).isNotNull());
    }

    @Test
    public void should_java_action_have_java_processor() {
        Action.stream(java).forEach(action -> assertThat(Processor.getProcessorForAction(action)).isNotEmpty());
    }

}