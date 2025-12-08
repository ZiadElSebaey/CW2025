package com.comp2042.ui;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Factory class for creating common animations.
 */
public final class AnimationFactory {
    
    private AnimationFactory() {}
    
    /**
     * Creates a scale transition animation.
     * 
     * @param node The node to animate
     * @param durationMs Duration in milliseconds
     * @param fromX Starting X scale
     * @param fromY Starting Y scale
     * @param toX Ending X scale
     * @param toY Ending Y scale
     * @return The ScaleTransition
     */
    public static ScaleTransition createScaleTransition(Node node, int durationMs, 
                                                         double fromX, double fromY, 
                                                         double toX, double toY) {
        ScaleTransition transition = new ScaleTransition(Duration.millis(durationMs), node);
        transition.setFromX(fromX);
        transition.setFromY(fromY);
        transition.setToX(toX);
        transition.setToY(toY);
        return transition;
    }
    
    /**
     * Creates a fade transition animation.
     * 
     * @param node The node to animate
     * @param durationMs Duration in milliseconds
     * @param fromValue Starting opacity
     * @param toValue Ending opacity
     * @return The FadeTransition
     */
    public static FadeTransition createFadeTransition(Node node, int durationMs, 
                                                      double fromValue, double toValue) {
        FadeTransition transition = new FadeTransition(Duration.millis(durationMs), node);
        transition.setFromValue(fromValue);
        transition.setToValue(toValue);
        return transition;
    }
    
    /**
     * Creates a rotate transition animation.
     * 
     * @param node The node to animate
     * @param durationSeconds Duration in seconds
     * @param byAngle Angle to rotate by
     * @return The RotateTransition
     */
    public static RotateTransition createRotateTransition(Node node, double durationSeconds, double byAngle) {
        RotateTransition transition = new RotateTransition(Duration.seconds(durationSeconds), node);
        transition.setByAngle(byAngle);
        return transition;
    }
    
    /**
     * Creates a pulse animation (scale up and down).
     * 
     * @param node The node to animate
     * @param durationSeconds Duration in seconds
     * @param scaleFactor The scale factor (e.g., 1.15 for 15% larger)
     * @return The ScaleTransition configured for pulsing
     */
    public static ScaleTransition createPulseAnimation(Node node, double durationSeconds, double scaleFactor) {
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(durationSeconds), node);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(scaleFactor);
        pulse.setToY(scaleFactor);
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.setAutoReverse(true);
        return pulse;
    }
}

