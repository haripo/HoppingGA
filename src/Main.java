import GeneticAlgorithm.SimpleGeneticAlgorithm;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Main extends JFrame {
    private Canvas canvas;
    private PhysicsWorld world;

    private HashMap<String, String> infoMap = new HashMap<>();

    private SimpleGeneticAlgorithm ga;
    private GeneStorage storage;

    private int gene_length = 100 * 3;
    private int population_size = 10;
    private double mutation_rate = 0.02;
    private double crossover_rate = 0.6;

    private int action_span = 3;
    private int gene_pair_size = 3;

    private double average_fitness = 0;
    private int[] slipTimes;

    private int tickCount = 0;
    private boolean fast_mode = false;

    private int gene_index = 0;
    private int generation = 0;


    private KeyAdapter keyAdapter = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SPACE:
                    fast_mode = !fast_mode;
                    break;
                case KeyEvent.VK_LEFT:
                    canvas.moveLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                    canvas.moveRight();
                    break;
                case KeyEvent.VK_UP:
                    canvas.zoomUp();
                    break;
                case KeyEvent.VK_DOWN:
                    canvas.zoomDown();
                    break;
            }
        }
    };

    public Main() {
        ga = new SimpleGeneticAlgorithm(
                population_size, gene_length, mutation_rate, crossover_rate);
        ga.randomInitialize();

        slipTimes = new int[population_size];

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'hoppingGA_'yyyymmdd'_'HHmmss'.log'");
        storage = new GeneStorage(dateFormat.format(new Date()));
        InitFrame();

        addKeyListener(keyAdapter);
    }

    public void InitFrame() {
        canvas = new Canvas();
        getContentPane().add(canvas);

        world = new PhysicsWorld();
        world.addIndividual(population_size);

        setTitle("Box2d");
        setBounds(0, 0, 1000, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private int[] fitnesses = new int[population_size];

    public void MainLoop() {
        try {
            while (true) {
                Step();
                if(!fast_mode) Thread.sleep(20);
            }
        } catch (InterruptedException e) {
            return;
        }
    }

    public void Step() {
        world.step(1);

        // refresh info
        infoMap.put("Tick", Integer.toString(tickCount));
        infoMap.put("Generation", Integer.toString(generation));
        infoMap.put("FastMode", Boolean.toString(fast_mode));
        infoMap.put("Scale", Float.toString(canvas.getScale()));
        infoMap.put("CameraX", Float.toString(canvas.getShiftX()));
        infoMap.put("CameraY", Float.toString(canvas.getShiftY()));

        // redraw canvas
        if (!fast_mode || tickCount % 10 == 0) {
            world.draw(canvas);
            canvas.drawInfoString(infoMap);
            canvas.drawLine(-20, 9.5f, 20, 9.5f);
            canvas.repaint();
        }

        int[][] genes = ga.getGenes();
        tickCount += 1;
        if (tickCount % action_span == 0) {
            for (int i = 0; i < population_size; i++) {
                int armSpeed = genes[i][gene_index] == 0 ? -1 : 1;
                int shoulderSpeed = genes[i][gene_index + 1] == 0 ? -1 : 1;
                int footSpeed = genes[i][gene_index + 2] == 0 ? -1 : 1;
                world.setIndividualMove(i, armSpeed * 2, shoulderSpeed, footSpeed * 2);
            }
            gene_index += gene_pair_size;
            if(gene_index >= gene_length){
                gene_index = 0;
            }

            // set slip time
            for(int i = 0; i < population_size; i++) {
                if(world.getIsSlipped(i) && slipTimes[i] == 0) {
                    slipTimes[i] = tickCount;
                }
            }
        }

        if(tickCount > action_span * gene_length / gene_pair_size) {
            tickCount = 0;
            generation += 1;

            for (int i = 0; i < population_size; i++) {
                fitnesses[i] += slipTimes[i] * 100;
            }

            average_fitness = 0;
            int best_fitness = Integer.MIN_VALUE;
            for (int i = 0; i < fitnesses.length; i++) {
                fitnesses[i] += world.getDistance(i);
                average_fitness += fitnesses[i];
                if(best_fitness < fitnesses[i]) {
                    best_fitness = fitnesses[i];
                }
            }
            average_fitness /= fitnesses.length;
            System.out.println("ave: " + average_fitness + " best: " + best_fitness);

            // save log
            for (int i = 0; i < population_size; i++) {
                storage.save(generation, fitnesses[i], genes[i]);
            }

            ga.generateNext(fitnesses);

            world.removeAllIndividuals();
            world.addIndividual(population_size);

            // reset fitnesses
            for (int i = 0; i < fitnesses.length; i++) {
                fitnesses[i] = 100;
            }

            // reset slipTimes
            for (int i = 0; i < slipTimes.length; i++) {
                slipTimes[i] = 0;
            }
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.MainLoop();
    }
}
