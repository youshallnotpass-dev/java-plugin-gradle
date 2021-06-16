package dev.youshallnotpass.inspections;

import dev.youshallnotpass.inspections.allfinal.Allfinal;
import dev.youshallnotpass.plugin.Inspection;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("allfinal") // only for gradle plugin framework
public class AllfinalExtension implements InspectionExtension {
    private final CommonExtension common;
    private final List<Boolean> skipInterfaceMethodParams;
    private final List<Boolean> skipLambdaParams;
    private final List<Boolean> skipCatchParams;

    public AllfinalExtension() {
        this(
            new CommonExtension(),
            Arrays.asList(true),
            Arrays.asList(false),
            Arrays.asList(false)
        );
    }

    public AllfinalExtension(
        final CommonExtension common,
        final List<Boolean> skipInterfaceMethodParams,
        final List<Boolean> skipLambdaParams,
        final List<Boolean> skipCatchParams
    ) {
        this.common = common;
        this.skipInterfaceMethodParams = skipInterfaceMethodParams;
        this.skipLambdaParams = skipLambdaParams;
        this.skipCatchParams = skipCatchParams;
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

    public void setSkipInterfaceMethodParams(final boolean skipInterfaceMethodParams) {
        this.skipInterfaceMethodParams.set(0, skipInterfaceMethodParams);
    }

    public void setSkipLambdaParams(final boolean skipLambdaParams) {
        this.skipLambdaParams.set(0, skipLambdaParams);
    }

    public void setSkipCatchParams(final boolean skipCatchParams) {
        this.skipCatchParams.set(0, skipCatchParams);
    }

    @Override
    public Inspection inspection() {
        final Inspection inspection;
        if (common.getDisabled()) {
            inspection = new Inspection.Fake();
        } else {
            inspection = new Allfinal(
                common.mask(),
                common.getThreshold(),
                skipInterfaceMethodParams.get(0),
                skipLambdaParams.get(0),
                skipCatchParams.get(0)
            );
        }
        return inspection;
    }
}
