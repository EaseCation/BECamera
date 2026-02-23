# BECamera - Bedrock Camera Lib

A Fabric library mod that replicates Minecraft Bedrock Edition's Camera APIs for Java Edition.

> Forked from [NaKeRNarolino/BECamera](https://github.com/NaKeRNarolino/BECamera) (CC0-1.0).

## Features

- **Camera Set** — Move and rotate the camera to any position with easing transitions
- **Camera Fade** — Screen fade effects with customizable color and timing
- **Camera Shake** — Positional and rotational camera shake with decay
- **Camera Presets** — Store and recall named camera configurations
- **32 Easing Functions** — Full Bedrock easing support (linear, spring, quad, cubic, quart, quint, sine, expo, circ, bounce, back, elastic — in/out/inOut variants)

## Supported Versions

Built with [Stonecutter](https://stonecutter.kikugie.dev/) for multi-version support:

`1.21.5` · `1.21.6` · `1.21.7` · `1.21.8` · `1.21.9` · `1.21.10` · `1.21.11`

## This mod is a *library*. It does not add/do anything on its own.

Written in Kotlin — requires [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin).

## Installation (for developers)

Add to your `build.gradle`:
```groovy
repositories {
    mavenLocal() // for development
    maven { url 'https://jitpack.io' } // for release
}

dependencies {
    modImplementation 'nakern.be_camera:bedrockcameralib:1.1.0'
}
```

## Usage

### Camera Set (Java)
```java
// Move camera with easing
CameraManager.INSTANCE.setCamera(new CameraData(
    new Vec3d(30, 30, 30),      // position
    new Vec3d(15, 15, 15),      // facing target (optional)
    null,                        // direct rotation (optional)
    new EaseOptions(
        Easings::easeInOutBack,  // easing function
        2500                     // duration (ms)
    )
));

// Direct rotation (pitch, yaw)
CameraManager.INSTANCE.setCamera(new CameraData(
    new Vec3d(0, 100, 0),
    null,
    new Vec2f(30.0f, 45.0f),    // pitch=30, yaw=45
    new EaseOptions(Easings::linear, 1000)
));

// Clear camera (return to player view)
CameraManager.INSTANCE.clear();
```

### Camera Fade (Java)
```java
CameraManager.INSTANCE.fade(new CameraFadeOptions(
    0x000000,  // color (RGB)
    1500,      // fade in (ms)
    2000,      // hold (ms)
    1500       // fade out (ms)
));
```

### Camera Shake (Java)
```java
// Add rotational shake
CameraShakeManager.INSTANCE.addShake(1.0f, 2.0f, 1); // intensity, duration(s), type(1=rotational)

// Add positional shake
CameraShakeManager.INSTANCE.addShake(0.5f, 1.5f, 0); // type(0=positional)

// Stop all shakes
CameraShakeManager.INSTANCE.stopAll();
```

### Camera Presets (Java)
```java
CameraPresetManager.INSTANCE.setPresets(List.of(
    new CameraPresetManager.Preset("free", null, 0f, 100f, 0f, 0f, 0f, null)
));

CameraPresetManager.Preset preset = CameraPresetManager.INSTANCE.getPresetByName("free");
```

### Easing Lookup by Bedrock Ordinal
```java
// Get easing function by Bedrock EasingType enum ordinal (0-31)
EasingFn easing = Easings.byBedrockOrdinal(ordinal);
```

### Disconnect Cleanup
```java
// Call on disconnect to reset all camera state
CameraManager.INSTANCE.resetAll();
```

## License

CC0-1.0 — Public Domain
