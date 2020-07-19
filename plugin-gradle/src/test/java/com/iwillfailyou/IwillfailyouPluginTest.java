package com.iwillfailyou;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

public final class IwillfailyouPluginTest {
    @Test
    public void applyPlugin() {
        final Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply(IwillfailyouPlugin.class);
        Assert.assertThat(
            project.getTasks().findByName("iwillfailyou"),
            IsNull.notNullValue()
        );
    }
}