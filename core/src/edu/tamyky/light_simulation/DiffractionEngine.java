package edu.tamyky.light_simulation;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public final class DiffractionEngine extends ApplicationAdapter implements Disposable {
    /**
     * Width of window in pixels.
     */
    public static final int WIDTH = 1000;
    public static final int WAVE_WIDTH = 600;
    /**
     * Height of window in pixels.
     */
    public static final int HEIGHT = 1000;
    public static final int WAVE_HEIGHT = 600;

    private ShapeRenderer renderer;

    private double[][] waveHeights;
    private double[][] waveVelocity;
    private double[][] cellMass;

    /**
     * The dimensions of a single cell.
     */
    private Vector2 dimensions;

    /**
     * Prints simulation frame per seconds (fps) to the console.
     */
    private FPSLogger fpsLogger;

    /**
     * Called on start up.
     */
    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        OrthographicCamera cam = new OrthographicCamera(30, 30 * (h / w));
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.lookAt(1000, 1000, 0);
        cam.update();
        this.fpsLogger = new FPSLogger();
        this.renderer = new ShapeRenderer();
        this.renderer.setAutoShapeType(true);

        waveHeights = new double[WAVE_HEIGHT][WAVE_WIDTH];
        waveVelocity = new double[WAVE_HEIGHT][WAVE_WIDTH];
        cellMass = new double[WAVE_HEIGHT][WAVE_WIDTH];
        for (int x = 0; x < WAVE_HEIGHT; x++) {
            for (int y = 0; y < WAVE_WIDTH; y++) {
                waveHeights[x][y] = 0;
                waveVelocity[x][y] = 0;
                cellMass[x][y] = 0.5;
            }
        }
        for (int i = 100; i < 110; i++) {
            for (int x = 0; x < 225; x++) {
                cellMass[x][i] = 0;
            }
            for (int x = 250; x < 350; x++) {
                cellMass[x][i] = 0;
            }
            for (int x = 375; x < 600; x++) {
                cellMass[x][i] = 0;
            }
        }
        this.dimensions = new Vector2(WIDTH / (float) waveHeights[0].length, HEIGHT / (float) waveHeights.length);
    }

    private int frame = 0;

    /**
     * @param dt Delta time
     */
    public void update(float dt) {

        if (frame < 5000) {
            for (int x = 100; x < WAVE_HEIGHT - 100; x++) {
                waveHeights[x][50] = Math.sin(frame * ((2 * Math.PI) / 30)) * 100;
            }
        }

        for (int x = 1; x < WAVE_WIDTH - 1; x++) {
            for (int y = 1; y < WAVE_HEIGHT - 1; y++) {
                if (x >= WAVE_WIDTH - 25 || y >= WAVE_HEIGHT - 25 || x <= 25 || y <= 25) {
                    waveVelocity[x][y] *= 0.85;
                }
                waveHeights[x][y] += waveVelocity[x][y];
            }
        }
        for (int x = 1; x < WAVE_WIDTH - 1; x++) {
            for (int y = 1; y < WAVE_HEIGHT - 1; y++) {
                double force = waveHeights[x - 1][y] + waveHeights[x + 1][y] + waveHeights[x][y - 1] + waveHeights[x][y + 1];
                waveVelocity[x][y] += ((force / 4 - waveHeights[x][y]) * cellMass[x][y]);
            }
        }
        frame++;
    }

    /**
     * Used to render the simulation. Calls {@link #update(float)} method. Logs frames pre second.
     */
    @Override
    @SuppressWarnings("LibGDXProfilingCode")
    public void render() {
        this.fpsLogger.log();

        Gdx.gl.glClearColor(1, 1, 1, 1); // white

        final float dt = Gdx.graphics.getDeltaTime();
        this.update(dt);

        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        this.renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int x = 0; x < WAVE_WIDTH; x++) {
            for (int y = 0; y < WAVE_HEIGHT; y++) {
                if (cellMass[x][y] == 0) {
                    this.renderer.setColor(Color.RED);
                    this.renderer.rect(x * dimensions.x, y * dimensions.y, dimensions.x, dimensions.y);
                } else {
                    this.renderer.setColor(ColorUtil.transition(waveHeights[x][y]));
                    this.renderer.rect(x * dimensions.x, y * dimensions.y, dimensions.x, dimensions.y);
                }
            }
        }
        this.renderer.end();
    }

    /**
     * Releases all resources of this object. Called when the {@link Application} is destroyed.
     * Preceded by a call to {@link #pause()}.
     */
    @Override
    public void dispose() {
        this.renderer.dispose();
    }
}