package com.dubreuia.processors;

import com.dubreuia.model.Action;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.dubreuia.model.ActionType.build;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class BuildProcessorTest {

    @Test
    void should_processor_have_no_duplicate_action() {
        List<Action> actions = BuildProcessor.stream().map(Processor::getAction).collect(toList());
        assertThat(actions).doesNotHaveDuplicates();
    }

    @Test
    void should_processor_have_valid_type_and_inspection() {
        BuildProcessor.stream()
                .forEach(processor -> assertThat(processor.getAction().getType()).isEqualTo(build));
        BuildProcessor.stream()
                .forEach(processor -> assertThat(((BuildProcessor) processor).getCommand()).isNotNull());
    }

    @Test
    void should_action_have_java_processor() {
        Action.stream(build).forEach(action -> assertThat(BuildProcessor.getProcessorForAction(action)).isNotEmpty());
    }

}
