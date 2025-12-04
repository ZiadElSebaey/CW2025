package com.comp2042.logic;

public class Level {
    private final int levelNumber;
    private final String name;
    private final String objective;
    private final int targetLines;
    private final int targetScore;
    private final int timeLimit;
    private final int dropSpeed;
    private boolean completed;
    
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
    
    public int getLevelNumber() {
        return levelNumber;
    }
    
    public String getName() {
        return name;
    }
    
    public String getObjective() {
        return objective;
    }
    
    public int getTargetLines() {
        return targetLines;
    }
    
    public int getTargetScore() {
        return targetScore;
    }
    
    public int getTimeLimit() {
        return timeLimit;
    }
    
    public int getDropSpeed() {
        return dropSpeed;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    public boolean checkObjective(int linesCleared, int score, int timeElapsed) {
        boolean linesMet = targetLines == 0 || linesCleared >= targetLines;
        boolean scoreMet = targetScore == 0 || score >= targetScore;
        boolean timeMet = timeLimit == 0 || timeElapsed <= timeLimit;
        
        return linesMet && scoreMet && timeMet;
    }
}

