import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

import java.util.ArrayList;
import java.util.List;

public class WormIndividual {
    private ArrayList<RevoluteJoint> joints = new ArrayList<>();
    private ArrayList<Body> bodies = new ArrayList<>();

    public WormIndividual(World world, int floorCategory, int warmCategory) {
        Filter fixture_filter = new Filter();
        fixture_filter.categoryBits = warmCategory;
        fixture_filter.maskBits = floorCategory;

        // box
        for (int i = 0; i < 3; i++) {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.1f, 0.6f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 20.0f;
            fixtureDef.friction = 40.0f;
            fixtureDef.filter = fixture_filter;

            BodyDef bodyDef = new BodyDef();
            bodyDef.position = new Vec2(2.0f, 0.6f * i);
            bodyDef.angle = 0.0f;
            bodyDef.type = BodyType.DYNAMIC;

            Body boxBody = world.createBody(bodyDef);
            boxBody.createFixture(fixtureDef);
            bodies.add(boxBody);
        }

        // joint
        for (int i = 0; i < bodies.size() - 1; i++) {
            RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
            revoluteJointDef.bodyA = bodies.get(i);
            revoluteJointDef.bodyB = bodies.get(i + 1);
            revoluteJointDef.localAnchorA = new Vec2(0f, 0.55f);
            revoluteJointDef.localAnchorB = new Vec2(0f, -0.55f);
            revoluteJointDef.collideConnected = false;
            revoluteJointDef.enableMotor = true;
            revoluteJointDef.maxMotorTorque = 1000f;
            revoluteJointDef.motorSpeed = 0.5f;
            RevoluteJoint joint = (RevoluteJoint)world.createJoint(revoluteJointDef);
            joints.add(joint);
        }
    }

    public List<Body> getBodies() {
        return bodies;
    }

    public void SetMotorSpeed(int i, int s){
        joints.get(i).setMotorSpeed(s);
    }

    public int getDistance() {
        return (int)(bodies.get(0).getPosition().x * 100);
    }
}
