package dev.youshallnotpass;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

public final class YoushallnotpassPluginTest {
    @Test
    public void applyPlugin() {
        final Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply(YoushallnotpassPlugin.class);
        Assert.assertThat(
            project.getTasks().findByName("youshallnotpass"),
            IsNull.notNullValue()
        );
    }
}