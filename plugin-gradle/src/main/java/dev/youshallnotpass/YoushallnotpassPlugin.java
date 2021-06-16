package dev.youshallnotpass;

import dev.youshallnotpass.inspections.AllfinalExtension;
import dev.youshallnotpass.inspections.AllpublicExtension;
import dev.youshallnotpass.inspections.InheritancefreeExtension;
import dev.youshallnotpass.inspections.InspectionExtension;
import dev.youshallnotpass.inspections.NoMultipleReturnExtension;
import dev.youshallnotpass.inspections.NullfreeExtension;
import dev.youshallnotpass.inspections.SetterFreeExtension;
import dev.youshallnotpass.inspections.StaticfreeExtension;
import com.nikialeksey.goo.Goo;
import com.nikialeksey.goo.GooException;
import com.nikialeksey.goo.Origin;
import dev.youshallnotpass.plugin.Inspection;
import dev.youshallnotpass.plugin.PublicInspection;
import dev.youshallnotpass.plugin.YsnpException;
import dev.youshallnotpass.plugin.YsnpPlugin;
import dev.youshallnotpass.plugin.YsnpUrls;
import org.cactoos.func.SolidFunc;
import org.cactoos.iterable.Mapped;
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

public final class YoushallnotpassPlugin implements Plugin<Project> {
    @Override
    public void apply(final Project target) {
        final ExtensionContainer targetExtensions = target.getExtensions();
        final YoushallnotpassExtension settings = targetExtensions.create(
            "youshallnotpass",
            YoushallnotpassExtension.class
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

        target.task("youshallnotpass").doLast((final Task task) -> {
            for (final InspectionExtension inspectionExtension : inspectionExtensions) {
                inspectionExtension.inheritExclude(settings.getExclude());
            }
            final List<Inspection> inspections = new ListOf<>(
                new Mapped<>(
                    new SolidFunc<>(InspectionExtension::inspection),
                    inspectionExtensions
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
                                    new YsnpUrls(
                                        origin,
                                        "https://www.youshallnotpass.dev"
                                    ),
                                    inspection
                                )
                            );
                        }
                    } catch (final GooException e) {
                        throw new YsnpException(
                            "Could not get the origin for git repo. You can " +
                                "use offline version, if you have not git " +
                                "repo yet, just set the youshallnotpass { " +
                                "offline = true }",
                            e
                        );
                    }
                }
                new YsnpPlugin(
                    new GradleUi(target.getLogger()),
                    target.getRootDir(),
                    wrapped
                ).run();
            } catch (final YsnpException e) {
                throw new GradleScriptException(
                    "Can not make the youshallnotpass analysis.",
                    e
                );
            }
        });
    }
}
