package com.dubreuia.processors;

import java.util.Comparator;

public interface Processor {

    void writeToFile();

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
