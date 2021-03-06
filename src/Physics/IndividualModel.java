package Physics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

import java.util.ArrayList;
import java.util.List;

public class IndividualModel {
    private ArrayList<Joint> joints = new ArrayList<>();
    private ArrayList<Body> bodies = new ArrayList<>();

    private Body bodyBody;
    private Body boardBody;
    private Body handBody;
    private Body shoulderBody;
    private Body footBody;

    private Fixture headFixture;

    private PrismaticJoint shoulderArmJoint;
    private PrismaticJoint bodyFootJoint;
    private RevoluteJoint shoulderBodyJoint;

    public IndividualModel(World world) {
        Filter fixture_filter = new Filter();
        fixture_filter.categoryBits = 0x0002;
        fixture_filter.maskBits = 0x0001;

        // body
        {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.1f, 1.2f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 20.0f;
            fixtureDef.friction = 40.0f;
            fixtureDef.filter = fixture_filter;

            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(0.3f);
            circleShape.m_p.set(0, -1.0f);

            FixtureDef headFixtureDef = new FixtureDef();
            headFixtureDef.shape = circleShape;
            headFixtureDef.density = 20.0f;
            headFixtureDef.friction = 40.0f;
            headFixtureDef.filter = fixture_filter;

            BodyDef bodyDef = new BodyDef();
            bodyDef.position = new Vec2(2.0f, 0.6f);
            bodyDef.angle = 0.0f;
            bodyDef.type = BodyType.DYNAMIC;

            bodyBody = world.createBody(bodyDef);
            bodyBody.createFixture(fixtureDef);
            headFixture = bodyBody.createFixture(headFixtureDef);
            bodies.add(bodyBody);
        }

        // hand
        {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.2f, 0.1f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.filter = fixture_filter;
            fixtureDef.density = 20.0f;
            fixtureDef.friction = 40.0f;

            BodyDef bodyDef = new BodyDef();
            bodyDef.position = new Vec2(2.2f, 1.3f);
            bodyDef.type = BodyType.DYNAMIC;

            handBody = world.createBody(bodyDef);
            handBody.createFixture(fixtureDef);
            bodies.add(handBody);
        }

        // foot
        {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.1f, 0.2f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.filter = fixture_filter;
            fixtureDef.density = 20.0f;
            fixtureDef.friction = 40.0f;

            BodyDef bodyDef = new BodyDef();
            bodyDef.position = new Vec2(2.0f, 1.3f);
            bodyDef.type = BodyType.DYNAMIC;

            footBody = world.createBody(bodyDef);
            footBody.createFixture(fixtureDef);
            bodies.add(footBody);
        }

        // shoulder
        {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.2f, 0.1f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.filter = fixture_filter;
            fixtureDef.density = 20.0f;
            fixtureDef.friction = 40.0f;

            BodyDef bodyDef = new BodyDef();
            bodyDef.position = new Vec2(2.0f, 1.3f);
            bodyDef.type = BodyType.DYNAMIC;

            shoulderBody = world.createBody(bodyDef);
            shoulderBody.createFixture(fixtureDef);
            bodies.add(shoulderBody);
        }

        // board
        {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.1f, 1.5f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 20.0f;
            fixtureDef.friction = 40.0f;
            fixtureDef.filter = fixture_filter;

            BodyDef bodyDef = new BodyDef();
            bodyDef.position = new Vec2(3.0f, 0.6f);
            bodyDef.angle = 0.0f;
            bodyDef.type = BodyType.DYNAMIC;

            boardBody = world.createBody(bodyDef);
            boardBody.createFixture(fixtureDef);
            bodies.add(boardBody);
        }

        // shoulder-arm joint
        {
            PrismaticJointDef jointDef = new PrismaticJointDef();
            jointDef.bodyA = shoulderBody;
            jointDef.bodyB = handBody;
            jointDef.collideConnected = true;
            jointDef.enableMotor = true;
            jointDef.maxMotorForce = 1000f;
            jointDef.localAxisA.set(1, 0);
            jointDef.localAnchorA.set(0.1f, 0);
            jointDef.localAnchorB.set(-0.1f, 0.0f);
            jointDef.enableLimit = true;
            jointDef.upperTranslation = 1;
            jointDef.lowerTranslation = 0;
            shoulderArmJoint = (PrismaticJoint)world.createJoint(jointDef);
            joints.add(shoulderArmJoint);
        }

        // body-foot joint
        {
            PrismaticJointDef jointDef = new PrismaticJointDef();
            jointDef.bodyA = bodyBody;
            jointDef.bodyB = footBody;
            jointDef.localAnchorA.set(0f, 0.7f);
            jointDef.localAnchorB.set(0f, -0.1f);
            jointDef.localAxisA.set(0, 1);
            jointDef.collideConnected = true;
            jointDef.enableMotor = true;
            jointDef.maxMotorForce = 1000f;
            jointDef.enableLimit = true;
            jointDef.upperTranslation = 1;
            jointDef.lowerTranslation = 0;
            bodyFootJoint = (PrismaticJoint) world.createJoint(jointDef);
            joints.add(bodyFootJoint);
        }

        // shoulder-body joint
        {
            RevoluteJointDef jointDef = new RevoluteJointDef();
            jointDef.bodyA = shoulderBody;
            jointDef.bodyB = bodyBody;
            jointDef.localAnchorA.set(-0.1f, 0);
            jointDef.localAnchorB.set(0f, -0.5f);
            jointDef.collideConnected = false;
            shoulderBodyJoint = (RevoluteJoint) world.createJoint(jointDef);
            joints.add(shoulderBodyJoint);
        }

        // foot-board joint
        {
            RevoluteJointDef jointDef = new RevoluteJointDef();
            jointDef.bodyA = footBody;
            jointDef.bodyB = boardBody;
            jointDef.localAnchorA.set(0f, 0.1f);
            jointDef.localAnchorB.set(0f, 1.0f);
            jointDef.collideConnected = false;
            joints.add(world.createJoint(jointDef));
        }

        // arm-board joint
        {
            RevoluteJointDef jointDef = new RevoluteJointDef();
            jointDef.bodyA = handBody;
            jointDef.bodyB = boardBody;
            jointDef.localAnchorA.set(0.1f, 0);
            jointDef.localAnchorB.set(0f, -1.0f);
            jointDef.collideConnected = false;
            joints.add(world.createJoint(jointDef));
        }
    }

    public List<Body> getBodies() {
        return bodies;
    }

    public ArrayList<Joint> getJoints() {
        return joints;
    }

    public void setArmSpeed(float speed) {
        shoulderArmJoint.setMotorSpeed(speed);
    }

    public void setFootSpeed(float speed) {
        bodyFootJoint.setMotorSpeed(speed);
    }

    public float getDistance() {
        return footBody.getPosition().x;
    }

    public float getHeadHeight() {
        Vec2 position = ((CircleShape)headFixture.getShape()).m_p;
        position = position.add(headFixture.getBody().getPosition());
        return position.y;
    }

    public float getHandPositionY() {
        Vec2 position = handBody.getPosition();
        return position.y;
    }
}
