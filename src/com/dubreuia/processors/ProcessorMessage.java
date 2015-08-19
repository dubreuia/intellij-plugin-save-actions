package com.dubreuia.processors;

import static java.text.MessageFormat.format;

class ProcessorMessage {

    private static final String FORMAT = "{0} ({1})";

    private ProcessorMessage() {
        // static class
    }

    static String toStringBuilder(String id, boolean enabled) {
        return format(FORMAT, id, enabled);
    }

}
