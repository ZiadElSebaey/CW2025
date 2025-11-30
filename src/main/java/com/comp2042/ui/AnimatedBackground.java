package com.comp2042.ui;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnimatedBackground extends Pane {

    private static final int SNOWFLAKE_COUNT = 80;
    private static final int TETRIS_BLOCK_COUNT = 6;

    private final Canvas canvas;
    private final List<Particle> snowflakes = new ArrayList<>();
    private final List<TetrisBlock> tetrisBlocks = new ArrayList<>();
    private final Random random = new Random();
    private AnimationTimer animationTimer;

    public AnimatedBackground(double width, double height) {
        canvas = new Canvas(width, height);
        getChildren().add(canvas);
        initParticles(width, height);
        startAnimation();
    }

    private void initParticles(double width, double height) {
        for (int i = 0; i < SNOWFLAKE_COUNT; i++) {
            snowflakes.add(new Particle(
                random.nextDouble() * width,
                random.nextDouble() * height,
                random.nextDouble() * 2 + 1,
                random.nextDouble() * 1.5 + 0.5
            ));
        }

        Color[] coldColors = {
            Color.rgb(100, 200, 255, 0.3),
            Color.rgb(150, 220, 255, 0.25),
            Color.rgb(200, 240, 255, 0.2),
            Color.rgb(80, 180, 220, 0.3),
            Color.rgb(120, 160, 255, 0.25)
        };

        int[][] shapes = {
            {1, 1, 1, 1},
            {1, 1, 0, 1, 1},
            {1, 1, 1, 0, 1},
            {0, 1, 1, 1, 1},
            {1, 1, 1, 1, 0, 0, 1}
        };

        for (int i = 0; i < TETRIS_BLOCK_COUNT; i++) {
            tetrisBlocks.add(new TetrisBlock(
                random.nextDouble() * width,
                random.nextDouble() * height - height,
                coldColors[random.nextInt(coldColors.length)],
                shapes[random.nextInt(shapes.length)],
                random.nextDouble() * 0.8 + 0.3
            ));
        }
    }

    private void startAnimation() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                updateAndDrawSnow(gc);
                updateAndDrawTetrisBlocks(gc);
            }
        };
        animationTimer.start();
    }

    private void updateAndDrawSnow(GraphicsContext gc) {
        gc.setFill(Color.rgb(220, 240, 255, 0.8));
        
        for (Particle snow : snowflakes) {
            snow.y += snow.speed;
            snow.x += Math.sin(snow.y * 0.02) * 0.5;

            if (snow.y > canvas.getHeight()) {
                snow.y = -5;
                snow.x = random.nextDouble() * canvas.getWidth();
            }

            gc.fillOval(snow.x, snow.y, snow.size, snow.size);
        }
    }

    private void updateAndDrawTetrisBlocks(GraphicsContext gc) {
        int blockSize = 18;
        
        for (TetrisBlock block : tetrisBlocks) {
            block.y += block.speed;

            if (block.y > canvas.getHeight() + 100) {
                block.y = -150;
                block.x = random.nextDouble() * canvas.getWidth();
            }

            gc.setFill(block.color);
            drawTetrisShape(gc, block.x, block.y, block.shape, blockSize);
        }
    }

    private void drawTetrisShape(GraphicsContext gc, double x, double y, int[] shape, int size) {
        int col = 0;
        int row = 0;
        for (int cell : shape) {
            if (cell == 1) {
                gc.fillRoundRect(x + col * (size + 2), y + row * (size + 2), size, size, 4, 4);
            }
            col++;
            if (col > 1) {
                col = 0;
                row++;
            }
        }
    }

    public void stop() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }

    private static class Particle {
        double x, y, size, speed;
        
        Particle(double x, double y, double size, double speed) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.speed = speed;
        }
    }

    private static class TetrisBlock {
        double x, y, speed;
        Color color;
        int[] shape;

        TetrisBlock(double x, double y, Color color, int[] shape, double speed) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.shape = shape;
            this.speed = speed;
        }
    }
}

