package userInterface;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMouseMovedEvent;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;



public class UIController implements ScreenController {
	@Override
	public void bind(Nifty nifty, Screen screen) {
		
	}
	
	@Override
	public void onStartScreen() {
	}
	
	@Override
	public void onEndScreen() {
	}
	
	/*
	@NiftyEventSubscriber(id="equipment")
	public void onHover(String id, NiftyMouseMovedEvent event){
		Element e = HUD.nifty.getCurrentScreen().findElementByName(id);
		PanelRenderer p = e.getRenderer(PanelRenderer.class);
		p.setBackgroundColor(new Color(1,0,0,1));
	}
	*/
}