package physics;

public class PhysicsDataStructure {
	private PhysicsObject obj;
	private bodyType type;
	
	public PhysicsDataStructure(PhysicsObject obj, bodyType type)
	{
		this.obj = obj;
		this.type = type;
	}
	
	public bodyType getType()
	{
		return type;
	}
	
	public PhysicsObject getPhysicsObject()
	{
		return obj;
	}

}
