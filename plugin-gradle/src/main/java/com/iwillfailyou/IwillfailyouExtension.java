package com.iwillfailyou;

import java.util.Arrays;
import java.util.List;

public class IwillfailyouExtension {
    private final List<Integer> threshold;
    private final List<Boolean> offline;

    public IwillfailyouExtension() {
        this(
            Arrays.asList(0),
            Arrays.asList(false)
        );
    }

    public IwillfailyouExtension(
        final List<Integer> threshold,
        final List<Boolean> offline
    ) {
        this.threshold = threshold;
        this.offline = offline;
    }

    public boolean getOffline() {
        return offline.get(0);
    }

    public void setOffline(final boolean offline) {
        this.offline.set(0, offline);
    }
}
