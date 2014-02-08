package physics;

public class PhysicsDataStructure {
	private PhysicsObject obj;
	private GameBodyType type;
	
	public PhysicsDataStructure(PhysicsObject obj, GameBodyType type)
	{
		this.obj = obj;
		this.type = type;
	}
	
	public GameBodyType getType()
	{
		return type;
	}
	
	public PhysicsObject getPhysicsObject()
	{
		return obj;
	}

}
