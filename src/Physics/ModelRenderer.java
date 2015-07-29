package Physics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.Joint;

import java.util.List;

public class ModelRenderer {
    public static void RenderBody(Body body, int color, CanvasInterface canvas) {
        Transform transform = new Transform(
                body.getWorldCenter(),
                new Rot(body.getAngle()));
        Fixture fixture = body.getFixtureList();
        while (fixture != null) {
            switch (fixture.getType()) {
                case CIRCLE: {
                    CircleShape shape = (CircleShape) fixture.getShape();
                    Vec2 vertex = Transform.mul(transform, shape.getVertex(0));
                    canvas.drawCircle(vertex.x, vertex.y, shape.m_radius, color);
                    break;
                }
                case POLYGON: {
                    PolygonShape shape = (PolygonShape) fixture.getShape();
                    int vertexCount = shape.getVertexCount();
                    for (int i = 0; i < vertexCount; i++) {
                        int j = (i + 1) % vertexCount;
                        Vec2 vertexA = Transform.mul(transform, shape.getVertex(i));
                        Vec2 vertexB = Transform.mul(transform, shape.getVertex(j));
                        canvas.drawLine(vertexA.x, vertexA.y, vertexB.x, vertexB.y, color);
                    }
                    break;
                }
            }
            fixture = fixture.getNext();
        }
    }

    public static void RenderJoint(Joint joint, int color, CanvasInterface canvas) {
        Vec2 anchorA = new Vec2();
        Vec2 anchorB = new Vec2();
        joint.getAnchorA(anchorA);
        joint.getAnchorB(anchorB);
        canvas.drawCircle(anchorA.x, anchorA.y, 0.1f, color);
        canvas.drawCircle(anchorB.x, anchorB.y, 0.1f, color);
        canvas.drawLine(anchorA.x, anchorA.y, anchorB.x, anchorB.y, color);
    }

    public static void RenderBodies(List<Body> bodies, int color, CanvasInterface canvas) {
        bodies.forEach(body -> RenderBody(body, color, canvas));
    }

    public static void RenderJoints(List<Joint> joints, int color, CanvasInterface canvas) {
        joints.forEach(joint -> RenderJoint(joint, color, canvas));
    }
}
