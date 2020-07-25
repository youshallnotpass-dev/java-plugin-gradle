package com.iwillfailyou;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("allfinal") // only for gradle plugin framework
public class AllpublicExtension {
    private final List<Integer> threshold;

    public AllpublicExtension() {
        this(
            Arrays.asList(0)
        );
    }

    public AllpublicExtension(
        final List<Integer> threshold
    ) {
        this.threshold = threshold;
    }

    public int getThreshold() {
        return threshold.get(0);
    }

    public int setThreshold(final int threshold) {
        return this.threshold.set(0, threshold);
    }
}
