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
        FixtureDef groundFixture = new FixtureDef();
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(20f, 0.1f);
        groundFixture.shape = groundShape;
        groundFixture.density = 25.0f;
        groundFixture.filter = new Filter();
        groundFixture.filter.categoryBits = 0x0001;

        BodyDef groundBody = new BodyDef();
        groundBody.position = new Vec2(0.0f, 11.0f);
        groundBody.angle = 0.0f;
        groundBody.type = BodyType.STATIC;
        world.createBody(groundBody).createFixture(groundFixture);
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

    public void draw(Renderer canvas) {
        canvas.fill();

        Body body = world.getBodyList();
        while (body != null) {
            Transform transform = new Transform(
                    body.getWorldCenter(),
                    new Rot(body.getAngle()));
            Fixture fixture = body.getFixtureList();
            while (fixture != null) {
                switch (fixture.getType()) {
                    case CIRCLE: {
                        CircleShape shape = (CircleShape) fixture.getShape();
                        Vec2 vertex = Transform.mul(transform, shape.getVertex(0));
                        canvas.drawCircle(vertex.x, vertex.y, shape.m_radius);
                        break;
                    }
                    case POLYGON: {
                        PolygonShape shape = (PolygonShape) fixture.getShape();
                        int vertexCount = shape.getVertexCount();
                        for (int i = 0; i < vertexCount; i++) {
                            int j = (i + 1) % vertexCount;
                            Vec2 vertexA = Transform.mul(transform, shape.getVertex(i));
                            Vec2 vertexB = Transform.mul(transform, shape.getVertex(j));
                            canvas.drawLine(vertexA.x, vertexA.y, vertexB.x, vertexB.y);
                        }
                        break;
                    }
                }
                fixture = fixture.getNext();
            }
            body = body.getNext();
        }

        Joint joint = world.getJointList();
        while (joint != null) {
            Vec2 anchorA = new Vec2();
            Vec2 anchorB = new Vec2();
            joint.getAnchorA(anchorA);
            joint.getAnchorB(anchorB);
            canvas.drawCircle(anchorA.x, anchorA.y, 0.1f);
            canvas.drawCircle(anchorB.x, anchorB.y, 0.1f);
            canvas.drawLine(anchorA.x, anchorA.y, anchorB.x, anchorB.y);
            joint = joint.getNext();
        }
    }

    public void step(int step_speed) {
        world.step(1.0f / 30.0f * step_speed, 10, 10);
    }
}
