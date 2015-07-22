package GeneticAlgorithm;

import java.util.Random;

public class SimpleGeneticAlgorithm {
    private int gene_length = 100;
    private int population_size = 20;
    private double mutation_rate = 0.02;
    private double crossover_rate = 0.6;

    private int[][] genes;

    private Random random;

    private int weightedSample(int[] values) {
        // TODO: divide normalize function
        double sum = 0;
        for (int value : values) {
            sum += value;
        }

        double threshold = random.nextDouble() * sum;
        for (int i = 0; i < values.length; i++) {
            threshold -= values[i];
            if(threshold <= 0) return i;
        }

        return values.length - 1;
    }

    public SimpleGeneticAlgorithm(int population_size, int gene_length, double mutation_rate, double crossover_rate,
                                  Random random) {
        this.population_size = population_size;
        this.gene_length = gene_length;
        this.mutation_rate = mutation_rate;
        this.crossover_rate = crossover_rate;
        this.random = random;
    }

    public SimpleGeneticAlgorithm(int population_size, int gene_length, double mutation_rate, double crossover_rate) {
        this(population_size, gene_length, mutation_rate, crossover_rate, new Random());
    }

    public void setGenes(int[][] newGenes) {
        // deep copy
        genes = new int[population_size][gene_length];
        for (int i = 0; i < population_size; i++) {
            genes[i] = newGenes[i].clone();
        }
    }

    public int[][] getGenes() {
        // deep copy
        int[][] result = new int[population_size][gene_length];
        for (int i = 0; i < population_size; i++) {
            result[i] = genes[i].clone();
        }
        return result;
    }

    public void randomInitialize() {
        genes = new int[population_size][gene_length];
        for (int i = 0; i < population_size; i++) {
            for (int j = 0; j < gene_length; j++) {
                genes[i][j] = random.nextInt(2);
            }
        }
    }

    public void generateNext(int[] fitnesses) {
        int[] elite = getElite(fitnesses);
        select(fitnesses);
        crossover();
        mutate();
        setElite(elite);
    }

    public void select(int[] fitnesses) {
        int[][] result = new int[genes.length][gene_length];

        for (int i = 0; i < genes.length; i++) {
            int selected = weightedSample(fitnesses);
            result[i] = genes[selected].clone();
        }

        genes = result;
    }

    public void crossover() {
        for (int i = 0; i < genes.length; i += 2) {
            if (random.nextDouble() > crossover_rate) {
                continue;
            }

            int point = random.nextInt(gene_length);
            for (int j = point; j < gene_length; j++) {
                int tmp = genes[i][j];
                genes[i][j] = genes[i + 1][j];
                genes[i + 1][j] = tmp;
            }
        }
    }

    public void mutate() {
        for (int i = 0; i < genes.length; i++) {
            for (int j = 0; j < genes[i].length; j++) {
                if (random.nextDouble() < mutation_rate) {
                    // TODO: 対立遺伝子の獲得を外部に出す
                    switch (genes[i][j]) {
                        case 0:
                            genes[i][j] = 1;
                            break;
                        case 1:
                            genes[i][j] = 0;
                            break;
                    }
                }
            }
        }
    }

    public int[] getElite(int[] fitnesses) {
        int best = 0;
        for (int i = 1; i < fitnesses.length; i++) {
            if (fitnesses[i] > fitnesses[best]) {
                best = i;
            }
        }
        return genes[best].clone();
    }

    public void setElite(int[] elite) {
        genes[0] = elite.clone();
    }
}
