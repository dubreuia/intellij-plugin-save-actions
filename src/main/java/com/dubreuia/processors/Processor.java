package com.dubreuia.processors;

import java.util.Comparator;

/**
 * Processor interface with main method {@link #run()} that make changes to the underlying document. The
 * method might or might not be async. For now, only {@link OptimizeImportsProcessor} seems async.
 */
public interface Processor {

    void run();

    int order();

    class ProcessorComparator implements Comparator<Processor> {

        @Override
        public int compare(Processor o1, Processor o2) {
            if (o1.order() == o2.order()) {
                return 0;
            }
            return o1.order() < o2.order() ? -1 : 1;
        }

    }

}
