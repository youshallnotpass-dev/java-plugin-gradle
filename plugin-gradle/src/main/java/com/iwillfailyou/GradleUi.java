package com.iwillfailyou;

import com.iwillfailyou.plugin.Ui;
import org.gradle.api.logging.Logger;

public class GradleUi implements Ui {

    private final Logger logger;

    public GradleUi(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void println(final String text) {
        logger.warn(text);
    }
}
