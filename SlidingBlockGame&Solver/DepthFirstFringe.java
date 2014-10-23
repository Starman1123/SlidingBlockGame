import java.util.Stack;


public class DepthFirstFringe extends Stack<FringeObject> implements
		FringeOptions {

	@Override
	public void put(FringeObject e) {
		push(e);
	}
	@Override
	public FringeObject take() {
		return pop();
	}
}