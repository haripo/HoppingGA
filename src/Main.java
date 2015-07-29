import GeneticAlgorithm.SimpleGeneticAlgorithm;
import Physics.PhysicsSimulator;
import com.sun.deploy.util.ArrayUtil;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class Main {
    private MainFrame frame;

    private PhysicsSimulator simulator;
    private Individual[] individuals;

    private SimpleGeneticAlgorithm ga;
    private GeneStorage storage;

    private HashMap<String, String> infoMap = new HashMap<>();

    private final int geneLength = 300;
    private final int populationSize = 50;
    private final double mutationRate = 0.01;
    private final double crossoverRate = 0.6;

    private final int actionSpan = 3;
    private int generationSpan;

    private int tick = 0;
    private int generation = 0;

    private boolean fastMode = false;

    private KeyAdapter keyAdapter = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SPACE:
                    fastMode = !fastMode;
                    break;
            }
        }
    };

    private void createIndividuals() {
        int[][] genes = ga.getGenes();
        simulator.removeModels();
        for (int i = 0; i < populationSize; i++) {
            individuals[i] = new Individual(simulator, genes[i], actionSpan);
            simulator.addModel(individuals[i].getModel());
        }

        if (generation > 0) {
            individuals[0].setAsElite();
        }
    }

    private void redraw() {
        Canvas canvas = frame.getCanvas();

        infoMap.put("Tick", Integer.toString(tick));
        infoMap.put("Generation", Integer.toString(generation));
        infoMap.put("FastMode", Boolean.toString(fastMode));
        infoMap.put("Scale", Float.toString(canvas.getScale()));
        infoMap.put("CameraX", Float.toString(canvas.getShiftX()));
        infoMap.put("CameraY", Float.toString(canvas.getShiftY()));

        canvas.fill();
        for (int i = individuals.length - 1; i >= 0; i--) {
            individuals[i].draw(canvas);
        }
        simulator.draw(canvas);
        canvas.drawInfoString(infoMap);
        canvas.drawLine(-20, 9.5f, 20, 9.5f, 1);
        canvas.repaint();
    }

    public Main() {
        frame = new MainFrame();
        frame.addKeyListener(keyAdapter);

        ga = new SimpleGeneticAlgorithm(populationSize, geneLength, mutationRate, crossoverRate);
        ga.randomInitialize();

        generationSpan = (geneLength / Individual.genePairLength) * actionSpan;

        individuals = new Individual[populationSize];

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'_'HHmmss");
        String logFilename = String.format("log_%s.csv", dateFormat.format(new Date()));
        storage = new GeneStorage(logFilename);
        infoMap.put("Log", logFilename);

        simulator = new PhysicsSimulator();

        createIndividuals();
    }

    public void mainLoop() {
        try {
            while (true) {
                step();
                if(!fastMode) Thread.sleep(20);
            }
        } catch (InterruptedException e) {
            return;
        }
    }

    public void step() {
        simulator.step(1);
        tick += 1;

        if (!fastMode || tick == generationSpan) {
            redraw();
        }

        // update generation
        if (tick >= generationSpan) {
            tick = 0;
            generation += 1;

            float[] fitnesses = new float[populationSize];
            for (int i = 0; i < fitnesses.length; i++) {
                Individual target = individuals[i];
                float distanceFactor = target.getDistance() / 50.0f;
                float slipTimeFactor = target.getSlipTime() / (float)generationSpan;
                fitnesses[i] = distanceFactor + slipTimeFactor;
            }

            // save log
//            int[][] genes = ga.getGenes();
//            for (int i = 0; i < populationSize; i++) {
//                storage.save(generation, fitnesses[i], genes[i]);
//            }

            // create next generation
            ga.generateNext(fitnesses);
            createIndividuals();
        } else {
            // update individual
            for (Individual individual : individuals) {
                individual.step(tick);
            }
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.mainLoop();
    }
}
