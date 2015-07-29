package GeneticAlgorithm;

import java.util.Random;

public class SimpleGeneticAlgorithm {
    private int geneLength = 100;
    private int populationSize = 20;
    private double mutationRate = 0.02;
    private double crossoverRate = 0.6;

    private int[][] genes;

    private int[] geneOption = { -3, -2, -1, 0, 1, 2, 3 };

    private Random random;

    private int weightedSample(float[] values) {
        float sum = 0;
        for (float value : values) {
            sum += value;
        }

        double threshold = random.nextDouble() * sum;
        for (int i = 0; i < values.length; i++) {
            threshold -= values[i];
            if(threshold <= 0) return i;
        }

        return values.length - 1;
    }

    private int getRandomGene() {
        return geneOption[random.nextInt(geneOption.length)];
    }

    public SimpleGeneticAlgorithm(int populationSize, int geneLength, double mutationRate, double crossoverRate,
                                  Random random) {
        this.populationSize = populationSize;
        this.geneLength = geneLength;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.random = random;
    }

    public SimpleGeneticAlgorithm(int populationSize, int geneLength, double mutationRate, double crossoverRate) {
        this(populationSize, geneLength, mutationRate, crossoverRate, new Random());
    }

    public void setGenes(int[][] newGenes) {
        // deep copy
        genes = new int[populationSize][geneLength];
        for (int i = 0; i < populationSize; i++) {
            genes[i] = newGenes[i].clone();
        }
    }

    public int[][] getGenes() {
        // deep copy
        int[][] result = new int[populationSize][geneLength];
        for (int i = 0; i < populationSize; i++) {
            result[i] = genes[i].clone();
        }
        return result;
    }

    public void randomInitialize() {
        genes = new int[populationSize][geneLength];
        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < geneLength; j++) {
                genes[i][j] = getRandomGene();
            }
        }
    }

    public void generateNext(float[] fitnesses) {
        int[] elite = getElite(fitnesses);
        select(fitnesses);
        crossover();
        mutate();
        setElite(elite);
    }

    public void select(float[] fitnesses) {
        int[][] result = new int[genes.length][geneLength];

        for (int i = 0; i < genes.length; i++) {
            int selected = 0;
            for (int j = 0; j < 16; j++) {
                int target = random.nextInt(genes.length);
                if (fitnesses[target] > fitnesses[selected]) {
                    selected = target;
                }
            }
            result[i] = genes[selected].clone();
        }

        genes = result;
    }

    public void crossover() {
        for (int i = 0; i < genes.length; i += 2) {
            if (random.nextDouble() > crossoverRate) {
                continue;
            }

            int begin = random.nextInt(geneLength);
            int end = begin + random.nextInt(geneLength - begin);
            for (int j = begin; j < end; j++) {
                int tmp = genes[i][j];
                genes[i][j] = genes[i + 1][j];
                genes[i + 1][j] = tmp;
            }
        }
    }

    public void mutate() {
        for (int i = 0; i < genes.length; i++) {
            for (int j = 0; j < genes[i].length; j++) {
                if (random.nextDouble() < mutationRate) {
                    genes[i][j] = getRandomGene();
                }
            }
        }
    }

    public int[] getElite(float[] fitnesses) {
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
