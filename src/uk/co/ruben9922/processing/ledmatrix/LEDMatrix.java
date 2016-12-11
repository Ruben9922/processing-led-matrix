package uk.co.ruben9922.processing.ledmatrix;

import processing.core.PApplet;
import processing.core.PShape;

public class LEDMatrix extends PApplet {
    private static final int MATRIX_HEIGHT = 8;
    private static final int MATRIX_WIDTH = 8;
    private static final int LED_HEIGHT = 25;
    private static final int LED_WIDTH = 25;
    private static final int LED_SPACING_HEIGHT = 5;
    private static final int LED_SPACING_WIDTH = 5;
    private static final int BORDER_HEIGHT = 10;
    private static final int BORDER_WIDTH = 10;

    private final int LED_OFF_COLOUR = color(127);
    private final int LED_ON_COLOUR = color(255);

    private boolean[][] ledStates = new boolean[MATRIX_HEIGHT][MATRIX_WIDTH];

    private PShape ledShape;

    public static void main(String[] args) {
        PApplet.main("uk.co.ruben9922.processing.ledmatrix.LEDMatrix");
    }

    public void settings() {
        int total_height = (LED_HEIGHT * MATRIX_HEIGHT) + (LED_SPACING_HEIGHT * (MATRIX_HEIGHT - 1)) + (BORDER_HEIGHT * 2);
        int total_width = (LED_WIDTH * MATRIX_WIDTH) + (LED_SPACING_WIDTH * (MATRIX_WIDTH - 1)) + (BORDER_WIDTH * 2);
        size(total_width, total_height);
    }

    public void setup() {
        // Create array and initialise values to false
        for (int i = 0; i < ledStates.length; i++) {
            for (int j = 0; j < ledStates[i].length; j++) {
                ledStates[i][j] = false;
            }
        }

        // Set up LED shape
        ledShape = createLedShape();
        ledShape.setStroke(false);
    }

    public void draw() {
        background(50);

        pushMatrix();

        translate(BORDER_WIDTH, BORDER_HEIGHT);

        for (int i = 0; i < ledStates.length; i++) {
            for (int j = 0; j < ledStates[i].length; j++) {
                boolean state = ledStates[i][j];
                ledShape.setFill(state ? LED_ON_COLOUR : LED_OFF_COLOUR);
                shape(ledShape);
                translate(LED_WIDTH + LED_SPACING_WIDTH, 0);
            }
            if (i != ledStates.length - 1) {
                translate((LED_WIDTH + LED_SPACING_WIDTH) * -ledStates[i].length, LED_HEIGHT + LED_SPACING_HEIGHT);
            }
        }

        popMatrix();
    }

    private PShape createLedShape() {
        return createShape(RECT, 0, 0, LED_WIDTH, LED_HEIGHT);
    }
}
