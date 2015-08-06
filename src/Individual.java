import Physics.IndividualModel;
import Physics.ModelRenderer;
import Physics.PhysicsSimulator;
import Physics.CanvasInterface;

public class Individual {
    private IndividualModel model;

    private int[] gene;

    private int actionSpan;

    private boolean elite = false;
    private int actionIndex = 0;
    private int slipTime = 0;
    private int cumulativeDistance = 0;

    public static int genePairLength = 2;

    private boolean getIsSlipped() {
        return model.getHeadHeight() > 1.5f || model.getHandPositionY() > 1.5f;
    }

    public Individual(PhysicsSimulator simulator, int[] gene, int actionSpan) {
        this.gene = gene;
        this.actionSpan = actionSpan;
        model = new IndividualModel(simulator.getWorld());
    }

    public IndividualModel getModel() {
        return model;
    }

    public void setAsElite() {
        elite = true;
    }

    public int getSlipTime() {
        return slipTime;
    }

    public float getCumulativeDistance() {
        return cumulativeDistance;
    }

    public void step(int tick) {
        if(getIsSlipped() && slipTime == 0) {
            slipTime = tick;
        }

        cumulativeDistance += model.getDistance();

        if (tick % actionSpan == 0) {
            actionIndex += 1;
            model.setArmSpeed(gene[actionIndex * 2]);
            model.setFootSpeed(gene[actionIndex * 2 + 1]);
        }
    }

    public void draw(CanvasInterface canvas) {
        int color = 0;
        if (elite) {
            color = 2;
        } else if (slipTime != 0) {
            color = 1;
        }

        ModelRenderer.RenderBodies(model.getBodies(), color, canvas);
        ModelRenderer.RenderJoints(model.getJoints(), color, canvas);
    }
}
