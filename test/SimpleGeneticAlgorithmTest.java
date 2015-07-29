
import GeneticAlgorithm.SimpleGeneticAlgorithm;
import junit.framework.*;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

public class SimpleGeneticAlgorithmTest extends TestCase {
    public SimpleGeneticAlgorithmTest(String name) {
        super(name);
    }

    public void testSelection() {
        Random random = new Random();
        SimpleGeneticAlgorithm target = new SimpleGeneticAlgorithm(3, 3, 0, 0, random);

        int[][] genes = {
                { 1, 2, 3 },
                { 2, 3, 4 },
                { 0, 1, 2 },
        };
        float[] fitnesses = { 1, 0, 0 };

        target.setGenes(genes);
        target.select(fitnesses);
        int[][] result = target.getGenes();

        assertArrayEquals(result[0], genes[0]);
        assertArrayEquals(result[1], genes[0]);
        assertArrayEquals(result[2], genes[0]);
    }

    public void testCrossover() {
        SimpleGeneticAlgorithm target = new SimpleGeneticAlgorithm(2, 3, 0, 1);
        int[][] genes = {
                { 1, 1, 1 },
                { 2, 2, 2 },
        };

        target.setGenes(genes);
        target.crossover();
        int[][] result = target.getGenes();

        assertEquals(3, result[0][0] + result[1][0]);
        assertEquals(3, result[0][1] + result[1][1]);
        assertEquals(3, result[0][2] + result[1][2]);

        assertFalse(Arrays.equals(genes[0], result[0]));
        assertFalse(Arrays.equals(genes[1], result[1]));
    }

    public void testMutation() {
        SimpleGeneticAlgorithm target = new SimpleGeneticAlgorithm(2, 3, 1, 0);
        int[][] genes = {
                { 1, 0, 1 },
                { 0, 1, 0 },
        };

        target.setGenes(genes);
        target.mutate();
        int[][] result = target.getGenes();

        assertArrayEquals(new int[]{0, 1, 0}, result[0]);
        assertArrayEquals(new int[]{1, 0, 1}, result[1]);
    }

    public void testGetElite() {
        SimpleGeneticAlgorithm target = new SimpleGeneticAlgorithm(2, 3, 1, 0);
        int[][] genes = {
                { 1, 2, 3 },
                { 2, 3, 4 },
                { 4, 5, 6 },
        };
        float[] fitnesses = { 1, 2, 1 };

        target.setGenes(genes);
        int[] result = target.getElite(fitnesses);

        assertArrayEquals(genes[1], result);
    }

    public void testSetElite() {
        SimpleGeneticAlgorithm target = new SimpleGeneticAlgorithm(3, 3, 1, 0);
        int[][] genes = {
                { 1, 2, 3 },
                { 2, 3, 4 },
                { 4, 5, 6 },
        };

        target.setGenes(genes);
        target.setElite(genes[2]);
        int[][] result = target.getGenes();

        assertArrayEquals(genes[2], result[0]);
        assertArrayEquals(genes[1], result[1]);
        assertArrayEquals(genes[2], result[2]);
    }
}
