package com.comp2042.bricks;

import java.util.List;

public interface BrickGenerator {

    Brick getBrick();

    Brick getNextBrick();
    
    List<Brick> getNextBricks(int count);
}
