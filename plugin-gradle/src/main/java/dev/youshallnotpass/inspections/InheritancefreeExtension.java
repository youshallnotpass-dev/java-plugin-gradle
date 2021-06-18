package dev.youshallnotpass.inspections;

import dev.youshallnotpass.inspections.inheritancefree.Inheritancefree;
import dev.youshallnotpass.plugin.Inspection;

import java.util.List;

@SuppressWarnings("allfinal") // only for gradle plugin framework
public class InheritancefreeExtension implements InspectionExtension {
    private final CommonExtension common;

    public InheritancefreeExtension() {
        this(
            new CommonExtension()
        );
    }

    public InheritancefreeExtension(
        final CommonExtension common
    ) {
        this.common = common;
    }

    @Override
    public void setDisabled(final boolean disabled) {
        this.common.setDisabled(disabled);
    }

    @Override
    public void setExclude(final List<String> exclude) {
        this.common.setExclude(exclude);
    }

    @Override
    public void inheritExclude(final List<String> exclude) {
        this.common.inheritExclude(exclude);
    }

    @Override
    public void setThreshold(final int threshold) {
        this.common.setThreshold(threshold);
    }

    @Override
    public Inspection inspection() {
        final Inspection inspection;
        if (common.getDisabled()) {
            inspection = new Inspection.Fake();
        } else {
            inspection = new Inheritancefree(
                common.mask(),
                common.getThreshold()
            );
        }
        return inspection;
    }
}
