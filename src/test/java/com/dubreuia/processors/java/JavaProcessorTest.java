package com.dubreuia.processors.java;

import com.dubreuia.model.Action;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.dubreuia.model.ActionType.java;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class JavaProcessorTest {

    @Test
    public void should_java_processor_have_no_duplicate_action() {
        List<Action> actions = JavaProcessor.stream().map(JavaProcessor::getAction).collect(toList());
        assertThat(actions).doesNotHaveDuplicates();
    }

    @Test
    public void should_java_processor_have_valid_type_and_inspection() {
        JavaProcessor.stream().forEach(processor -> assertThat(processor.getAction().getType()).isEqualTo(java));
        JavaProcessor.stream().forEach(processor -> assertThat(processor.getInspection()).isNotNull());
    }

    @Test
    public void should_java_action_have_java_processor() {
        Action.stream(java).forEach(action -> assertThat(JavaProcessor.getJavaProcessorForAction(action)).isNotEmpty());
    }

}