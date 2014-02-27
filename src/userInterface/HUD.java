package userInterface;

import static org.lwjgl.opengl.GL11.*;

import item.ItemListEnum;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import userInterface.inventory.Inventory;
import userInterface.inventory.InventoryItemEnum;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.nulldevice.NullSoundDevice;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.renderer.lwjgl.input.LwjglInputSystem;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;
import de.lessvoid.nifty.tools.SizeValue;
import entity.player.Player;
import environment.room.OxygenRoom;
import event.Timer;

public class HUD {
	private static int TEXTHEIGHT = 30; 
	
	private static Player player;
	private static Inventory inventory;
	static Nifty nifty;
	private static HashMap<ItemListEnum, NiftyImage> itemImages = new HashMap<ItemListEnum, NiftyImage>();
	
	
	public static void init(){
				
		
		LwjglInputSystem inputSystem = new LwjglInputSystem();
		
		try {
			inputSystem.startup();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		nifty = new Nifty(
				new LwjglRenderDevice(),
				new NullSoundDevice(),
				inputSystem,
				new AccurateTimeProvider());
			
		drawGameScreen();
		
		
		itemImages.put(ItemListEnum.LASERRIFLE,
					   nifty.createImage("assets/textures/laserRifle.png",
							   			 true));
		
		nifty.gotoScreen("gui");
	}
	
	public static boolean update(){				
		updateEquipment();
		updateHealthBar();
		updateOxygenBar();
		updateApneaBar();
		updateTexts();
		
		return nifty.update();
	}
	
	private static void updateEquipment(){
		Element e = HUD.nifty.getCurrentScreen().findElementByName("equipment1");
		ImageRenderer img = e.getRenderer(ImageRenderer.class);
		img.setImage(itemImages.get(inventory.getInfo(InventoryItemEnum.PWEAPON)));
		
		e = HUD.nifty.getCurrentScreen().findElementByName("equipment2");
		img = e.getRenderer(ImageRenderer.class);
		img.setImage(itemImages.get(inventory.getInfo(InventoryItemEnum.SWEAPON)));
		
		e = HUD.nifty.getCurrentScreen().findElementByName("equipment3");
		img = e.getRenderer(ImageRenderer.class);
		img.setImage(itemImages.get(inventory.getInfo(InventoryItemEnum.SHIELD)));
		
		e = HUD.nifty.getCurrentScreen().findElementByName("equipment4");
		img = e.getRenderer(ImageRenderer.class);
		img.setImage(itemImages.get(inventory.getInfo(InventoryItemEnum.ACCESSORY)));
		
		e = HUD.nifty.getCurrentScreen().findElementByName("equipment5");
		img = e.getRenderer(ImageRenderer.class);
		img.setImage(itemImages.get(inventory.getInfo(InventoryItemEnum.MGEAR)));
	}
	
	private static void updateHealthBar(){
		Element e = HUD.nifty.getCurrentScreen().findElementByName("healthbar");
		int width = 100 * player.getHealth() / player.getMaxHealth();
		e.setConstraintWidth(SizeValue.percent(width));
		e.getParent().layoutElements();
	}
	
	private static void updateOxygenBar(){
		Element e = HUD.nifty.getCurrentScreen().findElementByName("oxygenbar");
		int width = 100 * player.getPressure() / OxygenRoom.maxPressure;
		e.setConstraintWidth(SizeValue.percent(width));
		e.getParent().layoutElements();
	}
	
	private static void updateApneaBar(){
		Element e = HUD.nifty.getCurrentScreen().findElementByName("apneabar");
		int width = player.getApnea();
		e.setConstraintWidth(SizeValue.percent(width));
		e.getParent().layoutElements();
	}
	
	private static void updateTexts(){
		Element e = HUD.nifty.getCurrentScreen().findElementByName("fps");
		TextRenderer txt = e.getRenderer(TextRenderer.class);
		String s = Timer.getFPSDisplay();
		txt.setText(s);
			
		e = HUD.nifty.getCurrentScreen().findElementByName("time");
		txt = e.getRenderer(TextRenderer.class);
		s = Timer.getChrono();
		txt.setText(s);
		
		e = HUD.nifty.getCurrentScreen().findElementByName("energy");
		txt = e.getRenderer(TextRenderer.class);
		s = "" + player.getEnergy();
		txt.setText(s);
				
		e = HUD.nifty.getCurrentScreen().findElementByName("battery");
		txt = e.getRenderer(TextRenderer.class);
		s = "" + player.getBatteriesNb();
		txt.setText(s);
	}
	
	public static void render(){
		GL11.glDisable(GL11.GL_CULL_FACE);       
        
		nifty.render(false);
        
        GL11.glEnable(GL11.GL_CULL_FACE); 
		glEnable(GL_TEXTURE_2D);
		
	}
	
	
	static public void registerInventory(Inventory i){
		inventory = i;
	}
	
	static  public void registerPlayer(Player p){
		player = p;
	}
	
	private static void drawGameScreen(){
		new ScreenBuilder("gui") {{
			controller(new UIController());
			layer(new LayerBuilder(){{
				childLayoutHorizontal();	   						    	   					    		       	   					    	   					    	   					    	   					
    		   
				panel( new PanelBuilder(){{
					width("25%");
					childLayoutCenter();
					
					panel( new PanelBuilder(){{
						width("80%");
						height("90%");
						childLayoutVertical();

						panel( new PanelBuilder(){{							
							childLayoutCenter();
							
							panel( new PanelBuilder(){{
								
								childLayoutHorizontal();
								height(pixels(TEXTHEIGHT));
								
								panel( new PanelBuilder(){{
									width("20%");
									childLayoutHorizontal();
									text(new TextBuilder(){{									
										font("aurulent-sans-16.fnt");
										text("FPS:");	
									}});
								}});
								
								panel( new PanelBuilder(){{							
									childLayoutHorizontal();
									text(new TextBuilder("fps"){{
										font("aurulent-sans-16.fnt");
										text("");	
									}});
								}});
							}});
						}});
						
						panel( new PanelBuilder(){{							
							childLayoutCenter();

							panel( new PanelBuilder(){{							
								childLayoutHorizontal();
								height(pixels(TEXTHEIGHT));
								
								panel( new PanelBuilder(){{
									width("20%");
									childLayoutHorizontal();
								
									text(new TextBuilder(){{
										font("aurulent-sans-16.fnt");
										text("Time:");	
									}});
								}});
								
								panel( new PanelBuilder(){{									
									childLayoutHorizontal();
									
									text(new TextBuilder("time"){{
										font("aurulent-sans-16.fnt");
										text("");	
									}});
								}});
							}});
						}});
						
						panel( new PanelBuilder(){{							
							childLayoutCenter();
						
							panel( new PanelBuilder(){{							
								childLayoutHorizontal();
								height(pixels(TEXTHEIGHT));
								
								panel( new PanelBuilder(){{
									width("20%");
									childLayoutHorizontal();

									image( new ImageBuilder(){{
										width("30%");
										height("80%");
										filename("assets/textures/energy.png");
									}});									
								}});

								panel( new PanelBuilder(){{
									childLayoutHorizontal();

									text(new TextBuilder("energy"){{
										font("aurulent-sans-16.fnt");
										text("");	
									}});
								}});
							}});
						}});
						
						panel( new PanelBuilder(){{							
							childLayoutCenter();
						
							panel( new PanelBuilder(){{							
								childLayoutHorizontal();
								height(pixels(TEXTHEIGHT));
								
								panel( new PanelBuilder(){{
									width("20%");
									childLayoutHorizontal();

									image( new ImageBuilder(){{
										width("20%");
										height("85%");
										filename("assets/textures/battery.png");
									}});									
								}});
								
								panel( new PanelBuilder(){{									
									childLayoutHorizontal();

									text(new TextBuilder("battery"){{
										font("aurulent-sans-16.fnt");
										text("");	
									}});
								}});
							}});
						}});
						
					}});
					
					panel( new PanelBuilder(){{
						width("30%");
						childLayoutVertical();
					}});
					
				}});
    		   
				panel( new PanelBuilder(){{
					width("50%");
					childLayoutVertical();
					
					panel( new PanelBuilder(){{
						height("8%");
						childLayoutCenter();
						panel( new PanelBuilder(){{
							padding("5px");
							width("50%");
							backgroundColor("#002f");
							id("equipment");
							childLayoutHorizontal();
							
							panel( new PanelBuilder(){{
								childLayoutCenter();
								padding("5px");
								panel( new PanelBuilder(){{
									backgroundColor("#001f");									
									childLayoutCenter();
									image( new ImageBuilder("equipment1"){{									
										width("100%");
										height("100%");									
										/**/
									}});
								}});
							}});							
							
							panel( new PanelBuilder(){{
								childLayoutCenter();
								padding("5px");
								panel( new PanelBuilder(){{
									backgroundColor("#001f");
									
									childLayoutCenter();
									image( new ImageBuilder("equipment2"){{
										width("100%");
										height("100%");
										/**/
									}});
								}});
							}});
							
							panel( new PanelBuilder(){{
								childLayoutCenter();
								padding("5px");
								panel( new PanelBuilder(){{
									backgroundColor("#001f");									
									childLayoutCenter();
									image( new ImageBuilder("equipment3"){{
										width("100%");
										height("100%");
										/**/
									}});
								}});
							}});
							
							panel( new PanelBuilder(){{
								childLayoutCenter();
								padding("5px");
								panel( new PanelBuilder(){{
									backgroundColor("#001f");									
									childLayoutCenter();
									image( new ImageBuilder("equipment4"){{									
										width("100%");
										height("100%");
										/**/
									}});
								}});
							}});
							
							panel( new PanelBuilder(){{
								childLayoutCenter();
								padding("5px");
								panel( new PanelBuilder(){{
									backgroundColor("#001f");									
									childLayoutCenter();
									image( new ImageBuilder("equipment5"){{
										width("100%");
										height("100%");
										/**/
									}});
								}});
							}});
						}});											
					}});
					
					panel( new PanelBuilder(){{
						height("92%");
						childLayoutVertical();
						
						panel(new PanelBuilder(){{
							height("90%");
						}});
						
						panel(new PanelBuilder(){{
							height("5%");
							childLayoutCenter();
							
							panel(new PanelBuilder(){{
								height("90%");
								width("75%");
								childLayoutVertical();
								
								panel(new PanelBuilder("healthbarframe"){{
									
									backgroundColor("#ffff");
									childLayoutCenter();
									
									panel(new PanelBuilder(){{
										height("90%");
										width("99%");
										childLayoutHorizontal();
										
										panel(new PanelBuilder("healthbar"){{
											backgroundColor("#f00f");
											width("25%");
										}});
									}});
								}});
								
								panel(new PanelBuilder("oxygenbarframe"){{
							
									backgroundColor("#ffff");
									childLayoutCenter();
									
									panel(new PanelBuilder(){{
										height("90%");
										width("99%");
										childLayoutHorizontal();
										
										panel(new PanelBuilder("apneabar"){{
											backgroundColor("#0f0f");
											width("100%");
											childLayoutHorizontal();
										
											panel(new PanelBuilder("oxygenbar"){{
												backgroundColor("#00ff");
												width("25%");
											}});
										}});
									}});
								}});
							}});
						}});
						
						panel(new PanelBuilder());
						
					}});
				}});
				
				panel( new PanelBuilder(){{
					width("25%");
				}});
    	   					
			}});
    	   				
		}}.build(nifty);
	}	
}