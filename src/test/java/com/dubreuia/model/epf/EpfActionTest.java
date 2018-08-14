package com.dubreuia.model.epf;

import com.dubreuia.model.Action;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.fest.assertions.api.Assertions.assertThat;

public class EpfActionTest {

    @Test
    public void should_epf_action_have_no_duplicate_action() {
        List<Action> collect = Arrays.stream(EpfAction.values())
                .map(EpfAction::getAction)
                .collect(Collectors.toList());
        assertThat(collect).doesNotHaveDuplicates();
    }

}