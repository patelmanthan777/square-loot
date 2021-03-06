package physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import event.Timer;

public class PhysicsManager {
	private static World world;
	private static int velocityIterations = 8;
    private static int positionIterations = 3;
	
	public static void init()
	{
		 world = new World(new Vec2(0.0f, 0.0f));
		 PhysicsContactListener listener =  new PhysicsContactListener();
		 world.setContactListener(listener);
	}
	
	public static void update(float dt)
	{
	    world.step(dt/Timer.unitInOneSecond, velocityIterations, positionIterations);
	}
	
	public static Body createBody(BodyDef bodyDef)
	{
		Body b = world.createBody(bodyDef);
		return b;
	}
	
	public static void reinit(){
		Body current = world.getBodyList();
		Body next;
		while((current ) != null){
			next = current.getNext();
			world.destroyBody(current);
			current = next;
		}
	}
}
