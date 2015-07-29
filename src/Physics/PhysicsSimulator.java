package Physics;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.Joint;

public class PhysicsSimulator {
    private World world;

    private ArrayList<IndividualModel> individuals = new ArrayList<>();

    private Body groundBody;
    private Fixture groundFixture;

    public PhysicsSimulator() {
        initWorld();
    }

    public World getWorld() {
        return world;
    }

    public void initWorld() {
        Vec2 gravity = new Vec2(0.0f, 9.8f);
        world = new World(gravity);

        // ground
        FixtureDef groundFixtureDef = new FixtureDef();
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(20f, 0.1f);
        groundFixtureDef.shape = groundShape;
        groundFixtureDef.density = 25.0f;
        groundFixtureDef.filter = new Filter();
        groundFixtureDef.filter.categoryBits = 0x0001;

        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position = new Vec2(0.0f, 3f);
        groundBodyDef.angle = 0.0f;
        groundBodyDef.type = BodyType.STATIC;

        groundBody = world.createBody(groundBodyDef);
        groundFixture = groundBody.createFixture(groundFixtureDef);
    }

    public void addModel(IndividualModel model) {
        individuals.add(model);
    }

    public void removeModels() {
        for (IndividualModel individual : individuals) {
            individual.getJoints().forEach(world::destroyJoint);
            individual.getBodies().forEach(world::destroyBody);
        }
        individuals.clear();
    }

    public void draw(CanvasInterface canvas) {
        ModelRenderer.RenderBody(groundBody, 0, canvas);
    }

    public void step(float step_speed) {
        world.step(step_speed, 10, 10);
    }
}
