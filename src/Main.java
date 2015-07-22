import GeneticAlgorithm.SimpleGeneticAlgorithm;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends JFrame {
    private Timer timer;
    private Canvas canvas;
    private PhysicsWorld world;

    private SimpleGeneticAlgorithm ga;
    private GeneStorage storage;

    private int gene_length = 100 * 3;
    private int population_size = 100;
    private double mutation_rate = 0.02;
    private double crossover_rate = 0.6;

    private int action_span = 3;
    private int gene_pair_size = 3;

    private double average_fitness = 0;

    private int tickCount = 0;
    private int step_speed = 1;
    private int gene_index = 0;
    private int generation = 0;


    private KeyAdapter keyAdapter = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            if (step_speed == 1) {
                step_speed = 10;
            } else {
                step_speed = 1;
            }
        }
    };

    public Main() {
        ga = new SimpleGeneticAlgorithm(
                population_size, gene_length, mutation_rate, crossover_rate);
        ga.randomInitialize();

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

        timer = new Timer(1000 / 30, (x) -> Tick());
        timer.start();
    }

    private int[] fitnesses = new int[population_size];

    public void Tick() {
        world.step(step_speed);

        // redraw canvas
        world.draw(canvas);
        canvas.repaint();

        int[][] genes = ga.getGenes();
        tickCount += 1;
        if (tickCount % action_span == 0) {
            for (int i = 0; i < population_size; i++) {
                for (int j = 0; j < gene_pair_size; j++) {
                    int sp = genes[i][gene_index + j] == 0 ? -1 : 1;
                    world.setMotorSpeed(i, j, sp);
                }
            }
            gene_index += gene_pair_size;
            if(gene_index >= gene_length){
                gene_index = 0;
            }

            for(int i = 0; i < population_size; i++) {
                if(world.getIsSlipped(i)) {
                    fitnesses[i] -= 5;
                }
            }
        }

        if(tickCount > action_span * gene_length / gene_pair_size) {
            tickCount = 0;
            generation += 1;

            average_fitness = 0;
            int best_fitness = 0;
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

            for (int i = 0; i < fitnesses.length; i++) {
                fitnesses[i] = 100;
            }
        }
    }

    public static void main(String[] args) {
        Main test = new Main();
    }
}
