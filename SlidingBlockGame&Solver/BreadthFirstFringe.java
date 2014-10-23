import java.util.LinkedList;
public class BreadthFirstFringe extends LinkedList<FringeObject> implements
		FringeOptions {

	@Override
	public FringeObject take() {
		return remove();
	}
	@Override
	public void put(FringeObject e) {
		add(e);
	}


}