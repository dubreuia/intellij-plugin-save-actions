package com.dubreuia.processors;

/**
 * ACCESSOR to get access to hidden classes in this package.
 */
public final class ProcessorACCESSOR {
    public static Class<? extends Processor> getCompileProcessorClass() {
        return CompileProcessor.class;
    }
}
