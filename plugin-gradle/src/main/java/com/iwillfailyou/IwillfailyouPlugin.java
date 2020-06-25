package com.iwillfailyou;

import com.iwillfailyou.nullfree.NullfreeInspection;
import com.iwillfailyou.nullfree.sources.java.JavaSourceFileFactory;
import com.iwillfailyou.plugin.Inspection;
import com.iwillfailyou.plugin.IwfyException;
import com.iwillfailyou.plugin.IwfyPlugin;
import com.iwillfailyou.plugin.IwfyUrls;
import com.iwillfailyou.plugin.PublicInspection;
import com.iwillfailyou.staticfree.StaticfreeInspection;
import com.nikialeksey.goo.Goo;
import com.nikialeksey.goo.GooException;
import com.nikialeksey.goo.Origin;
import org.cactoos.list.ListOf;
import org.gradle.api.GradleScriptException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionAware;
import org.gradle.api.plugins.ExtensionContainer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IwillfailyouPlugin implements Plugin<Project> {
    @Override
    public void apply(final Project target) {
        final ExtensionContainer targetExtensions = target.getExtensions();
        final IwillfailyouExtension settings = targetExtensions.create(
            "iwillfailyou",
            IwillfailyouExtension.class
        );
        final ExtensionAware settingsExtension = (ExtensionAware) settings;
        final ExtensionContainer settingsExtensions = settingsExtension.getExtensions();

        final NullfreeExtension nullfreeSettings = settingsExtensions.create(
            "nullfree",
            NullfreeExtension.class
        );
        final StaticfreeExtension staticfreeSettings = settingsExtensions.create(
            "staticfree",
            StaticfreeExtension.class
        );

        target.task("iwillfailyou").doLast(task -> {
            final List<Inspection> inspections = new ListOf<>(
                new NullfreeInspection(
                    new JavaSourceFileFactory(),
                    nullfreeSettings.getSkipComparisions(),
                    nullfreeSettings.getThreshold()
                ),
                new StaticfreeInspection(
                    new com.iwillfailyou.staticfree.sources.java.JavaSourceFileFactory(),
                    staticfreeSettings.getThreshold()
                )
            );
            try {
                final List<Inspection> wrapped;
                if (settings.getOffline()) {
                    wrapped = inspections;
                } else {
                    try {
                        final Origin origin = new Goo(
                            new File(target.getRootDir(), ".git")
                        ).origin();
                        wrapped = new ArrayList<>();
                        for (final Inspection inspection : inspections) {
                            wrapped.add(
                                new PublicInspection(
                                    new IwfyUrls(
                                        origin,
                                        "https://www.iwillfailyou.com"
                                    ),
                                    inspection
                                )
                            );
                        }
                    } catch (GooException e) {
                        throw new IwfyException(
                            "Could not get the origin for git repo. You can " +
                                "use offline version, if you have not git " +
                                "repo yet, just set the iwillfailyou { " +
                                "offline = true }",
                            e
                        );
                    }
                }
                new IwfyPlugin(
                    new GradleUi(target.getLogger()),
                    target.getRootDir(),
                    wrapped
                ).run();
            } catch (final IwfyException e) {
                throw new GradleScriptException(
                    "Can not make the iwillfailyou analysis.",
                    e
                );
            }
        });
    }
}
