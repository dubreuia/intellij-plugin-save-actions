package com.dubreuia.model.epf;

import com.dubreuia.model.Action;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class EpfActionTest {

    @Test
    public void should_epf_action_have_no_duplicate_action() {
        List<Action> actions = EpfAction.stream().map(EpfAction::getAction).collect(toList());
        assertThat(actions).doesNotHaveDuplicates();
    }

    @Test
    public void should_java_processor_have_valid_type_and_inspection() {
        EpfAction.stream().forEach(epfAction -> assertThat(epfAction.getAction().getType()).isNotNull());
        EpfAction.stream().forEach(epfAction -> assertThat(epfAction.getEpfKeys()).isNotEmpty());
    }

}