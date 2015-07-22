import GeneticAlgorithm.SimpleGeneticAlgorithm;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends JFrame {
    private Timer timer;
    private Canvas canvas;
    private PhysicsWorld world;

    private SimpleGeneticAlgorithm ga;
    private GeneStorage storage;

    private int gene_length = 100;
    private int population_size = 20;
    private double mutation_rate = 0.02;
    private double crossover_rate = 0.6;

    private double average_fitness = 0;

    private int tickCount = 0;
    private int gene_index = 0;
    private int generation = 0;

    public Main() {
        ga = new SimpleGeneticAlgorithm(
                population_size, gene_length, mutation_rate, crossover_rate);
        ga.randomInitialize();

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'hoppingGA_'yyyymmdd'_'HHmmss'.log'");
        storage = new GeneStorage(dateFormat.format(new Date()));
        InitFrame();
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

        timer = new Timer(1000 / 300, (x) -> Tick());
        timer.start();
    }

    public void Tick() {
        world.step();
        // redraw canvas
        world.draw(canvas);
        canvas.repaint();

        int[][] genes = ga.getGenes();
        tickCount += 1;
        if (tickCount % 30 == 0) {
            for (int i = 0; i < population_size; i++) {
                for (int j = 0; j < 2; j++) {
                    int sp = genes[i][gene_index + j] == 0 ? -1 : 1;
                    world.setMotorSpeed(i, j, sp);
                }
            }
            gene_index += 2;
            if(gene_index >= gene_length){
                gene_index = 0;
            }
        }

        if(tickCount > 30 * 50) {
            tickCount = 0;
            generation += 1;

            int[] fitnesses = new int[population_size];
            average_fitness = 0;
            int best_fitness = 0;
            for (int i = 0; i < fitnesses.length; i++) {
                fitnesses[i] = world.getDistance(i);
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
        }
    }

    public static void main(String[] args) {
        Main test = new Main();
    }
}
