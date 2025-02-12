package de.janschuri.lunaticlib;

import net.kyori.adventure.text.Component;

import java.io.Serializable;

public interface DecisionMessage extends Serializable {

    Component asComponent();

    Component getPrefix();

    Component getQuestion();

    Component getConfirmText();

    Component getDenyText();

    Component getConfirmHover();

    Component getDenyHover();

    String getConfirmCommand();

    String getDenyCommand();

    String[] toStringArray();
}
