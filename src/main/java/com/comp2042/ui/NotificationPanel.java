package com.comp2042.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class NotificationPanel extends BorderPane {

    private static final int MIN_HEIGHT = 200;
    private static final int MIN_WIDTH = 220;
    private static final double GLOW_LEVEL = 0.6;
    private static final int FADE_DURATION_MS = 2000;
    private static final int TRANSLATE_DURATION_MS = 2500;
    private static final int TRANSLATE_OFFSET = -40;

    public NotificationPanel(String text) {
        setMinHeight(MIN_HEIGHT);
        setMinWidth(MIN_WIDTH);
        Label scoreLabel = new Label(text);
        scoreLabel.getStyleClass().add("bonusStyle");
        scoreLabel.setEffect(new Glow(GLOW_LEVEL));
        scoreLabel.setTextFill(Color.WHITE);
        setCenter(scoreLabel);
    }

    public void showScore(ObservableList<Node> list) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(FADE_DURATION_MS), this);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(TRANSLATE_DURATION_MS), this);
        translateTransition.setToY(getLayoutY() + TRANSLATE_OFFSET);

        ParallelTransition transition = new ParallelTransition(translateTransition, fadeTransition);
        transition.setOnFinished(event -> list.remove(this));
        transition.play();
    }
}
