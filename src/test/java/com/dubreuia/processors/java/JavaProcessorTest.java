package com.dubreuia.processors.java;

import com.dubreuia.model.Action;
import com.dubreuia.processors.Processor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.dubreuia.model.ActionType.java;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class JavaProcessorTest {

    @Test
    void should_java_processor_have_no_duplicate_action() {
        List<Action> actions = JavaProcessor.stream().map(Processor::getAction).collect(toList());
        assertThat(actions).doesNotHaveDuplicates();
    }

    @Test
    void should_java_processor_have_valid_type_and_inspection() {
        JavaProcessor.stream()
                .forEach(processor -> assertThat(processor.getAction().getType()).isEqualTo(java));
        JavaProcessor.stream()
                .forEach(processor -> assertThat(((JavaProcessor) processor).getInspection()).isNotNull());
    }

    @Test
    void should_java_action_have_java_processor() {
        Action.stream(java).forEach(action -> assertThat(JavaProcessor.getProcessorForAction(action)).isNotEmpty());
    }

}