package de.janschuri.lunaticlib;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class DecisionMessage {

    private final Component prefix;
    private final Component question;
    private final Component confirmText;
    private final Component denyText;
    private final Component confirmHover;
    private final Component denyHover;
    private final String confirmCommand;
    private final String denyCommand;

    public DecisionMessage(Component prefix, Component question, Component confirmText, Component denyText, Component confirmHover, Component denyHover, String confirmCommand, String denyCommand) {
        this.prefix = prefix;
        this.question = question;
        this.confirmText = confirmText;
        this.denyText = denyText;
        this.confirmHover = confirmHover;
        this.denyHover = denyHover;
        this.confirmCommand = confirmCommand;
        this.denyCommand = denyCommand;
    }

    public DecisionMessage(Component prefix, Component question, Component confirmHover, Component denyHover, String confirmCommand, String denyCommand) {
        this.prefix = prefix;
        this.question = question;
        this.confirmText = Component.text("✓", NamedTextColor.GREEN, TextDecoration.BOLD);
        this.denyText = Component.text("❌", NamedTextColor.RED, TextDecoration.BOLD);
        this.confirmHover = confirmHover;
        this.denyHover = denyHover;
        this.confirmCommand = confirmCommand;
        this.denyCommand = denyCommand;
    }

    public Component getPrefix() {
        return prefix;
    }

    public Component getQuestion() {
        return question;
    }

    public Component getConfirmText() {
        return confirmText;
    }

    public Component getDenyText() {
        return denyText;
    }

    public Component getConfirmHover() {
        return confirmHover;
    }

    public Component getDenyHover() {
        return denyHover;
    }

    public String getConfirmCommand() {
        return confirmCommand;
    }

    public String getDenyCommand() {
        return denyCommand;
    }

    public Component asComponent() {
        return prefix
                .append(question)
                .append(
                        confirmText
                                .hoverEvent(HoverEvent.showText(confirmHover))
                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, confirmCommand))
                )
                .append(
                        denyText
                                .hoverEvent(HoverEvent.showText(denyHover))
                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, denyCommand))
                );
    }

    public String[] toStringArray() {
        return new String[] {
                LegacyComponentSerializer.legacySection().serialize(prefix),
                LegacyComponentSerializer.legacySection().serialize(question),
                LegacyComponentSerializer.legacySection().serialize(confirmText),
                LegacyComponentSerializer.legacySection().serialize(denyText),
                LegacyComponentSerializer.legacySection().serialize(confirmHover),
                LegacyComponentSerializer.legacySection().serialize(denyHover),
                confirmCommand,
                denyCommand
        };
    }

    public static DecisionMessage fromStringArray(String[] array) {
        return new DecisionMessage(
                LegacyComponentSerializer.legacySection().deserialize(array[0]),
                LegacyComponentSerializer.legacySection().deserialize(array[1]),
                LegacyComponentSerializer.legacySection().deserialize(array[2]),
                LegacyComponentSerializer.legacySection().deserialize(array[3]),
                LegacyComponentSerializer.legacySection().deserialize(array[4]),
                LegacyComponentSerializer.legacySection().deserialize(array[5]),
                array[6],
                array[7]
        );
    }
}
