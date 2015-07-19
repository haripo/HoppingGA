import java.util.ArrayList;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;

public class PhysicsWorld {
    private World world;

    private int warmCategory = 0x0001;
    private int floorCategory = 0x0002;

    private ArrayList<Individual> individuals = new ArrayList<>();

    public PhysicsWorld() {
        initWorld();
    }

    public void initWorld() {
        Vec2 gravity = new Vec2(0.0f, 9.8f);
        world = new World(gravity);

        // ground
        FixtureDef groundFixture = new FixtureDef();
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(100f, 0.1f);
        groundFixture.shape = groundShape;
        groundFixture.density = 25.0f;
        groundFixture.filter = new Filter();
        groundFixture.filter.categoryBits = floorCategory;

        BodyDef groundBody = new BodyDef();
        groundBody.position = new Vec2(0.0f, 11.0f);
        groundBody.angle = 0.0f;
        groundBody.type = BodyType.STATIC;
        world.createBody(groundBody).createFixture(groundFixture);
    }

    public void addIndividual(int indCount) {
        for(int i = 0; i < indCount; i++) {
            Individual individual = new Individual(world, floorCategory, warmCategory);
            individuals.add(individual);
        }
    }

    public void removeAllIndividuals() {
        for (Individual individual : individuals) {
            for (Body body : individual.getBodies()) {
                world.destroyBody(body);
            }
        }
        individuals.clear();
    }

    public void draw(Canvas canvas) {
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
                        canvas.drawCircle(shape.m_p.x, shape.m_p.y, shape.m_radius);
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
    }

    public void setMotorSpeed(int individual, int i, int speed) {
        individuals.get(individual).SetMotorSpeed(i, speed);
    }

    public int getDistance(int individual){
        return individuals.get(individual).getDistance();
    }

    public void step() {
        world.step(1.0f / 30.0f, 10, 10);
    }
}
