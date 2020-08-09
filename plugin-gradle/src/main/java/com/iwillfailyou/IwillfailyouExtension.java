package com.iwillfailyou;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("allfinal") // only for gradle plugin framework
public class IwillfailyouExtension {
    private final List<Boolean> offline;
    private final List<String> exclude;

    public IwillfailyouExtension() {
        this(
            Arrays.asList(false),
            new ArrayList<>()
        );
    }

    public IwillfailyouExtension(
        final List<Boolean> offline,
        final List<String> exclude
    ) {
        this.offline = offline;
        this.exclude = exclude;
    }

    public boolean getOffline() {
        return offline.get(0);
    }

    public void setOffline(final boolean offline) {
        this.offline.set(0, offline);
    }

    public List<String> getExclude() {
        return exclude;
    }

    public void setExclude(final List<String> exclude) {
        this.exclude.clear();
        this.exclude.addAll(exclude);
    }
}
