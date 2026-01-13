# Scope - Java Game Framework

A lightweight, high-performance game framework built on Java Swing for rapid game development.

## Overview

**Scope** is a Java-based game framework that provides essential features for 2D game development, including:
- Stable game loop with fixed timestep
- Comprehensive input handling (keyboard & mouse)
- Dynamic viewport/camera management
- Visual effects system (after-images, popups)
- Real-time system monitoring

---

## Features

### 1. **Game Loop & Timing**
- Fixed 60 FPS game loop (16ms per frame)
- Frame-independent movement using deltaTime
- Precise tick-based time tracking via `TickManager`

### 2. **Input Handling**
- Keyboard support: A-Z, 0-9, Function keys (F1-F12), Arrow keys, Control keys
- Real-time mouse position tracking
- Both press and release events for all keys
- Easily extensible key binding system via `KeyBindingBase`

### 3. **Viewport Management**
- Automatic 16:9 aspect ratio enforcement
- Dynamic resolution scaling (ScaleX, ScaleY)
- Automatic mouse coordinate transformation
- Window resize handling

### 4. **Visual Effects**
- **After-Image**: Trails in rectangular or oval shapes with RGB color and alpha transparency
- **Popup**: On-screen text notifications (up to 3 lines + title) with auto-expiration

### 5. **System Monitoring**
- Real-time CPU usage tracking
- Memory statistics (total, free, used)

---

## Architecture

### Core Component: `Base`
The main entry point for all Scope games. Extend this class to create your game:

```
Base (Main Game Class)
├── TickManager (Time Management)
├── ViewMetrics (Viewport/Camera)
├── AfterImageManager (Trail Effects)
├── PopupManager (Text Notifications)
└── SystemMonitor (Performance)
```

### Internal Modules
| Module | Purpose |
|--------|---------|
| `tick` | Time and frame counting system |
| `viewMetrics` | Coordinate transformation and scaling |
| `effect.afterImage` | Trail/motion blur effects |
| `effect.popup` | Text notification system |
| `systemMonitor` | CPU & memory monitoring |

---

## Getting Started

### 1. Create Your Game Class

```java
class MyGame extends Base {
    public MyGame(String title) {
        super(title);
    }
    
    @Override
    protected void init() {
        // Initialize game resources
    }
    
    @Override
    protected void update(double deltaTime) {
        // Update game logic
    }
    
    @Override
    protected void render(Graphics g) {
        // Draw game elements
    }
}
```

### 2. Set Up Input Handling

Extend `KeyBindingBase` to handle keyboard input:

```java
class MyGameInput extends KeyBindingBase {
    private MyGame game;
    
    public MyGameInput(MyGame game) {
        super(game);
        this.game = game;
    }
    
    @Override
    protected void onKeyWPress() {
        // Handle W key press
    }
    
    @Override
    protected void onKeyWRelease() {
        // Handle W key release
    }
}
```

### 3. Use Visual Effects

```java
// Add after-image trail
addAfterImageRect(x, y, r, g, b, width, height, alpha, alphaDegree);
addAfterImageOval(x, y, r, g, b, width, height, alpha, alphaDegree);

// Show popup notification
addPopup("Line 1", "Line 2", "Line 3", "Title");
```

---

## API Reference

### Viewport & Input
| Method | Returns | Description |
|--------|---------|-------------|
| `getMouseX()` | int | Current mouse X coordinate |
| `getMouseY()` | int | Current mouse Y coordinate |
| `getWindowWidth()` | int | Game window width |
| `getWindowHeight()` | int | Game window height |
| `getScaleX()` | double | Horizontal scale factor |
| `getScaleY()` | double | Vertical scale factor |

### Effects
| Method | Parameters | Description |
|--------|-----------|-------------|
| `addAfterImageRect()` | x, y, r, g, b, width, height, alpha, alphaDegree | Add rectangular trail |
| `addAfterImageOval()` | x, y, r, g, b, width, height, alpha, alphaDegree | Add oval trail |
| `addPopup()` | line1, line2, line3, title | Show text notification |

### Lifecycle
| Method | Purpose |
|--------|---------|
| `registerUpdatable(Runnable)` | Register custom update logic |
| `init()` | One-time initialization |
| `update(double deltaTime)` | Per-frame update |
| `render(Graphics g)` | Per-frame rendering |

---

## Key Binding Reference

**Supported Keys:**
- **Letters**: A-Z
- **Numbers**: 0-9
- **Function Keys**: F1-F12
- **Control Keys**: Shift, Ctrl, Alt, Space, Tab, Enter, Escape, Backspace
- **Navigation**: Up, Down, Left, Right

**Event Methods:**
- `onKey{X}Press()` - Called when key is pressed
- `onKey{X}Release()` - Called when key is released

---

## System Requirements
- Java 8 or higher
- JDK with Swing support

