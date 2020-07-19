package com.iwillfailyou;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("allfinal") // only for gradle plugin framework
public class IwillfailyouExtension {
    private final List<Boolean> offline;

    public IwillfailyouExtension() {
        this(
            Arrays.asList(false)
        );
    }

    public IwillfailyouExtension(
        final List<Boolean> offline
    ) {
        this.offline = offline;
    }

    public boolean getOffline() {
        return offline.get(0);
    }

    public void setOffline(final boolean offline) {
        this.offline.set(0, offline);
    }
}
