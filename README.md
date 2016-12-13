# Processing LED Matrix
Java library that allows **Processing applications to easily draw and interact with a virtual grid of LEDs**.  
I made this for developing and testing games in Java that are designed to output to a grid of LEDs (such as that on the [Raspberry Pi Sense HAT](https://www.raspberrypi.org/products/sense-hat/)).

## Quick Start

### Setting Up
In your Processing application (inside a class that extends `PApplet`), you can **create an `LEDMatrix` object with the default options** like so:
```java
LEDMatrix ledMatrix = new LEDMatrix(this);
```
(As with any non-static `PApplet` method, such as `shape`, `translate`, `pushMatrix` and `popMatrix`, this code must be run after calling `PApplet.main` - e.g. in `setup` or `draw`.)

### Interacting with the Matrix
Interact with the LED matrix by **calling `LEDMatrix` methods such as `getLEDState` and `setLEDState`**.  
* **`getLEDState` returns the state of the LED with the given coordinates.**  
  More specifically, it returns a `java.util.Optional<Boolean>` object containing a boolean value if the given coordinates are valid, or an empty `Optional` instance if they are not valid.  
  Hence, you need to check whether the value is present before using it (e.g. using `ifPresent`).  
  See the [relevant page in the Java 8 documentation](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html) for more information.  
* **`setLEDState` sets the state of the LED with the given coordinates to the given state.**

Example usage:
```java
// Setting the state of an LED to true
ledMatrix.setLEDState(0, 1, true);

// Getting the state of an LED and printing it to the console
Optional<Boolean> state = ledMatrix.getLEDState(0, 1);
state.ifPresent(System.out::println);
```

### Drawing the Matrix
Simply **call `<LEDMatrix>.draw` in your `draw` method**:
```java
public void draw() {
  ledMatrix.draw();
}
```

## To-Do
* **Add more useful methods**  
  For example, to set the state of multiple LEDs (e.g. all LEDs in a certain row or whose row/column numbers match some predicate).
* **Add more examples above**
* **Describe all possible options that can be passed to the `LEDMatrix` constructor**  
  The above example in the *Setting Up* section above only gives an example for setting up an LED matrix with the default options.
