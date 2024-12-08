import processing.core.PApplet;
import processing.core.PVector;

import static parameters.Parameters.*;
import static save.SaveUtil.saveSketch;

public class Isolines extends PApplet {
    public static void main(String[] args) {
        PApplet.main(Isolines.class);
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
        randomSeed(SEED);
        noiseSeed(floor(random(MAX_INT)));
    }

    @Override
    public void setup() {
        background(BACKGROUND_COLOR.red(), BACKGROUND_COLOR.green(), BACKGROUND_COLOR.blue());
        stroke(STROKE_COLOR.red(), STROKE_COLOR.green(), STROKE_COLOR.blue(), STROKE_COLOR.alpha());
        blendMode(BLEND_MODE);
        noFill();
        noLoop();
    }

    @Override
    public void draw() {
        for (int i = MARGIN; i < WIDTH - MARGIN; i += STEP) {
            for (int j = MARGIN; j < HEIGHT - MARGIN; j += STEP) {
                PVector p = new PVector(i, j);
                for (int l = 0; l < MAX_LENGTH; l++) {
                    point(p.x, p.y);
                    float currentHeight = noise(NOISE_SCALE * p.x, NOISE_SCALE * p.y);
                    PVector direction = new PVector(0, 0);
                    float minHeight = currentHeight;
                    int numberOfAngles = floor(map(dist(p.x, p.y, width / 2f, height / 2f),
                            0, width / sqrt(2f),
                            8, 3));
                    for (int k = 0; k < numberOfAngles; k++) {
                        PVector temporaryDirection = PVector.fromAngle(TWO_PI * k / numberOfAngles);
                        float temporaryHeight = noise(NOISE_SCALE * (p.x + temporaryDirection.x),
                                NOISE_SCALE * (p.y + temporaryDirection.y));
                        if (temporaryHeight < minHeight) {
                            direction = temporaryDirection;
                            minHeight = temporaryHeight;
                        }
                    }
                    if (minHeight == currentHeight) {
                        break;
                    }
                    p.add(direction.rotate(HALF_PI).mult(NOISE_STRENGTH));
                    if (p.x < 0 || p.x >= width || p.y < 0 || p.y >= height) {
                        break;
                    }
                }
            }
        }
        saveSketch(this);
    }
}
