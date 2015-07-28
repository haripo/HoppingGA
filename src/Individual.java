import Physics.IndividualModel;
import Physics.PhysicsSimulator;

public class Individual {
    private IndividualModel model;

    private int[] gene;

    private int actionSpan;

    private int actionIndex = 0;
    private int slipTime = 0;

    public static int genePairLength = 2;

    private boolean getIsSlipped() {
        return model.getHeadHeight() > 9.5f || model.getHandPositionY() > 9.5f;
    }

    public Individual(PhysicsSimulator simulator, int[] gene, int actionSpan) {
        this.gene = gene;
        this.actionSpan = actionSpan;
        model = new IndividualModel(simulator.getWorld());
    }

    public IndividualModel getModel() {
        return model;
    }

    public int getFitness() {
        //System.out.println("Distance: " + model.getDistance() + "  slip: " + slipTime);
        return model.getDistance() / 10 + slipTime;
    }

    public void step(int tick) {
        if(getIsSlipped() && slipTime == 0) {
            slipTime = tick;
        }

        if (tick % actionSpan == 0) {
            actionIndex += 1;

            int armSpeed = gene[actionIndex * 2] == 0 ? -1 : 1;
            int footSpeed = gene[actionIndex * 2 + 1] == 0 ? -1 : 1;
            model.setArmSpeed(armSpeed);
            model.setFootSpeed(footSpeed);
        }
    }
}
