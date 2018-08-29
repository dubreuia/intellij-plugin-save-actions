package com.dubreuia.processors;

import com.dubreuia.core.ExecutionMode;

import java.util.Comparator;

import static java.text.MessageFormat.format;

/**
 * Processor interface with main method {@link #run()}.
 */
public interface Processor {

    String FORMAT = "{0} ({1})";

    /**
     * Makes the change to the underlying document. The method might or might not be async. For now,
     * only {@link OptimizeImportsProcessor} seems async.
     */
    void run();

    /**
     * Return a comparable integer for {@link Comparator} to order the processor running order.
     *
     * @return a comparison integer
     * @see ProcessorComparator
     */
    int getOrder();

    /**
     * Returns if the processor is available to run in the given execution mode.
     *
     * @param mode the execution mode to run in
     * @return true if the processor is available in the given execution mode
     */
    default boolean canRun(ExecutionMode mode) {
        return true;
    }

    /**
     * Comparator on processor {@link #getOrder()}.
     */
    class ProcessorComparator implements Comparator<Processor> {

        @Override
        public int compare(Processor o1, Processor o2) {
            if (o1.getOrder() == o2.getOrder()) {
                return 0;
            }
            return o1.getOrder() < o2.getOrder() ? -1 : 1;
        }

    }

    default String toString(String name, boolean enabled) {
        return format(FORMAT, name, enabled);
    }

}
