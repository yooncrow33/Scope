# Scope

**Scope v1.6.4-alpha** - A comprehensive Java game development framework built on Swing for creating 2D games with ease.

---

## Table of Contents

- [Overview](#overview)
- [Core Features](#core-features)
- [Getting Started](#getting-started)
- [Base Classes](#base-classes)
- [Side-Scroll Engine](#side-scroll-engine)
- [Entity System](#entity-system)
- [Camera System](#camera-system)
- [Input Handling](#input-handling)
- [Effect System](#effect-system)
- [Sound Management](#sound-management)
- [System Monitoring](#system-monitoring)
- [API Reference](#api-reference)

---

## Overview

Scope is a lightweight yet powerful Java game framework designed to simplify game development. It provides:

- **Easy window management** with automatic aspect ratio handling (16:9)
- **Flexible rendering pipeline** with graphics scaling and offset management
- **Comprehensive input system** supporting keyboard input detection
- **Side-scroll game support** with entity management, collision detection, and camera following
- **Effect system** for post-processing effects like after-images and popup text
- **Sound management** for background music and sound effects
- **System monitoring** for memory and CPU usage tracking
- **Tick-based timing** for frame-independent updates

---

## Core Features

### Window Management
- Automatic 16:9 aspect ratio enforcement
- Resizable window with dynamic scaling
- High-DPI support through Graphics2D transformations

### Game Loop
- 60 FPS game loop (16ms updates)
- Delta time-based updates for frame-independent logic
- Automatic rendering pipeline

### Coordinate System
- Virtual coordinate system that scales to window size
- Mouse position tracking in virtual space
- Configurable viewport scaling

---

## Getting Started

### Basic Setup

To create a simple Scope game, extend `EmptyBase`:

```java
public class MyGame extends EmptyBase {
    
    public MyGame() {
        super("My Awesome Game");
    }
    
    @Override
    protected void init() {
        // Initialize your game here
        System.out.println("Game initialized!");
    }
    
    @Override
    protected void update(double deltaTime) {
        // Update game logic
    }
    
    @Override
    protected void render(Graphics g) {
        // Render your game
        g.setColor(Color.BLUE);
        g.fillRect(100, 100, 50, 50);
    }
    
    public static void main(String[] args) {
        new MyGame();
    }
}
```

### Side-Scroll Game Setup

For side-scrolling games, extend `SideScrollBase`:

```java
public class MyPlatformer extends SideScrollBase {
    
    public MyPlatformer() {
        super("My Platformer", 5000, 2000); // world width, world height
    }
    
    @Override
    protected void init() {
        // Add entities and set up the level
        Entity player = createPlayer();
        addEntity(player);
        
        //update, render loop start.
        launch();
    }
    
    @Override
    protected void update(double deltaTime) {
        // Update game logic
    }
    
    @Override
    protected void render(Graphics g) {
        // Render world-specific objects
    }
    
    public static void main(String[] args) {
        new MyPlatformer();
    }
}
```

---

## Base Classes

### `EmptyBase`

A simple base class for non-side-scroll games. Provides basic rendering and update loops.

**Key Methods:**
- `init()` - Called once at game start
- `update(double deltaTime)` - Called every frame
- `render(Graphics g)` - Called every frame for rendering
- `clickEvent()` - Called when mouse moves
- `exit()` - Gracefully shut down the game

**Utility Methods:**
- `getMouseX()` - Get virtual mouse X position
- `getMouseY()` - Get virtual mouse Y position
- `getScaleX()` - Get X-axis scaling factor
- `getScaleY()` - Get Y-axis scaling factor
- `getWindowWidth()` - Get window width in pixels
- `getWindowHeight()` - Get window height in pixels
- `scopeEngine()` - Access the main engine facade

---

## Side-Scroll Engine

### `SideScrollBase`

Extended base class specifically for side-scrolling games. Includes entity management, collision detection, and camera system.

**Constructor:**
```java
SideScrollBase(String title, int worldWidth, int worldHeight)
```

**World Bounds:**
- `WORLD_WIDTH` - Total width of the game world
- `WORLD_HEIGHT` - Total height of the game world
- `MIN_X`, `MAX_X` - World X boundaries
- `MIN_Y`, `MAX_Y` - World Y boundaries

**Key Methods:**

**Entity Management:**
- `addEntity(Entity e)` - Add a game entity (collisionable object)
- `addHudEntity(HudEntity e)` - Add a HUD entity (UI element, no collision)

**Camera:**
- `getCamera()` - Get the Camera object for manipulation

**Rendering & Updates:**
- `update(double deltaTime)` - Implement your game logic
- `render(Graphics g)` - Implement your world-specific rendering
- `init()` - Called at startup
- `clickEvent()` - Called on mouse movement

**Collision Handling:**
- `resolveCollision(Entity a, Entity b)` - Handle collisions between entities
  - Automatically separates overlapping entities
  - Prevents tunneling with overlap resolution

**Debug Features:**
- `setHitBoxRender(boolean b)` - Toggle hitbox visualization

---

## Entity System

### `Entity`

Abstract base class for collidable game objects (player, enemies, obstacles).

**Constructor:**
```java
Entity(int radius, double x, double y, 
       boolean isCollisionEnabled, 
       boolean isUpdateEnabled, 
       int layer)
```

**Parameters:**
- `radius` - Collision radius (circle-based collision)
- `x`, `y` - World position
- `isCollisionEnabled` - Whether this entity can collide with others
- `isUpdateEnabled` - Whether update() is called
- `layer` - Render order (lower = background, higher = foreground)

**Abstract Methods:**
- `render(Graphics g, double x, double y)` - Render the entity at screen position
- `update(double dt)` - Update entity logic

**Useful Methods:**
- `getX()`, `getY()` - Get world position
- `getRadius()` - Get collision radius
- `getLayer()` - Get render layer
- `addX(double value)`, `addY(double value)` - Move entity
- `checkBound(SideScrollBase ssBase)` - Keep entity within world bounds
- `remove()` - Mark entity for removal
- `renderHitbox(Graphics g)` - Draw collision circle for debugging
- `isCollisionEnabled()` - Check if entity can collide
- `isRemove()` - Check if entity is marked for removal

**Example Entity:**
```java
public class Player extends Entity {
    
    public Player(double startX, double startY) {
        super(25, startX, startY, true, true, 5);
    }
    
    @Override
    public void render(Graphics g, double x, double y) {
        g.setColor(Color.RED);
        g.fillOval((int)x - radius, (int)y - radius, radius * 2, radius * 2);
    }
    
    @Override
    public void update(double dt) {
        // Move player logic
    }
}
```

### `HudEntity`

Non-colliding UI element that stays in screen space (not affected by camera).

**Constructor:**
```java
HudEntity(double x, double y, 
          boolean isUpdateEnabled, 
          int layer)
```

**Abstract Methods:**
- `render(Graphics g)` - Render HUD element at screen position
- `update(double dt)` - Update HUD element

**Example HudEntity:**
```java
public class HealthBar extends HudEntity {
    private int health = 100;
    
    public HealthBar() {
        super(10, 10, true, 100);
    }
    
    @Override
    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect((int)x, (int)y, health * 2, 20);
    }
    
    @Override
    public void update(double dt) {
        // Update health
    }
}
```

---

## Camera System

### `Camera`

Smooth camera control with follow mechanics.

**Methods:**

- `getX()`, `getY()` - Get camera position
- `move(double xValue, double yValue)` - Relative movement
- `setX(double xValue)`, `setY(double yValue)` - Absolute positioning
- `follow(double targetX, double targetY, double lerp)` - Smooth follow
  - `lerp` parameter: 0.0 (no movement) to 1.0 (instant snap)
  - Typical value: 0.1 for smooth follow

**Example - Following Player:**
```java
@Override
protected void update(double deltaTime) {
    Player player = getPlayer();
    Camera camera = getCamera();
    
    // Smooth camera follow
    camera.follow(player.getX(), player.getY(), 0.05);
}
```

---

## Input Handling

### `KeyBindingBase`

Comprehensive keyboard input system with press/release events.

**Supported Keys:**

- **A-Z**: `A`, `B`, `C`, ... `Z`
- **Numbers**: `NUM0`, `NUM1`, ... `NUM9`
- **Function Keys**: `F1`, `F2`, ... `F12`
- **Control Keys**: `SHIFT`, `CTRL`, `ALT`, `SPACE`, `TAB`, `ENTER`, `ESC`, `BACKSPACE`
- **Arrow Keys**: `UP`, `DOWN`, `LEFT`, `RIGHT`

**Press Callbacks:**
```java
@Override
protected void onKeyWPress() {
    player.moveForward();
}

@Override
protected void onKeyAPress() {
    player.moveLeft();
}

@Override
protected void onKeyDPress() {
    player.moveRight();
}

@Override
protected void onKeySpacePress() {
    player.jump();
}
```

**Release Callbacks:**
```java
@Override
protected void onKeyWRelease() {
    player.stopMovingForward();
}
```

**Available Methods:**
- `onKey{KEY}Press()` - Called when key is pressed
- `onKey{KEY}Release()` - Called when key is released
- Example: `onKeyWPress()`, `onKeyWRelease()`, `onKeySpacePress()`, etc.

---

## Effect System

### `EffectFacade`

Access effects through `scopeEngine().effect()`.

#### After-Image Effect

Creates trailing visual effects for fast-moving objects.

**Rectangular After-Image:**
```java
scopeEngine().effect().AfterImage()
    .adeAfterImageRect(
        x, y,                      // Position
        255, 100, 50,              // RGB color
        width, height,             // Size
        200,                       // Initial alpha
        5                          // Alpha decrease per frame
    );
```

**Circular After-Image:**
```java
scopeEngine().effect().AfterImage()
    .addAfterImageOval(
        x, y,                      // Position
        255, 100, 50,              // RGB color
        width, height,             // Size
        200,                       // Initial alpha
        5                          // Alpha decrease per frame
    );
```

#### Popup Effect

Display floating text notifications.

```java
scopeEngine().effect().Popup()
    .addPopup(
        "Line 1: Damage taken!",
        "Line 2: -25 HP",
        "Line 3: Critical Hit!",
        "DAMAGE"                   // Title/Category
    );
```

---

## Sound Management

### `SoundFacade`

Access sound through `scopeEngine().sound()`.

**Methods:**

- `setExternalRoot(String dir)` - Set root directory for sound files
- `play(String path)` - Play a one-shot sound effect
- `loopBgm(String path)` - Play background music on loop
- `stopBgm()` - Stop current background music
- `setBgmVolume(float db)` - Set BGM volume in decibels
- `setSfxVolume(float db)` - Set SFX volume in decibels
- `dispose()` - Clean up audio resources

**Example:**
```java
@Override
protected void init() {
    SoundFacade sound = scopeEngine().sound();
    sound.setExternalRoot("assets/audio");
    sound.setBgmVolume(-20f);
    sound.setSfxVolume(-10f);
    sound.loopBgm("background.wav");
}

@Override
protected void update(double deltaTime) {
    if (playerTakeDamage) {
        scopeEngine().sound().play("hurt.wav");
    }
}
```

---

## System Monitoring

### `SystemFacade`

Monitor system resources through `scopeEngine().system()`.

**Methods:**

- `getTotalMemory()` - Get total JVM memory in bytes
- `getFreeMemory()` - Get available memory in bytes
- `getUsedMemory()` - Get currently used memory in bytes
- `getCpuPercentage()` - Get CPU usage percentage
- `getTick()` - Get current tick count

**Example - Debug Display:**
```java
@Override
protected void render(Graphics g) {
    SystemFacade system = scopeEngine().system();
    g.setColor(Color.WHITE);
    g.drawString("Used Memory: " + (system.getUsedMemory() / 1024 / 1024) + " MB", 10, 20);
    g.drawString("CPU: " + system.getCpuPercentage() + "%", 10, 40);
    g.drawString("Tick: " + system.getTick(), 10, 60);
}
```

---

## API Reference

### Window & Rendering

| Method | Return | Description |
|--------|--------|-------------|
| `getMouseX()` | int | Virtual mouse X position |
| `getMouseY()` | int | Virtual mouse Y position |
| `getScaleX()` | double | X-axis scale factor |
| `getScaleY()` | double | Y-axis scale factor |
| `getWindowWidth()` | int | Window width in pixels |
| `getWindowHeight()` | int | Window height in pixels |
| `getVersion()` | String | Framework version |
| `getBuildDate()` | String | Build date |

### Entity & Collision

| Method | Description |
|--------|-------------|
| `addEntity(Entity e)` | Register entity in collision system |
| `addHudEntity(HudEntity e)` | Register UI element (no collision) |
| `getCamera()` | Access camera control |
| `resolveCollision(Entity a, Entity b)` | Resolve collision between two entities |
| `setHitBoxRender(boolean b)` | Debug: Show/hide hitboxes |

### Engine Access

| Method | Return | Description |
|--------|--------|-------------|
| `scopeEngine()` | ScopeEngineAccess | Access all engine systems |
| `scopeEngine().effect()` | EffectFacade | Effects (popups, after-images) |
| `scopeEngine().system()` | SystemFacade | System monitoring |
| `scopeEngine().sound()` | SoundFacade | Sound management |

---

## Complete Example: Simple Platformer

```java
import scope.*;
import scope.sideScroll.*;
import java.awt.*;
import java.awt.event.*;

public class SimplePlatformer extends SideScrollBase {
    
    private Entity player;
    
    public SimplePlatformer() {
        super("Simple Platformer", 3200, 1800);
    }
    
    @Override
    protected void init() {
        // Create player
        player = new Entity(15, 0, 0, true, true, 10) {
            private double velocityY = 0;
            private boolean isJumping = false;
            
            @Override
            public void render(Graphics g, double x, double y) {
                g.setColor(Color.RED);
                g.fillOval((int)x - radius, (int)y - radius, radius * 2, radius * 2);
            }
            
            @Override
            public void update(double dt) {
                // Gravity
                velocityY += 500 * dt;
                y += velocityY * dt;
                
                // Keep in bounds
                if (y > 900) {
                    y = 900;
                    velocityY = 0;
                    isJumping = false;
                }
            }
        };
        
        addEntity(player);
        
        // Create platform
        addEntity(new Entity(100, 0, 900, true, false, 5) {
            @Override
            public void render(Graphics g, double x, double y) {
                g.setColor(Color.GREEN);
                g.fillRect((int)x - radius, (int)y - radius, radius * 2, radius * 2);
            }
            
            @Override
            public void update(double dt) {}
        });
        
        // Start background music
        scopeEngine().sound().setExternalRoot("assets");
        scopeEngine().sound().loopBgm("bgm.wav");
    }
    
    @Override
    protected void update(double deltaTime) {
        // Camera follows player
        getCamera().follow(player.getX(), player.getY(), 0.08);
    }
    
    @Override
    protected void render(Graphics g) {
        // Render background
        g.setColor(Color.CYAN);
        g.fillRect((int)(MIN_X - 1000), (int)(MIN_Y - 1000), 10000, 10000);
    }
    
    @Override
    protected void onKeyWPress() {
        // Jump
        if (player.getY() > -500) { // Basic jump check
            // Apply upward velocity in update
        }
    }
    
    @Override
    protected void onKeyAPress() {
        player.addX(-200 * 0.016); // Move left
    }
    
    @Override
    protected void onKeyDPress() {
        player.addX(200 * 0.016); // Move right
    }
    
    public static void main(String[] args) {
        new SimplePlatformer();
    }
}
```

---

## Version Information

- **Current Version**: 1.6.0-alpha
- **Build Date**: 2026-02-15
- **Framework**: Scope
- **Language**: Java 16+
- **Dependencies**: Java Swing (built-in)

---

## Tips & Best Practices

1. **Use Layer System**: Assign appropriate layer numbers to entities for proper rendering order
2. **Collision Radius**: Use circular collision detection for simplicity and performance
3. **Camera Lerp**: Use lerp values between 0.05-0.15 for smooth camera following
4. **Entity Cleanup**: Always call `remove()` on entities that are no longer needed
5. **Aspect Ratio**: Window automatically maintains 16:9 ratio
6. **Performance**: Collision detection is O(n²) - keep entity count reasonable
7. **Audio Format**: Use WAV files for best compatibility
8. **Debug Mode**: Use `setHitBoxRender(true)` to visualize collision circles

---

## Support & Issues

For bug reports and feature requests, please refer to the project repository.
- **Control Keys**: Shift, Ctrl, Alt, Space, Tab, Enter, Escape, Backspace
- **Navigation**: Up, Down, Left, Right

**Event Methods:**
- `onKey{X}Press()` - Called when key is pressed
- `onKey{X}Release()` - Called when key is released

---

## System Requirements
- Java 8 or higher
- JDK with Swing support

