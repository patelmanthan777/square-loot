package physics;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

public class PhysicsContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		PhysicsDataStructure a = (PhysicsDataStructure) contact.getFixtureA()
				.getBody().getUserData();
		PhysicsDataStructure b = (PhysicsDataStructure) contact.getFixtureB()
				.getBody().getUserData();

		a.getPhysicsObject().ContactHandler(b);
		b.getPhysicsObject().ContactHandler(a);
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}
}
