package com.iwillfailyou;

import java.util.Arrays;
import java.util.List;

public final class AllfinalExtension {
    private final List<Integer> threshold;

    public AllfinalExtension() {
        this(
            Arrays.asList(0)
        );
    }

    public AllfinalExtension(
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
