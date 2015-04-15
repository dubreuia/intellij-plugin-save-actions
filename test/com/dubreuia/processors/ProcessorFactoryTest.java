package com.dubreuia.processors;

import com.dubreuia.Settings;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProcessorFactoryTest {

    private ProcessorFactory processorFactory;

    private Settings settings;

    @Before
    public void before() {
        processorFactory = ProcessorFactory.INSTANCE;
        settings = new Settings();
    }

    @Test
    public void test_get_save_action_processors_not_activated() {
        settings.setActivate(false);
        Assert.assertEquals(0, processorFactory.getSaveActionsProcessors(null, null, settings).size());
    }

}