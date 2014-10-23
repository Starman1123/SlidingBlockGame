//class FringeObject 
import java.util.Stack;

public class FringeObject {
	public final Tray current;
	private final FringeObject parent;
	public final Location prevBlockLocation;
	public final Location newBlockLocation;
	public FringeObject(Tray current, FringeObject parent,
			Location oldBlockPosition, Location newBlockPosition) {
		this.current = current;
		this.parent = parent;
		this.prevBlockLocation = oldBlockPosition;
		this.newBlockLocation = newBlockPosition;
	}
	// print the moves before the current FringeObject
	public void printMoves() {
		printMovesHelper(this);
		while (!outputStack.isEmpty()) {
			System.out.println(outputStack.pop());
		}
	}

	private static final Stack<String> outputStack = new Stack<String>();
	//printmoves helper function
	private static void printMovesHelper(FringeObject object) {
		while (object != null) {
			if (object.prevBlockLocation != null && object.newBlockLocation != null) {
				outputStack.push(object.prevBlockLocation.toString() + " "
						+ object.newBlockLocation.toString());
			}
			object = object.parent;
		}
	}
}