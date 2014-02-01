package physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import entity.EntityManager;
import environment.Map;

public class PhysicsManager {
	private static World world;
	private static int velocityIterations = 8;
    private static int positionIterations = 3;
	
	public static void init(Map m)
	{
		 world = new World(new Vec2(0.0f, 0.0f));
		 m.initPhysics(world);
		 EntityManager.initPhysics(world);
	}
	
	public static void update(float dt)
	{
	    world.step(dt, velocityIterations, positionIterations);
	}
	
	public static Body createBody(BodyDef bodyDef)
	{
		Body b = world.createBody(bodyDef);
		return b;
	}
}
