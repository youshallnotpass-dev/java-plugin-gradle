package com.iwillfailyou;

import com.iwillfailyou.inspections.AllfinalExtension;
import com.iwillfailyou.inspections.AllpublicExtension;
import com.iwillfailyou.inspections.InheritancefreeExtension;
import com.iwillfailyou.inspections.InspectionExtension;
import com.iwillfailyou.inspections.NoMultipleReturnExtension;
import com.iwillfailyou.inspections.NullfreeExtension;
import com.iwillfailyou.inspections.SetterFreeExtension;
import com.iwillfailyou.inspections.StaticfreeExtension;
import com.iwillfailyou.plugin.Inspection;
import com.iwillfailyou.plugin.IwfyException;
import com.iwillfailyou.plugin.IwfyPlugin;
import com.iwillfailyou.plugin.IwfyUrls;
import com.iwillfailyou.plugin.PublicInspection;
import com.nikialeksey.goo.Goo;
import com.nikialeksey.goo.GooException;
import com.nikialeksey.goo.Origin;
import org.cactoos.func.SolidFunc;
import org.cactoos.list.ListOf;
import org.cactoos.list.Mapped;
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

        final List<InspectionExtension> inspectionExtensions = new ListOf<>(
            settingsExtensions.create(
                "nullfree",
                NullfreeExtension.class
            ),
            settingsExtensions.create(
                "staticfree",
                StaticfreeExtension.class
            ),
            settingsExtensions.create(
                "allfinal",
                AllfinalExtension.class
            ),
            settingsExtensions.create(
                "allpublic",
                AllpublicExtension.class
            ),
            settingsExtensions.create(
                "setterfree",
                SetterFreeExtension.class
            ),
            settingsExtensions.create(
                "nomultiplereturn",
                NoMultipleReturnExtension.class
            ),
            settingsExtensions.create(
                "inheritancefree",
                InheritancefreeExtension.class
            )
        );

        target.task("iwillfailyou").doLast((final Task task) -> {
            for (final InspectionExtension inspectionExtension : inspectionExtensions) {
                inspectionExtension.inheritExclude(settings.getExclude());
            }
            final List<Inspection> inspections = new Mapped<>(
                new SolidFunc<>(InspectionExtension::inspection),
                inspectionExtensions
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
