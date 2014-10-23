//using FringeOptions interface is easier to switch between multiple fringes
public interface FringeOptions {

	public void put(FringeObject e);

	
	public FringeObject take();

	
	public boolean isEmpty();

	
}