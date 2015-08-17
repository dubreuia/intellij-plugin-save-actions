package com.dubreuia.processors;

import com.dubreuia.model.Storage;
import org.junit.Before;
import org.junit.Test;

import static com.dubreuia.model.Action.activate;
import static org.junit.Assert.assertEquals;

public class ProcessorFactoryTest {

    private ProcessorFactory processorFactory;

    private Storage storage;

    @Before
    public void before() {
        processorFactory = ProcessorFactory.INSTANCE;
        storage = new Storage();
    }

    @Test
    public void test_get_save_action_processors_not_activated() {
        storage.setEnabled(activate, false);
        assertEquals(0, processorFactory.getSaveActionsProcessors(null, null, storage).size());
    }

}