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

    private PShape ledShape;
    private int matrixHeight;
    private int matrixWidth;
    private int ledHeight;
    private int ledWidth;
    private int ledSpacingHeight;
    private int ledSpacingWidth;
    private int ledOffColour;
    private int ledOnColour;
    private boolean[][] ledStates;

    public LEDMatrix(PApplet parent, boolean initialState, PShape ledShape, int matrixHeight, int matrixWidth, int ledHeight,
                     int ledWidth, int ledSpacingHeight, int ledSpacingWidth, int ledOffColour,
                     int ledOnColour) {
        this.parent = parent;
        this.ledShape = ledShape;
        this.matrixHeight = matrixHeight;
        this.matrixWidth = matrixWidth;
        this.ledHeight = ledHeight;
        this.ledWidth = ledWidth;
        this.ledSpacingHeight = ledSpacingHeight;
        this.ledSpacingWidth = ledSpacingWidth;
        this.ledOffColour = ledOffColour;
        this.ledOnColour = ledOnColour;

        // Create array and initialise values to false
        ledStates = new boolean[matrixHeight][matrixWidth];
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

    public PShape getLedShape() {
        return ledShape;
    }

    public void setLedShape(PShape ledShape) {
        this.ledShape = ledShape;
    }

    public int getMatrixHeight() {
        return matrixHeight;
    }

    public void setMatrixHeight(int matrixHeight) {
        this.matrixHeight = matrixHeight;
    }

    public int getMatrixWidth() {
        return matrixWidth;
    }

    public void setMatrixWidth(int matrixWidth) {
        this.matrixWidth = matrixWidth;
    }

    public int getLedHeight() {
        return ledHeight;
    }

    public void setLedHeight(int ledHeight) {
        this.ledHeight = ledHeight;
    }

    public int getLedWidth() {
        return ledWidth;
    }

    public void setLedWidth(int ledWidth) {
        this.ledWidth = ledWidth;
    }

    public int getLedSpacingHeight() {
        return ledSpacingHeight;
    }

    public void setLedSpacingHeight(int ledSpacingHeight) {
        this.ledSpacingHeight = ledSpacingHeight;
    }

    public int getLedSpacingWidth() {
        return ledSpacingWidth;
    }

    public void setLedSpacingWidth(int ledSpacingWidth) {
        this.ledSpacingWidth = ledSpacingWidth;
    }

    public int getLedOffColour() {
        return ledOffColour;
    }

    public void setLedOffColour(int ledOffColour) {
        this.ledOffColour = ledOffColour;
    }

    public int getLedOnColour() {
        return ledOnColour;
    }

    public void setLedOnColour(int ledOnColour) {
        this.ledOnColour = ledOnColour;
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
            for (int i = 0; i < matrixHeight; i++) {
                ledStates[i][x] = state;
            }
        }
    }

    // Does not use `setLEDStatesWithPredicate` method to avoid O(n^2) running time since only need to loop through each
    // LED in a single row
    public void setLEDStatesWithY(int y, boolean state) {
        if (isYCoordinateValid(y)) {
            for (int i = 0; i < matrixWidth; i++) {
                ledStates[y][i] = state;
            }
        }
    }

    public void setLEDStatesWithPredicate(BiPredicate<Integer, Integer> predicate, boolean state, boolean setOthers) {
        for (int i = 0; i < matrixHeight; i++) {
            for (int j = 0; j < matrixWidth; j++) {
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
                ledShape.setFill(state ? ledOnColour : ledOffColour);
                parent.shape(ledShape);
                parent.translate(ledWidth + ledSpacingWidth, 0);
            }
            if (i != ledStates.length - 1) {
                parent.translate((ledWidth + ledSpacingWidth) * -ledStates[i].length, ledHeight + ledSpacingHeight);
            }
        }

        parent.popMatrix();
    }

    @Contract(pure = true)
    private int calculateTotalHeight() {
        return (ledHeight * matrixHeight) + (ledSpacingHeight * (matrixHeight - 1));
    }

    @Contract(pure = true)
    private int calculateTotalWidth() {
        return (ledWidth * matrixWidth) + (ledSpacingWidth * (matrixWidth - 1));
    }

    @Contract(pure = true)
    private boolean areCoordinatesValid(int x, int y) {
        return isXCoordinateValid(x) && isYCoordinateValid(y);
    }

    @Contract(pure = true)
    private boolean isXCoordinateValid(int x) {
        return x >= 0 && x < matrixWidth;
    }

    @Contract(pure = true)
    private boolean isYCoordinateValid(int y) {
        return y >= 0 && y < matrixHeight;
    }
}
