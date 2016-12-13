package uk.co.ruben9922.processing.ledmatrix;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import processing.core.PApplet;
import processing.core.PShape;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.UnaryOperator;

public class LEDMatrix {
    private final PApplet parent;
    private final PShape ledShape;
    private final int MATRIX_HEIGHT;
    private final int MATRIX_WIDTH;
    private final int LED_HEIGHT;
    private final int LED_WIDTH;
    private final int LED_SPACING_HEIGHT;
    private final int LED_SPACING_WIDTH;
    private final int LED_OFF_COLOUR;
    private final int LED_ON_COLOUR;

    private boolean[][] ledStates;

    public LEDMatrix(PApplet parent, boolean initialState, PShape ledShape, int MATRIX_HEIGHT, int MATRIX_WIDTH, int LED_HEIGHT,
                     int LED_WIDTH, int LED_SPACING_HEIGHT, int LED_SPACING_WIDTH, int LED_OFF_COLOUR,
                     int LED_ON_COLOUR) {
        this.parent = parent;
        this.ledShape = ledShape;
        this.MATRIX_HEIGHT = MATRIX_HEIGHT;
        this.MATRIX_WIDTH = MATRIX_WIDTH;
        this.LED_HEIGHT = LED_HEIGHT;
        this.LED_WIDTH = LED_WIDTH;
        this.LED_SPACING_HEIGHT = LED_SPACING_HEIGHT;
        this.LED_SPACING_WIDTH = LED_SPACING_WIDTH;
        this.LED_OFF_COLOUR = LED_OFF_COLOUR;
        this.LED_ON_COLOUR = LED_ON_COLOUR;

        // Create array and initialise values to false
        ledStates = new boolean[MATRIX_HEIGHT][MATRIX_WIDTH];
        for (int i = 0; i < ledStates.length; i++) {
            for (int j = 0; j < ledStates[i].length; j++) {
                ledStates[i][j] = initialState;
            }
        }
    }

    // Call "primary" constructor with default values
    public LEDMatrix(PApplet parent, boolean initialState) {
        // Would have made this more readable but Java requires calls to `this` to be first statement in constructor
        this(parent, initialState, ((UnaryOperator<PShape>)((x) -> { x.setStroke(false); return x; })).apply(parent.createShape(PApplet.RECT, 0, 0, 25, 25)),
                8, 8, 25, 25, 5, 5, parent.color(127), parent.color(255));
    }

    public LEDMatrix(PApplet parent) {
        this(parent, false);
    }

    // Given x- and y-coordinates, returns an Optional instance containing the current state of the LED with those
    // coordinates
    // Returns an empty Optional instance if given coordinates are invalid
    @NotNull
    public Optional<Boolean> getLEDState(int x, int y) {
        if (areCoordinatesValid(x, y)) {
            return Optional.of(ledStates[y][x]);
        } else {
            return Optional.empty();
        }
    }

    public void setLEDState(int x, int y, boolean state) {
        if (areCoordinatesValid(x, y)) {
            ledStates[y][x] = state;
        }
    }

    // Does not use `setLEDStatesWithPredicate` method to avoid O(n^2) running time since only need to loop through each
    // LED in a single column
    public void setLEDStatesWithX(int x, boolean state) {
        if (isXCoordinateValid(x)) {
            for (int i = 0; i < MATRIX_HEIGHT; i++) {
                ledStates[i][x] = state;
            }
        }
    }

    // Does not use `setLEDStatesWithPredicate` method to avoid O(n^2) running time since only need to loop through each
    // LED in a single row
    public void setLEDStatesWithY(int y, boolean state) {
        if (isYCoordinateValid(y)) {
            for (int i = 0; i < MATRIX_WIDTH; i++) {
                ledStates[y][i] = state;
            }
        }
    }

    public void setLEDStatesWithPredicate(BiPredicate<Integer, Integer> predicate, boolean state, boolean setOthers) {
        for (int i = 0; i < MATRIX_HEIGHT; i++) {
            for (int j = 0; j < MATRIX_WIDTH; j++) {
                if (predicate.test(i, j)) {
                    ledStates[j][i] = state;
                } else if (setOthers) {
                    ledStates[j][i] = !state;
                }
            }
        }
    }

    // May change to return PShape rather than drawing directly
    public void draw() {
        parent.pushMatrix();

        parent.translate(PApplet.max(0, (parent.width - calculateTotalWidth()) / 2),
                PApplet.max(0, (parent.height - calculateTotalHeight()) / 2));

        for (int i = 0; i < ledStates.length; i++) {
            for (int j = 0; j < ledStates[i].length; j++) {
                boolean state = ledStates[i][j];
                ledShape.setFill(state ? LED_ON_COLOUR : LED_OFF_COLOUR);
                parent.shape(ledShape);
                parent.translate(LED_WIDTH + LED_SPACING_WIDTH, 0);
            }
            if (i != ledStates.length - 1) {
                parent.translate((LED_WIDTH + LED_SPACING_WIDTH) * -ledStates[i].length, LED_HEIGHT + LED_SPACING_HEIGHT);
            }
        }

        parent.popMatrix();
    }

    @Contract(pure = true)
    private int calculateTotalHeight() {
        return (LED_HEIGHT * MATRIX_HEIGHT) + (LED_SPACING_HEIGHT * (MATRIX_HEIGHT - 1));
    }

    @Contract(pure = true)
    private int calculateTotalWidth() {
        return (LED_WIDTH * MATRIX_WIDTH) + (LED_SPACING_WIDTH * (MATRIX_WIDTH - 1));
    }

    @Contract(pure = true)
    private boolean areCoordinatesValid(int x, int y) {
        return isXCoordinateValid(x) && isYCoordinateValid(y);
    }

    @Contract(pure = true)
    private boolean isXCoordinateValid(int x) {
        return x >= 0 && x < MATRIX_WIDTH;
    }

    @Contract(pure = true)
    private boolean isYCoordinateValid(int y) {
        return y >= 0 && y < MATRIX_HEIGHT;
    }
}
