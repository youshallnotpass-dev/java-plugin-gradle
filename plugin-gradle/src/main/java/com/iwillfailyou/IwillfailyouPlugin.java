package com.iwillfailyou;

import com.iwillfailyou.plugin.Inspection;
import com.iwillfailyou.plugin.IwfyException;
import com.iwillfailyou.plugin.IwfyPlugin;
import com.iwillfailyou.plugin.IwfyUrls;
import com.iwillfailyou.plugin.PublicInspection;
import com.nikialeksey.goo.Goo;
import com.nikialeksey.goo.GooException;
import com.nikialeksey.goo.Origin;
import org.cactoos.list.ListOf;
import org.gradle.api.GradleScriptException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.ExtensionAware;
import org.gradle.api.plugins.ExtensionContainer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class IwillfailyouPlugin implements Plugin<Project> {
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
        final AllfinalExtension allfinalSettings = settingsExtensions.create(
            "allfinal",
            AllfinalExtension.class
        );
        final AllpublicExtension allpublicSettings = settingsExtensions.create(
            "allpublic",
            AllpublicExtension.class
        );

        target.task("iwillfailyou").doLast((final Task task) -> {
            final List<Inspection> inspections = new ListOf<>(
                nullfreeSettings.inspection(),
                staticfreeSettings.inspection(),
                allfinalSettings.inspection(),
                allpublicSettings.inspection()
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
                    } catch (final GooException e) {
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
