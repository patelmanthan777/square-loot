package physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import entity.player.Player;
import environment.Map;

public class PhysicsManager {
	public static World world;
	
	public static void init(Map m, Player p)
	{
		 world = new World(new Vec2(0.0f, 0.0f));
		 m.initPhysics(world);
		 p.initPhysics(world);
	}
	
	public static void update(float dt)
	{
		int velocityIterations = 8;
	    int positionIterations = 3;
	    world.step(dt, velocityIterations, positionIterations);
	}
}
