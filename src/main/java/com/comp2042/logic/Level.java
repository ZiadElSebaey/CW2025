package com.comp2042.logic;

/**
 * Represents a game level with specific objectives and constraints.
 * Tracks completion status and validates whether objectives have been met.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 */
public class Level {
    private final int levelNumber;
    private final String name;
    private final String objective;
    private final int targetLines;
    private final int targetScore;
    private final int timeLimit;
    private final int dropSpeed;
    private boolean completed;
    
    /**
     * Creates a new Level with the specified parameters.
     * 
     * @param levelNumber the level number
     * @param name the level name
     * @param objective the level objective description
     * @param targetLines the target number of lines to clear (0 if not required)
     * @param targetScore the target score to achieve (0 if not required)
     * @param timeLimit the time limit in seconds (0 if no time limit)
     * @param dropSpeed the drop speed for this level
     */
    public Level(int levelNumber, String name, String objective, int targetLines, int targetScore, int timeLimit, int dropSpeed) {
        this.levelNumber = levelNumber;
        this.name = name;
        this.objective = objective;
        this.targetLines = targetLines;
        this.targetScore = targetScore;
        this.timeLimit = timeLimit;
        this.dropSpeed = dropSpeed;
        this.completed = false;
    }
    
    /**
     * Gets the level number.
     * 
     * @return the level number
     */
    public int getLevelNumber() {
        return levelNumber;
    }
    
    /**
     * Gets the level name.
     * 
     * @return the level name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the level objective description.
     * 
     * @return the objective description
     */
    public String getObjective() {
        return objective;
    }
    
    /**
     * Gets the target number of lines to clear.
     * 
     * @return the target lines (0 if not required)
     */
    public int getTargetLines() {
        return targetLines;
    }
    
    /**
     * Gets the target score to achieve.
     * 
     * @return the target score (0 if not required)
     */
    public int getTargetScore() {
        return targetScore;
    }
    
    /**
     * Gets the time limit for this level.
     * 
     * @return the time limit in seconds (0 if no time limit)
     */
    public int getTimeLimit() {
        return timeLimit;
    }
    
    /**
     * Gets the drop speed for this level.
     * 
     * @return the drop speed
     */
    public int getDropSpeed() {
        return dropSpeed;
    }
    
    /**
     * Checks if this level has been completed.
     * 
     * @return {@code true} if completed, {@code false} otherwise
     */
    public boolean isCompleted() {
        return completed;
    }
    
    /**
     * Sets the completion status of this level.
     * 
     * @param completed the completion status
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    /**
     * Checks if the level objectives have been met with the given game statistics.
     * 
     * @param linesCleared the number of lines cleared
     * @param score the current score
     * @param timeElapsed the time elapsed in seconds
     * @return {@code true} if all objectives are met, {@code false} otherwise
     */
    public boolean checkObjective(int linesCleared, int score, int timeElapsed) {
        boolean linesMet = targetLines == 0 || linesCleared >= targetLines;
        boolean scoreMet = targetScore == 0 || score >= targetScore;
        boolean timeMet = timeLimit == 0 || timeElapsed <= timeLimit;
        
        return linesMet && scoreMet && timeMet;
    }
}

