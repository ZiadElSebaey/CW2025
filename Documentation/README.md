# COMP2042 Coursework - Tetris Game
My Name : Ziad Ahmed Kamel    ID: 20610050

This is my coursework project for COMP2042. I created a Tetris game using JavaFX with extensive features, creative design elements paying homage to Tetris creator Alexey Pajitnov and background of russia where he was born to acknowledge the user of fun facts and history behind tetris and let him enjoy the first version of tetris as a gamemode and a lot of fun experience, and comprehensive refactoring for maintainability and code quality.

## Quick Feature Highlights

- 🎮 **Four Game Modes**: Free Play, UpsideDown, 1984 (Retro Soviet), and Levels
- 🎨 **Creative Design**: Russian theme honoring Alexey Pajitnov with Electronika-60 monitor aesthetic
- 🎵 **Audio System**: Background music (Russia Music & Christmas) with comprehensive sound effects
- 🧩 **Modern Tetris Features**: Hold piece, hard drop, ghost piece preview, 3-piece queue
- 📊 **Progress Tracking**: Leaderboard, high scores, and level progress persistence
- 🎯 **Level System**: 5 unique levels with specific objectives and unlock progression
- 💬 **Alexey's Dialogue**: Animated motivational dialogue system mid-game
- 🧪 **Comprehensive Testing**: 159 JUnit tests covering all major components
- 🔧 **Well-Refactored**: 11 utility classes eliminating code duplication
- 📚 **Full Documentation**: Complete Javadoc for all classes

## Table of Contents

- [Quick Feature Highlights](#quick-feature-highlights)
- [GitHub Repository](#github-repository)
- [Compilation Instructions](#compilation-instructions)
- [How to Play](#how-to-play)
- [Project Structure](#project-structure)
- [Features Implemented](#features-implemented)
- [New Java Classes](#new-java-classes)
- [Modified Java Classes](#modified-java-classes)
- [Refactoring](#refactoring)
- [Testing](#testing)
- [Technologies Used](#technologies-used)
- [Unexpected Problems Encountered](#unexpected-problems-encountered)
- [Features Not Implemented](#features-not-implemented)
- [Developer Information](#developer-information)
- [Acknowledgments](#acknowledgments)

## GitHub Repository

**My GitHub repository link:**  
https://github.com/ZiadElSebaey/CW2025.git

### Quick Start

```bash
# Clone the repository
git clone https://github.com/ZiadElSebaey/CW2025.git

cd CW2025

# Open in IntelliJ IDEA
# Maven will automatically download dependencies
# Run Main.java
```

**Controls:** Arrow keys or WASD to move, SPACE for hard drop, H for hold, P for pause, M to toggle music

### Prerequisites

- Java Development Kit (JDK) 23 or higher
- JavaFX SDK (managed via Maven - no manual download required)
- IDE (IntelliJ IDEA Community Edition recommended)
- Maven (included via Maven Wrapper)

## Compilation Instructions
### Step-by-Step Compilation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/ZiadElSebaey/CW2025.git
   cd CW2025
   ```

2. **Configure JavaFX Libraries**
   - JavaFX dependencies are automatically managed via Maven (pom.xml)
   - No manual JavaFX SDK download required
   - Dependencies will be automatically downloaded on first build

3. **Compile and Run**

   **Using IntelliJ IDEA (Recommended):**
   - Open the project in IntelliJ IDEA
   - Maven will automatically download dependencies
   - Run `Main.java` directly, or use Maven:
     ```bash
     mvn clean javafx:run
     ```

   **Using Command Line:**
   ```bash
   # Navigate to project directory
   cd /path/to/CW2025
   
   # Compile and run
   mvn clean compile javafx:run
   ```

## How to Play

### Controls

- **Arrow Keys** or **WASD**: Move and rotate pieces
  - ↑ / W: Rotate piece
  - ← / A: Move left
  - → / D: Move right
  - ↓ / S: Soft drop (move down faster)
- **SPACE**: Hard drop (instant drop to bottom)
- **H**: Hold current piece
- **P**: Pause/Resume game

### Game Modes

1. **Free Play Mode**: Standard Tetris gameplay
2. **UpsideDown Mode**: Board rotates 180 degrees, pieces fall upward
3. **1984 Mode**: Retro Soviet aesthetic with period-accurate visuals shows the very first version of tetris created by Alexey Pajitnov
4. **Levels Mode**: 5 unique levels with specific objectives


### Objectives

- **UpsideDown/Free Play**: Clear lines to increase score and level
- **Levels Mode**: Complete level-specific objectives (clear lines, reach score, time limits)
- **1984 Mode**: Survive as long as possible in retro Soviet aesthetic

## Project Structure

```
CW2025/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/comp2042/
│   │   │       ├── Main.java
│   │   │       ├── bricks/          # Brick implementations
│   │   │       ├── logic/            # Game logic
│   │   │       └── ui/               # UI controllers and utilities
│   │   └── resources/
│   │       ├── *.fxml                # FXML layouts
│   │       ├── *.css                  # Stylesheets
│   │       ├── *.png                  # Images
│   │       └── sfx/                   # Sound effects and music
│   └── test/
│       └── java/
│           └── com/comp2042/
│               ├── bricks/            # Brick tests
│               ├── logic/             # Logic tests
│               └── ui/                # UI tests
├── Javadoc/                           # Generated API documentation
├── pom.xml                            # Maven configuration
└── README.md                          # This file
```

## Features Implemented

### Creative Design Elements

1. **Alexey Pajitnov Character & Russian Theme**
   - **Location:** `MainMenuController.java`, `GuiController.java`, resources
   - **Description:** The game pays homage to Tetris creator Alexey Pajitnov, born in Russia. The main menu features Russian-themed music ("Russia Music") and celebrates the game's Soviet origins
   - **Rationale:** Historical accuracy and tribute to the game's creator
   - **Implementation:** 
     - Russian music track (`Russiamusic.mp3`) loops every 17.5 seconds
     - Alexey Pajitnov character image displayed in main menu
     - Russian cultural elements integrated into UI design

2. **Electronika-60 Monitor Display (How to Play)**
   - **Location:** `HowToPlayController.java`, resources
   - **Description:** The "How to Play" tutorial is displayed in the style of the Electronika-60 computer monitor that Alexey Pajitnov used when creating Tetris
   - **Rationale:** Authentic historical reference to the original development environment
   - **Implementation:**
     - Electronika-60 monitor image as background
     - Retro terminal-style text display
     - Step-by-step instructions matching the original development era aesthetic

3. **1984 Gamemode - Retro Soviet Aesthetic**
   - **Location:** `Tetris1984Controller.java`, `GuiController.java`, resources
   - **Description:** Special game mode inspired by the 1984 era when Tetris was created, featuring retro Soviet-style graphics, monochrome display, and period-accurate visual effects
   - **Rationale:** Immersive historical experience matching the original game's era
   - **Implementation:**
     - Custom 1984-themed background and frame graphics
     - Monochrome/limited color palette
     - Special UI styling with retro computer aesthetics
     - Unique game over sound effect (`gameover1984.mp3`)
     - Custom panel sizing and positioning for authentic feel

4. **Animated Dialogue System - Alexey's Motivation**
   - **Location:** `GuiController.java`
   - **Description:** Alexey Pajitnov appears mid-game with animated dialogue text, providing motivation, tips, and encouragement to players. This unique feature brings the game's creator into the gameplay experience, offering historical context, strategic advice, and motivational messages at key moments during gameplay.
   - **Rationale:** Adds personality and engagement, making the game feel more alive and connected to its creator. This creative element educates players about Tetris history while providing an immersive experience.
   - **Implementation:**
     - `dialogueTextTimeline` manages animated text display with smooth transitions
     - Character dialogue appears at strategic moments (score milestones, level completions, challenging situations)
     - Smooth fade-in/fade-out animations using JavaFX transitions
     - Multiple dialogue variations based on game state (score, level, difficulty)
     - Context-aware messages that adapt to player performance
     - Integrated with game pause/resume system (dialogue pauses when game is paused)
     - Non-intrusive display that doesn't interfere with gameplay
     - Historical facts and fun trivia about Tetris and its creation

### Core Game Features

5. **Hold Piece System**
   - **Location:** `SimpleBoard.java` and `GuiController.java`
   - **Description:** Players can press 'H' to hold the current piece and swap it with a previously held piece
   - **Rationale:** Standard Tetris feature that adds strategic depth by allowing players to save pieces for later use
   - **Implementation:** 
     - Added `heldBrick` field and `canHold` flag in `SimpleBoard`
     - Added `holdPieceGrid` UI component in FXML
     - Prevents multiple holds per drop to avoid exploitation
     - Visual display shows held piece in dedicated panel

6. **Hard Drop Feature**
   - **Location:** `GameController.java` and `GuiController.java`
   - **Description:** Press SPACE to instantly drop the piece to the bottom and lock it
   - **Rationale:** Modern Tetris games require fast placement mechanics for competitive play
   - **Implementation:** 
     - `onHardDropEvent()` method moves piece down repeatedly until collision
     - Awards points based on distance dropped
     - Immediately locks piece and spawns new one
     - Sound effect (`fall.mp3`) plays on hard drop (except in 1984 mode)

7. **Ghost Piece Preview**
   - **Location:** `GuiController.java`
   - **Description:** Semi-transparent preview showing where the current piece will land
   - **Rationale:** Essential visual aid for precise placement in modern Tetris
   - **Implementation:** 
     - `ghostMatrix` array stores ghost piece rectangles
     - `updateGhostBlockVisibility()` calculates lowest valid position
     - Renders with reduced opacity using `ColorManager.createGhostColor()`
     - Can be toggled on/off in settings

8. **Next Piece Queue (3 pieces)**
   - **Location:** `SimpleBoard.java` and `GuiController.java`
   - **Description:** Displays the next 3 pieces that will spawn
   - **Rationale:** Allows players to plan ahead, standard in modern Tetris
   - **Implementation:** 
     - Queue system using `ArrayDeque` in `RandomBrickGenerator`
     - `getNextBricks(int count)` ensures queue always has preview pieces
     - Visual display in right panel with `nextBrickData`, `nextBrickData2`, `nextBrickData3`
     - Maintains fair distribution through `RandomBrickGenerator`

9. **Level Progression System (Levels Mode)**
   - **Location:** `LevelManager.java`, `Level.java`, `GameController.java`
   - **Description:** Dedicated "Levels" game mode with 5 unique levels, each with specific objectives (clear lines, time limits, score targets, special challenges)
   - **Rationale:** Provides structured gameplay with clear goals and progression
   - **Implementation:** 
     - `LevelManager` manages 5 predefined levels
     - Each level has unique objectives (lines, score, time, special conditions)
     - Level unlock system based on previous level completion
     - Progress persistence via `LevelProgressManager`
     - Level completion sound effect (`next-level-tetris-sounds.mp3`)
     - Visual level selection screen with unlock indicators

10. **Multiple Game Modes**
    - **Location:** `GameMode.java`, `MainMenuController.java`, `GamemodesController.java`
    - **Description:** Four distinct game modes: Free Play, UpsideDown, 1984 (retro Soviet theme), and Levels
    - **Rationale:** Variety and challenge for different player preferences
    - **Implementation:**
      - `GameMode` enum manages mode selection
      - Free Play mode: Standard Tetris gameplay
      - UpsideDown mode: Board rotates 180 degrees, pieces fall upward
      - 1984 mode: Retro aesthetic with custom UI and sounds
      - Levels mode: 5 unique levels with specific objectives
      - Mode-specific UI adjustments and visual styling

11. **Background Music System**
    - **Location:** `MusicManager.java` and `Main.java`
    - **Description:** Looping background music with two selectable tracks: "Russia Music" and "Christmas"
    - **Rationale:** Enhances game atmosphere and player immersion
    - **Implementation:** 
      - Uses JavaFX `MediaPlayer` for seamless playback
      - "Russia Music" loops every 17.5 seconds with custom `Timeline`
      - "Christmas" track loops indefinitely
      - Music selection dialog for track switching
      - Volume control and pause/resume functionality
      - Settings persistence via `SettingsManager`

12. **Sound Effects System**
    - **Location:** `SoundManager.java`
    - **Description:** Comprehensive sound effects for all game events (countdown, line clear, hard drop, level complete, game over)
    - **Rationale:** Audio feedback improves game feel and player satisfaction
    - **Implementation:** 
      - Centralized `SoundManager` class with pre-loaded audio clips
      - Sound effects: countdown (`3-2-1.mp3`), line clear (`glass-break.mp3`), hard drop (`fall.mp3`), level complete (`next-level-tetris-sounds.mp3`), game over (`gameover.mp3`, `gameover1984.mp3`)
      - Mode-specific sound selection (1984 mode uses different game over sound)
      - Mute toggle functionality tied to settings
      - Respects `SettingsManager.isSoundEffectsEnabled()` setting

13. **Music/Sound Toggle (Settings)**
    - **Location:** `SettingsController.java`, `SettingsManager.java`
    - **Description:** Independent toggles for music and sound effects in settings menu
    - **Rationale:** Player choice for audio control
    - **Implementation:** 
      - Separate settings for music and sound effects
      - Settings persist across sessions
      - Visual indicators show ON/OFF state
      - Settings saved to `~/.tetrisjfx/settings.dat`

14. **Enhanced UI/UX**
    - **Location:** `GuiController.java`, FXML files
    - **Description:** Professional game interface with animated backgrounds, countdown panels, pause system, game over panels, and comprehensive UI elements
    - **Rationale:** Professional game interface with clear feedback
    - **Implementation:**
      - `AnimatedBackground` for dynamic menu backgrounds
      - `CountdownPanel` with 3-2-1 countdown before game starts
      - `PausePanel` with resume/restart/settings/leaderboard options
      - `GameOverPanel` with score submission and leaderboard integration
      - `LevelCompletePanel` for level mode completion
      - Responsive layout with `WindowConstants` for consistent sizing
      - Color-coded UI elements using `ColorManager`

15. **Leaderboard System**
    - **Location:** `LeaderboardManager.java`, `LeaderboardPanel.java`
    - **Description:** Top 10 player scores with case-insensitive name matching and automatic sorting and if input same user name and got higherscore, their score will be automatically updated on their profile
    - **Rationale:** Competitive element and player achievement tracking
    - **Implementation:**
      - Persistent leaderboard stored in `~/.tetrisjfx/leaderboard.txt`
      - Updates existing entries if new score is higher
      - Sorted by score descending
      - Maximum 10 entries maintained
      - Visual leaderboard panel with clear/reset functionality

16. **High Score Tracking**
    - **Location:** `HighScoreManager.java`
    - **Description:** Tracks highest score achieved with player name
    - **Rationale:** Personal best tracking
    - **Implementation:**
      - Persistent storage in `~/.tetrisjfx/highscore.dat`
      - Updates only when new high score is achieved
      - Displays high score holder name in game UI

17. **Level Progress Persistence**
    - **Location:** `LevelProgressManager.java`
    - **Description:** Saves and loads level completion status across game sessions
    - **Rationale:** Allows players to continue their progress
    - **Implementation:**
      - Progress stored in `~/.tetrisjfx/levelprogress.dat`
      - Automatically loads on game start
      - Reset functionality for starting fresh

18. **Countdown System**
    - **Location:** `CountdownPanel.java`
    - **Description:** 3-2-1 countdown before game starts with sound effect
    - **Rationale:** Prevents sudden game start and adds anticipation
    - **Implementation:**
      - Animated countdown with scale and fade effects
      - Sound effect plays (`3-2-1.mp3`) if sound effects enabled
      - Slower countdown for Free Play and Levels modes
      - Special styling for 1984 mode

19. **UpsideDown Mode Rotation**
    - **Location:** `GuiController.java`
    - **Description:** Entire game board rotates 180 degrees for UpsideDown gameplay
    - **Rationale:** Unique and harder challenge mode
    - **Implementation:**
      - `setupRootPaneRotation()` applies rotation transform
      - Pieces fall upward instead of down
      - All UI elements rotate accordingly
      - Special rotation angle from `AnimationConstants`

20. **Settings Persistence**
    - **Location:** `SettingsManager.java`
    - **Description:** Saves user preferences (ghost block, music enabled, sound effects enabled, selected music track)
    - **Rationale:** Remembers player preferences across sessions
    - **Implementation:**
      - Settings stored in `~/.tetrisjfx/settings.dat`
      - Automatically loads on application start
      - Updates immediately when changed

## New Java Classes

### Utility Classes

1. **SoundManager.java**
   - **Location:** `src/main/java/com/comp2042/ui/SoundManager.java`
   - **Purpose:** Centralized management of all game sound effects
   - **Key Features:**
     - Static utility class with pre-loaded `MediaPlayer` instances
     - Sound effect mapping (fall, glass-break, gameover, gameover1984, next-level-tetris-sounds, 3-2-1)
     - Respects `SettingsManager.isSoundEffectsEnabled()`
     - Volume control per sound effect
     - Automatic resource cleanup with `dispose()` method

2. **MusicManager.java**
   - **Location:** `src/main/java/com/comp2042/ui/MusicManager.java`
   - **Purpose:** Handles background music playback with track switching
   - **Key Features:**
     - Two selectable tracks: "Russia Music" and "Christmas"
     - "Russia Music" loops every 17.5 seconds via custom `Timeline`
     - "Christmas" track loops indefinitely
     - Pause/resume with position tracking
     - Volume control
     - Settings integration for track persistence

3. **ColorManager.java**
   - **Location:** `src/main/java/com/comp2042/ui/ColorManager.java`
   - **Purpose:** Centralized color management for game pieces and UI
   - **Key Features:**
     - Maps color indices to `Paint` objects
     - Special 1984 mode color handling
     - Ghost color creation with opacity adjustment
     - RGB color preservation

4. **AnimationFactory.java**
   - **Location:** `src/main/java/com/comp2042/ui/AnimationFactory.java`
   - **Purpose:** Factory for creating common JavaFX animations
   - **Key Features:**
     - Fade transitions
     - Scale transitions
     - Rotate transitions
     - Pulse animations
     - Reusable animation creation methods

5. **ResourceLoader.java**
   - **Location:** `src/main/java/com/comp2042/ui/ResourceLoader.java`
   - **Purpose:** Centralized resource loading from classpath
   - **Key Features:**
     - `getResource(String path)` for URL retrieval
     - `getResourceAsStream(String path)` for InputStream
     - Null and empty string handling
     - Consistent error handling

6. **FontLoader.java**
   - **Location:** `src/main/java/com/comp2042/ui/FontLoader.java`
   - **Purpose:** Centralized font loading with fallback
   - **Key Features:**
     - Loads fonts from resources
     - Fallback to system font if loading fails
     - Size parameter support
     - Error handling

7. **SceneNavigator.java**
   - **Location:** `src/main/java/com/comp2042/ui/SceneNavigator.java`
   - **Purpose:** Centralized FXML scene loading and navigation
   - **Key Features:**
     - `loadScene()` for FXML loading with controller setup
     - `navigateToScene()` for stage navigation
     - Consumer-based controller configuration
     - Consistent error handling

8. **WindowConstants.java**
   - **Location:** `src/main/java/com/comp2042/ui/WindowConstants.java`
   - **Purpose:** Centralized window dimension constants
   - **Key Features:**
     - `WINDOW_WIDTH`, `WINDOW_HEIGHT`
     - `DIALOG_WIDTH`, `DIALOG_HEIGHT`
     - Consistent sizing across application

9. **GameUIConstants.java**
   - **Location:** `src/main/java/com/comp2042/ui/GameUIConstants.java`
   - **Purpose:** UI-related constants (brick sizes, board dimensions, padding)
   - **Key Features:**
     - Brick size constants
     - Board dimension constants
     - Padding and offset values
     - UI spacing constants

10. **AnimationConstants.java**
    - **Location:** `src/main/java/com/comp2042/ui/AnimationConstants.java`
    - **Purpose:** Animation-related constants
    - **Key Features:**
      - Rotation angles
      - Animation durations
      - Ghost opacity values
      - Default drop intervals

11. **GameMode.java**
    - **Location:** `src/main/java/com/comp2042/ui/GameMode.java`
    - **Purpose:** Enum representing different game modes
    - **Key Features:**
      - `NORMAL`, `INVERTED` (UpsideDown), `MODE_1984` enum values
      - `getValue()`, `fromString()`, `matches()` methods
      - Type-safe mode handling

### Manager Classes

12. **LevelManager.java**
    - **Location:** `src/main/java/com/comp2042/ui/LevelManager.java`
    - **Purpose:** Manages game levels for levels mode
    - **Key Features:**
      - 5 predefined levels with unique objectives
      - Level unlock system
      - Level retrieval by number
      - Reset functionality

13. **LevelProgressManager.java**
    - **Location:** `src/main/java/com/comp2042/ui/LevelProgressManager.java`
    - **Purpose:** Manages level progress persistence
    - **Key Features:**
      - Saves completed levels to file
      - Loads progress on initialization
      - Reset functionality
      - Directory creation

14. **LeaderboardManager.java**
    - **Location:** `src/main/java/com/comp2042/ui/LeaderboardManager.java`
    - **Purpose:** Manages leaderboard with top 10 scores
    - **Key Features:**
      - Case-insensitive name matching
      - Automatic score sorting
      - Maximum 10 entries
      - Persistent storage
      - `LeaderboardEntry` record for data structure

15. **HighScoreManager.java**
    - **Location:** `src/main/java/com/comp2042/ui/HighScoreManager.java`
    - **Purpose:** Manages high score persistence
    - **Key Features:**
      - Tracks highest score with player name
      - Updates only when new high score is achieved
      - Persistent storage
      - Reset functionality

16. **SettingsManager.java**
    - **Location:** `src/main/java/com/comp2042/ui/SettingsManager.java`
    - **Purpose:** Manages persistent application settings
    - **Key Features:**
      - Ghost block toggle
      - Music enabled/disabled
      - Sound effects enabled/disabled
      - Selected music track
      - Automatic save on change

### UI Panel Classes

17. **GameOverPanel.java**
    - **Location:** `src/main/java/com/comp2042/ui/GameOverPanel.java`
    - **Purpose:** Panel displayed when game ends
    - **Key Features:**
      - Score display
      - High score information
      - Player name input
      - Leaderboard submission
      - Mode-specific styling

18. **CountdownPanel.java**
    - **Location:** `src/main/java/com/comp2042/ui/CountdownPanel.java`
    - **Purpose:** 3-2-1 countdown animation before game starts
    - **Key Features:**
      - Animated countdown with scale effects
      - Sound effect integration
      - Slower timing for certain modes
      - 1984 mode styling

19. **PausePanel.java**
    - **Location:** `src/main/java/com/comp2042/ui/PausePanel.java`
    - **Purpose:** Pause menu with game options
    - **Key Features:**
      - Resume button
      - Restart button
      - Settings access
      - Leaderboard access
      - Main menu return

20. **LevelCompletePanel.java**
    - **Location:** `src/main/java/com/comp2042/ui/LevelCompletePanel.java`
    - **Purpose:** Panel for level completion in levels mode
    - **Key Features:**
      - Congratulations message
      - Next level button
      - Levels menu return
      - Main menu return

21. **NotificationPanel.java**
    - **Location:** `src/main/java/com/comp2042/ui/NotificationPanel.java`
    - **Purpose:** Score notification animations
    - **Key Features:**
      - Animated score bonuses
      - Visual effects (glow, rotation, scale)
      - Auto-removal after animation

22. **LeaderboardPanel.java**
    - **Location:** `src/main/java/com/comp2042/ui/LeaderboardPanel.java`
    - **Purpose:** Leaderboard display panel
    - **Key Features:**
      - Top 10 scores display
      - Clear leaderboard option
      - Close functionality

### Dialog Classes

23. **MusicSelectionDialog.java**
    - **Location:** `src/main/java/com/comp2042/ui/MusicSelectionDialog.java`
    - **Purpose:** Dialog for selecting background music track
    - **Key Features:**
      - "Russia Music" and "Christmas" options
      - Settings integration
      - Modal dialog

24. **PlayerNameDialog.java**
    - **Location:** `src/main/java/com/comp2042/ui/PlayerNameDialog.java`
    - **Purpose:** Dialog for entering player name
    - **Key Features:**
      - Name input field
      - Guest option
      - Enter key submission

## Modified Java Classes

1. **Main.java**
   - **Changes:**
     - Added `MusicManager.initialize()` and music startup
     - Added `SoundManager.dispose()` on shutdown
     - Replaced hardcoded dimensions with `WindowConstants`
     - Replaced resource loading with `ResourceLoader`
     - Replaced font loading with `FontLoader`
     - Replaced FXML loading with `SceneNavigator`
   - **Rationale:** Centralized resource management and consistent UI sizing

2. **GameController.java**
   - **Changes:**
     - Implements `InputEventListener` interface
     - Handles all game input events (down, left, right, rotate, hard drop, hold)
     - Manages game state and coordinates with `GuiController`
     - Integrated with `SimpleBoard` for game logic
   - **Rationale:** Separation of concerns - game logic separate from UI

3. **GuiController.java**
   - **Changes:**
     - Massive refactoring to use utility classes (`ColorManager`, `AnimationFactory`, `GameUIConstants`, `AnimationConstants`)
     - Integrated `SoundManager` for all sound effects
     - Integrated `MusicManager` for background music
     - Added dialogue text animation system
     - Mode-specific UI handling (Free Play, UpsideDown, 1984)
     - Ghost block rendering with `ColorManager`
     - Next pieces display (3 pieces)
     - Hold piece display
     - Countdown system integration
     - Game over panel integration
     - Pause panel integration
     - Level complete panel integration
     - Settings integration
   - **Rationale:** Comprehensive UI management with code reuse and maintainability

4. **SimpleBoard.java**
   - **Changes:**
     - Integrated with `RandomBrickGenerator` for brick queue
     - Hold functionality with `heldBrick` and `canHold` flag
     - `getViewData()` returns next 3 pieces and held piece
     - Board state management
   - **Rationale:** Core game logic with modern Tetris features

5. **MatrixOperations.java**
   - **Changes:**
     - Fixed coordinate system (X/Y consistency)
     - `checkRemoving()` with row clearing logic
     - `intersect()` for collision detection
     - `merge()` for brick placement
     - `copy()` and `deepCopyList()` for array operations
   - **Rationale:** Core matrix operations with bug fixes

6. **ViewData.java**
   - **Changes:**
     - Added `nextBrickData2` and `nextBrickData3` for 3-piece preview
     - Added `holdBrickData` for hold piece display
     - Immutable data class with deep copying
   - **Rationale:** Supports modern Tetris UI requirements

7. **InputEventListener.java**
   - **Changes:**
     - Added `onHardDropEvent()` method
     - Added `onHoldEvent()` method
     - All methods return appropriate data structures
   - **Rationale:** Interface extension for new features

8. **SettingsController.java**
   - **Changes:**
     - Integrated `MusicManager` for music control
     - Integrated `SettingsManager` for persistence
     - Replaced hardcoded values with `WindowConstants`
     - Replaced resource loading with `ResourceLoader`
     - Replaced FXML loading with `SceneNavigator`
   - **Rationale:** Consistent code patterns and centralized management

9. **MainMenuController.java**
   - **Changes:**
     - Replaced hardcoded dimensions with `WindowConstants`
     - Replaced resource loading with `ResourceLoader`
     - Replaced FXML loading with `SceneNavigator`
     - Updated game mode checks to use `GameMode` enum
   - **Rationale:** Code consistency and maintainability

10. **All Other Controllers** (`GamemodesController.java`, `Tetris1984Controller.java`, `LevelsController.java`, `HowToPlayController.java`)
    - **Changes:**
      - Replaced hardcoded dimensions with `WindowConstants`
      - Replaced resource loading with `ResourceLoader`
      - Replaced FXML loading with `SceneNavigator`
      - Updated to use `GameMode` enum
    - **Rationale:** Consistent refactoring across all UI controllers

## Refactoring

### Code Duplication Elimination

1. **Sound Effects Management**
   - **Before:** Sound effect playback code duplicated across multiple controllers
   - **After:** Centralized in `SoundManager.java`
   - **Impact:** Reduced code duplication, easier maintenance, consistent sound behavior

2. **Resource Loading**
   - **Before:** `getClass().getClassLoader().getResource()` repeated in every controller
   - **After:** Centralized in `ResourceLoader.java`
   - **Impact:** Consistent error handling, easier resource path management

3. **Font Loading**
   - **Before:** `Font.loadFont()` calls scattered throughout codebase
   - **After:** Centralized in `FontLoader.java` with fallback handling
   - **Impact:** Consistent font loading, graceful degradation on failure

4. **FXML Scene Loading**
   - **Before:** Manual `FXMLLoader` setup duplicated in every controller
   - **After:** Centralized in `SceneNavigator.java`
   - **Impact:** Consistent scene navigation, reduced boilerplate code

5. **Window Dimensions**
   - **Before:** Hardcoded `720, 680` and `400, 300` values throughout codebase
   - **After:** Centralized in `WindowConstants.java`
   - **Impact:** Single source of truth for window sizing, easier to modify

6. **Color Management**
   - **Before:** Color creation logic duplicated in `GuiController`
   - **After:** Centralized in `ColorManager.java`
   - **Impact:** Consistent color handling, easier to add new color schemes

7. **Animation Creation**
   - **Before:** Animation setup code repeated for similar animations
   - **After:** Centralized in `AnimationFactory.java`
   - **Impact:** Reusable animation patterns, consistent animation behavior

8. **UI Constants**
   - **Before:** Magic numbers scattered throughout UI code
   - **After:** Centralized in `GameUIConstants.java` and `AnimationConstants.java`
   - **Impact:** Self-documenting code, easier to adjust UI parameters

9. **Game Mode Handling**
   - **Before:** String comparisons (`"normal"`, `"inverted"` (UpsideDown), `"1984"`) throughout code
   - **After:** Type-safe `GameMode` enum
   - **Impact:** Compile-time safety, easier to add new modes

### Code Organization Improvements

- **Package Structure:** Clear separation between `bricks`, `logic`, and `ui` packages
- **Utility Classes:** All helper classes grouped logically
- **Manager Classes:** All persistence and state management classes clearly named
- **UI Components:** Panels and dialogs separated from controllers

### Benefits of Refactoring

- **Maintainability:** Changes to resource loading, colors, or animations only need to be made in one place
- **Readability:** Code is more self-documenting with named constants
- **Testability:** Utility classes can be tested independently
- **Consistency:** All controllers follow the same patterns
- **Reduced Bugs:** Centralized logic reduces chance of inconsistencies

## Testing

### JUnit Test Coverage

The project includes comprehensive JUnit tests covering:

1. **Core Logic Tests** (`MatrixOperationsTest.java`)
   - Matrix operations (intersect, merge, copy)
   - Row clearing logic
   - Boundary checking
   - Score calculations
   - Level management
   - High score persistence
   - Settings persistence
   - Clear row data structures
   - View data structures
   - Move events
   - Next shape info

2. **Brick Tests** (`BrickTest.java`)
   - Brick generation (`RandomBrickGenerator`)
   - Brick rotation (`BrickRotator`)
   - Hold functionality
   - Queue consistency
   - Next shape information
   - Brick spawning

3. **UI Utility Tests**
   - `ColorManagerTest.java` - Color mapping and ghost colors
   - `GameModeTest.java` - Mode enum functionality
   - `SettingsManagerTest.java` - Settings persistence
   - `LevelProgressManagerTest.java` - Level progress tracking
   - `ResourceLoaderTest.java` - Resource loading
   - `FontLoaderTest.java` - Font loading
   - `LeaderboardManagerTest.java` - Leaderboard functionality
   - `SoundManagerTest.java` - Sound effect management
   - `MusicManagerTest.java` - Music playback
**Test summary**
Total: 159 tests (all passing)
Breakdown by category


Core Game Logic (60 tests - 37.7%)
Matrix operations (12)
Line clearing (5)
SimpleBoard operations (20)
Score management (4)
Level system (7)
Data structures (6)
Integration (3)
Manager tests (2)

Brick System (25 tests - 15.7%)
Brick generation (7)
Brick rotation (8)
Hold functionality (5)
Board integration (3)

UI Utilities (29 tests - 18.2%)
ColorManager (9)
GameMode (10)
ResourceLoader (5)
FontLoader (5)

Managers (35 tests - 22.0%)
SettingsManager (6)
LevelProgressManager (4)
LeaderboardManager (13)
SoundManager (10)
MusicManager (12)


Test files
MatrixOperationsTest.java - 60 tests
BrickTest.java - 25 tests
ColorManagerTest.java - 9 tests
GameModeTest.java - 10 tests
SettingsManagerTest.java - 6 tests
LevelProgressManagerTest.java - 4 tests
ResourceLoaderTest.java - 5 tests
FontLoaderTest.java - 5 tests
LeaderboardManagerTest.java - 13 tests
SoundManagerTest.java - 10 tests
MusicManagerTest.java - 12 tests

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=MatrixOperationsTest

# Run with coverage (if configured)
mvn test jacoco:report
```

## Technologies Used

- **Java 23**: Programming language
- **JavaFX 21.0.6**: UI framework
  - `javafx-controls`: UI controls
  - `javafx-fxml`: FXML scene builder
  - `javafx-media`: Audio playback
- **Maven**: Build tool and dependency management
- **JUnit 5**: Testing framework
- **Maven Javadoc Plugin**: API documentation generation

## Unexpected Problems Encountered

1. **Coordinate System Confusion (X/Y Swap Bug)**
   - **Problem:** Original code inconsistently used X/Y coordinates in `MatrixOperations.intersect()` and `merge()` methods
   - **Location:** `MatrixOperations.java`
   - **Solution:** Standardized coordinate system: j = column = X, i = row = Y
   - **Time Spent:** ~3 hours debugging and testing

2. **1984 Mode Panel Positioning**
   - **Problem:** Initial panel sizing caused bricks to not reach bottom or touch borders incorrectly
   - **Location:** `GuiController.java`, `Tetris1984Controller.java`
   - **Solution:** Multiple iterations of panel size and position adjustments to achieve correct gameplay area
   - **Time Spent:** ~4 hours

3. **Music Looping for Russia Music**
   - **Problem:** Standard MediaPlayer looping didn't work for 17.5-second clip requirement
   - **Location:** `MusicManager.java`
   - **Solution:** Implemented custom `Timeline` to loop every 17.5 seconds
   - **Time Spent:** ~2 hours

4. **Sound Effects Not Playing in 1984 Mode**
   - **Problem:** Some sound effects were playing in 1984 mode when they shouldn't
   - **Location:** `GuiController.java`, `SoundManager.java`
   - **Solution:** Added mode checks before playing certain sounds (game over, line clear)
   - **Time Spent:** ~1 hour

5. **Javadoc Cross-Package References**
   - **Problem:** `@see` tags referencing classes in different packages caused compilation errors
   - **Location:** Multiple files with Javadoc
   - **Solution:** Used fully qualified class names in `@see` tags (e.g., `@see com.comp2042.logic.GameController`)
   - **Time Spent:** ~1 hour

6. **Test Failures After Refactoring**
   - **Problem:** Multiple JUnit tests failed after utility class refactoring
   - **Location:** Test files
   - **Solution:** Updated test assertions to match new implementation patterns, fixed duplicate test methods
   - **Time Spent:** ~3 hours

7. **Duplicate Code in Test Files**
   - **Problem:** Entire class definitions were duplicated in test files causing compilation errors
   - **Location:** Multiple test files
   - **Solution:** Removed duplicate class definitions
   - **Time Spent:** ~1 hour

8. **Maven Javadoc Plugin Configuration**
   - **Problem:** Invalid `<target>` element in javadoc plugin configuration
   - **Location:** `pom.xml`
   - **Solution:** Removed `<target>` element (only `<source>` is valid for javadoc plugin)
   - **Time Spent:** ~30 minutes

9. **Line Clear Animation Implementation**
   - **Problem:** Attempted to implement visual animations when lines are cleared (fade-out, scale, particle effects) but encountered performance issues and timing conflicts with the game loop
   - **Location:** `GuiController.java`, line clearing logic
   - **Solution:** Decided to prioritize game performance and stability over visual effects. Line clearing now happens instantly without animation to maintain smooth gameplay, especially at higher levels with faster drop speeds
   - **Time Spent:** ~4 hours attempting various animation approaches before deciding to remove the feature

## Features Not Implemented

1. **T-Spin Detection and Bonus Scoring**
   - **Reason:** Requires complex rotation system mechanics and advanced collision detection algorithms. Current rotation system uses simple matrix transformations.

2. **Online Multiplayer**
   - **Reason:** Requires backend server, network protocols, and synchronization. Beyond scope of local application.

3. **Replay System**
   - **Reason:** Would require recording all user inputs with timestamps and implementing playback controls. Deemed non-essential for core gameplay.

4. **Advanced Particle Effects**
   - **Reason:** Time prioritization - focused on functional features and audio feedback rather than purely cosmetic enhancements.

5. **Custom Piece Colors**
   - **Reason:** Current color system works well with game modes. Custom colors would require additional UI and settings management.

## Developer Information

**Developer:** Ziad ElSebaey  
**Student ID:** 20610050  
**Course:** COMP2042  
**Submission Date:** 8/12/2025
**Assignment:** Tetris Game - CW2025

## Acknowledgments

- **Alexey Pajitnov** - Creator of Tetris, inspiration for the Russian theme and historical elements waiting for you in my game!
- **JavaFX Team** - UI Framework
- **OpenJFX Project** - JavaFX Libraries
- **JUnit Team** - Testing Framework
- **Window size** - 720x680

