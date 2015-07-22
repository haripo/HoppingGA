import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

import java.util.ArrayList;
import java.util.List;

public class Individual {
    private ArrayList<PrismaticJoint> joints = new ArrayList<>();
    private ArrayList<RevoluteJoint> revoluteJoints = new ArrayList<>();
    private ArrayList<Body> bodies = new ArrayList<>();

    public Fixture bodyFixture;

    public Individual(World world, int floorCategory, int warmCategory) {
        Filter fixture_filter = new Filter();
        fixture_filter.categoryBits = warmCategory;
        fixture_filter.maskBits = floorCategory;

        // body
        Body bodyBody;
        {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.1f, 1.2f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 20.0f;
            fixtureDef.friction = 40.0f;
            fixtureDef.filter = fixture_filter;

            BodyDef bodyDef = new BodyDef();
            bodyDef.position = new Vec2(2.0f, 0.6f);
            bodyDef.angle = 0.0f;
            bodyDef.type = BodyType.DYNAMIC;

            Body boxBody = world.createBody(bodyDef);
            bodyFixture = boxBody.createFixture(fixtureDef);
            bodies.add(boxBody);
            bodyBody = boxBody;
        }

        // arm
        Body armBody;
        {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.2f, 0.1f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 20.0f;
            fixtureDef.friction = 40.0f;
            fixtureDef.filter = fixture_filter;

            BodyDef bodyDef = new BodyDef();
            bodyDef.position = new Vec2(2.2f, 1.3f);
            bodyDef.angle = 0.0f;
            bodyDef.type = BodyType.DYNAMIC;

            Body boxBody = world.createBody(bodyDef);
            boxBody.createFixture(fixtureDef);
            bodies.add(boxBody);
            armBody = boxBody;
        }

        // foot
        Body footBody;
        {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.1f, 0.2f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 20.0f;
            fixtureDef.friction = 40.0f;
            fixtureDef.filter = fixture_filter;

            BodyDef bodyDef = new BodyDef();
            bodyDef.position = new Vec2(2.0f, 1.3f);
            bodyDef.angle = 0.0f;
            bodyDef.type = BodyType.DYNAMIC;

            Body boxBody = world.createBody(bodyDef);
            boxBody.createFixture(fixtureDef);
            bodies.add(boxBody);
            footBody = boxBody;
        }

        // shoulder
        Body shoulderBody;
        {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.2f, 0.1f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 20.0f;
            fixtureDef.friction = 40.0f;
            fixtureDef.filter = fixture_filter;

            BodyDef bodyDef = new BodyDef();
            bodyDef.position = new Vec2(2.0f, 1.3f);
            bodyDef.angle = 0.0f;
            bodyDef.type = BodyType.DYNAMIC;

            Body boxBody = world.createBody(bodyDef);
            boxBody.createFixture(fixtureDef);
            bodies.add(boxBody);
            shoulderBody = boxBody;
        }

        // board
        Body boardBody;
        {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.1f, 1.6f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 20.0f;
            fixtureDef.friction = 40.0f;
            fixtureDef.filter = fixture_filter;

            BodyDef bodyDef = new BodyDef();
            bodyDef.position = new Vec2(3.0f, 0.6f);
            bodyDef.angle = 0.0f;
            bodyDef.type = BodyType.DYNAMIC;

            Body boxBody = world.createBody(bodyDef);
            boxBody.createFixture(fixtureDef);
            bodies.add(boxBody);
            boardBody = boxBody;
        }

        // shoulder-arm joint
        {
            PrismaticJointDef jointDef = new PrismaticJointDef();
            jointDef.bodyA = shoulderBody;
            jointDef.bodyB = armBody;
            jointDef.collideConnected = true;
            jointDef.enableMotor = true;
            jointDef.maxMotorForce = 1000f;
            jointDef.localAxisA.set(1, 0);
            jointDef.localAnchorA.set(0.1f, 0);
            jointDef.localAnchorB.set(-0.1f, 0.0f);
            jointDef.enableLimit = true;
            jointDef.upperTranslation = 2;
            jointDef.lowerTranslation = 1;
            PrismaticJoint joint = (PrismaticJoint)world.createJoint(jointDef);
            joints.add(joint);
        }

        // body-foot joint
        {
            PrismaticJointDef jointDef = new PrismaticJointDef();
            jointDef.bodyA = bodyBody;
            jointDef.bodyB = footBody;
            jointDef.localAnchorA.set(0f, 1.1f);
            jointDef.localAnchorB.set(0f, -0.1f);
            jointDef.localAxisA.set(0, 1);
            jointDef.collideConnected = true;
            jointDef.enableMotor = true;
            jointDef.maxMotorForce = 1000f;
            jointDef.enableLimit = true;
            jointDef.upperTranslation = 2;
            jointDef.lowerTranslation = 1;
            PrismaticJoint joint = (PrismaticJoint)world.createJoint(jointDef);
            joints.add(joint);
        }

        // shoulder-body joint
        {
            RevoluteJointDef jointDef = new RevoluteJointDef();
            jointDef.bodyA = shoulderBody;
            jointDef.bodyB = bodyBody;
            jointDef.localAnchorA.set(-0.1f, 0);
            jointDef.localAnchorB.set(0f, -0.5f);
            jointDef.collideConnected = false;
            jointDef.enableMotor = true;
            jointDef.maxMotorTorque = 100f;
            revoluteJoints.add((RevoluteJoint) world.createJoint(jointDef));
        }

        // foot-board joint
        {
            RevoluteJointDef jointDef = new RevoluteJointDef();
            jointDef.bodyA = footBody;
            jointDef.bodyB = boardBody;
            jointDef.localAnchorA.set(0f, 0.1f);
            jointDef.localAnchorB.set(0f, 0.8f);
            jointDef.collideConnected = false;
            world.createJoint(jointDef);
        }

        // arm-board joint
        {
            RevoluteJointDef jointDef = new RevoluteJointDef();
            jointDef.bodyA = armBody;
            jointDef.bodyB = boardBody;
            jointDef.localAnchorA.set(0.1f, 0);
            jointDef.localAnchorB.set(0f, -0.8f);
            jointDef.collideConnected = false;
            world.createJoint(jointDef);
        }
    }

    public List<Body> getBodies() {
        return bodies;
    }

    public void SetMotorSpeed(int i, int s){
        if(i < 2) {
            joints.get(i).setMotorSpeed(s * 2);
        } else {
            revoluteJoints.get(0).setMotorSpeed(s * 2);
        }
    }

    public int getDistance() {
        return (int)(bodies.get(0).getPosition().x * 100);
    }

    public int getHeight() {
        return (int)(bodies.get(0).getPosition().y);
    }
}
