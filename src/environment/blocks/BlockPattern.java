package environment.blocks;

import environment.Map;

public interface BlockPattern {
	/**
	 * Place the pattern on the Map at the i j block.
	 * @param i
	 * @param j
	 */
	public abstract void place(Map map, int i, int j);
}
