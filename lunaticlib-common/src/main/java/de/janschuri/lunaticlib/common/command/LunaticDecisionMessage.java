package de.janschuri.lunaticlib.common.command;

import de.janschuri.lunaticlib.DecisionMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class LunaticDecisionMessage implements DecisionMessage {

    private final Component prefix;
    private final Component question;
    private final Component confirmText;
    private final Component denyText;
    private final Component confirmHover;
    private final Component denyHover;
    private final String confirmCommand;
    private final String denyCommand;

    private boolean executeFromBackend = false;

    public LunaticDecisionMessage(Component prefix, Component question, Component confirmText, Component denyText, Component confirmHover, Component denyHover, String confirmCommand, String denyCommand) {
        this.prefix = prefix;
        this.question = question;
        this.confirmText = confirmText;
        this.denyText = denyText;
        this.confirmHover = confirmHover;
        this.denyHover = denyHover;
        this.confirmCommand = confirmCommand;
        this.denyCommand = denyCommand;
    }

    public LunaticDecisionMessage(Component prefix, Component question, Component confirmHover, Component denyHover, String confirmCommand, String denyCommand) {
        this.prefix = prefix;
        this.question = question;
        this.confirmText = Component.text("✓", NamedTextColor.GREEN, TextDecoration.BOLD);
        this.denyText = Component.text("❌", NamedTextColor.RED, TextDecoration.BOLD);
        this.confirmHover = confirmHover;
        this.denyHover = denyHover;
        this.confirmCommand = confirmCommand;
        this.denyCommand = denyCommand;
    }

    public void setExecuteFromBackend(boolean executeFromBackend) {
        this.executeFromBackend = executeFromBackend;
    }

    public boolean isExecuteFromBackend() {
        return executeFromBackend;
    }

    @Override
    public Component getPrefix() {
        return prefix;
    }

    @Override
    public Component getQuestion() {
        return question;
    }

    @Override
    public Component getConfirmText() {
        return confirmText;
    }

    @Override
    public Component getDenyText() {
        return denyText;
    }

    @Override
    public Component getConfirmHover() {
        return confirmHover;
    }

    @Override
    public Component getDenyHover() {
        return denyHover;
    }

    @Override
    public String getConfirmCommand() {
        return confirmCommand;
    }

    @Override
    public String getDenyCommand() {
        return denyCommand;
    }

    @Override
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

    public static LunaticDecisionMessage fromStringArray(String[] array) {
        return new LunaticDecisionMessage(
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
