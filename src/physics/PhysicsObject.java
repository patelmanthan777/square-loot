package physics;

public interface PhysicsObject {
	public void ContactHandler(PhysicsDataStructure a);
	public void EndContactHandler(PhysicsDataStructure a);
	public void initPhysics();
}
