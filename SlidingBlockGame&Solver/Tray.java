import java.util.*;

public class Tray implements Cloneable {
	//number of rows in the tray
	private final short rows;
	//number of cols in the tray
	private final short cols;
	//A list of blocks that are in the tray
	private final List<Block> blocks;
	//number of blocks
	private final int numBlocks;
	//number of blank spaces
	private final int numBlanks;
	//a set of blank spaces
	private final Set<Location> blankLocations;
	//Debugging option
	private boolean iAmDebugging = true;
	
	public short getRows() {
		return rows;
	}
	public short getCols() {
		return cols;
	}
	public List<Block> getBlocks() {
		return blocks;
	}
	public int numBlocks() {
		return numBlocks;
	}
	public int numBlanks() {
		return numBlanks;
	}
	public Iterator<Location> blanksIterator() {
		return blankLocations.iterator();
	}
	//Tray constructor
	public Tray(short rows, short cols, Collection<Block> blocks) {
		if (rows<=0 || cols<=0) {
			throw new IllegalArgumentException("number of rows and cols cannot be negative.");
		}
		if (blocks == null) {
			throw new IllegalArgumentException("No blocks");
		}
		this.rows = rows;
		this.cols = cols;
		this.numBlocks = blocks.size();
		this.blocks = new ArrayList<Block>();
		this.blankLocations = new HashSet<Location>();
		for (int i=0; i<rows;i++) {
			Location small = Location.getInst(i, 0);
			Location big = Location.getInst(i, cols - 1);
			clearPos(small, big);
		}
		//compute the number of Locations occupied by the blocks
		int num = 0;
		for (Block b : blocks) {
			this.blocks.add(b);
			num += b.getLength() * b.getWidth();
			markPos(b.getTopLeft(),b.getBotRight());
		}
		this.numBlanks = rows*cols - num;
		if (iAmDebugging) {
			isOK();
		}
	}
	//Another constructor for Tray so that it can take in integers
	public Tray(int rows, int cols, Collection<Block> blocks) {
		this((short) rows, (short) cols, blocks);
	}
	public void move(int index, String direction) {
		Block prevBlock = blocks.get(index);
		Location l1 = prevBlock.getTopLeft();
		Location l2 = prevBlock.getTopRight();
		Location l3 = prevBlock.getBotLeft();
		Location l4 = prevBlock.getBotRight();
		if (!canMoveToLocation(index, l1.move(direction))){
			throw new IllegalArgumentException();
		}
		
		if (direction == "up") {
			prevBlock = blocks.get(index);
			clearPos(l3,l4);
		}
		if (direction == "down") {
			prevBlock = blocks.get(index);
			clearPos(l1, l2);
		}
		if (direction == "left") {
			prevBlock = blocks.get(index);
			clearPos(l2, l4);
		}
		if (direction == "right") {
			prevBlock = blocks.get(index);
			clearPos(l1, l3);
		}
		
		Block newBlock = Block.getInst(l1.move(direction), prevBlock.getLength(), prevBlock.getWidth());
		blocks.set(index, newBlock);
		
		Location newl1 = newBlock.getTopLeft();
		Location newl2 = newBlock.getTopRight();
		Location newl3 = newBlock.getBotLeft();
		Location newl4 = newBlock.getBotRight();
		if (direction == "up") {
			markPos(newl1,newl2);
		}
		if (direction == "down") {
			markPos(newl3, newl4);
		}
		if (direction == "left") {
			markPos(newl1, newl3);
		}
		if (direction == "right") {
			markPos(newl2, newl4);
		}
		if (iAmDebugging) {
			isOK();
		}
		
	}
	@Override
	public String toString() {
		String result = rows + " " + cols + "\n";
		for (Block b : blocks) {
			result += b.toString() + "\n";
		}
		return result;
	}
	private void markPos(Location topLeft, Location botRight){
		for (int r = topLeft.getRow(); r<=botRight.getRow();r++) {
			for (int c = topLeft.getCol(); c <= botRight.getCol();c++){
				Location curr = Location.getInst(r, c);
				blankLocations.remove(curr);
			}
		}
	}
	private void clearPos(Location topLeft, Location botRight) {
		for (int r = topLeft.getRow(); r<=botRight.getRow();r++) {
			for (int c = topLeft.getCol(); c <= botRight.getCol();c++){
				Location curr = Location.getInst(r, c);
				blankLocations.add(curr);
			}
		}
		
	}
	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + cols;
		result = 31 * result + rows;
		Set<Block> set = new HashSet<Block>(blocks);
		result = 31 * result + set.hashCode();
		return result;
	}
	@Override
	public boolean equals(Object obj){
		if (this == obj) return true;
		if (obj == null || this == null) return false;
		if (this.getClass() != obj.getClass()) return false;
		Tray tray = (Tray) obj;
		if (rows != tray.getRows() || cols!=tray.getCols()) return false;
		if (this.getBlocks().size() != tray.getBlocks().size()) return false;
		Set<Block> set1 = new HashSet<Block>(blocks);
		Set<Block> set2 = new HashSet<Block>(tray.getBlocks());
		if (!set1.equals(set2)) return false;
		else return true;
	}
	public void isOK() throws IllegalStateException{
		
	}
	public Tray copy() {
		return new Tray(rows, cols, blocks);
	}
	//check if a given location is valid
	private boolean isValidLocation(Location l) {
		if (l == null) return false;
		if (l.getRow()<0 || l.getRow()>=rows || l.getCol()<0 || l.getCol()>=cols) {
			return false;
		}
		else return true;
	}
	//check if a given block is valid
	private boolean isValidBlock(Block b) {
		if (b == null || !isValidLocation(b.getTopLeft()) || !isValidLocation(b.getBotRight())){
			return false;
		}
		for (Block bb : blocks) {
			if (bb != null && b.ifOverlap(bb)) {
				return false;
			}
		}
		return true;
	}
	
	
	public boolean canMoveToLocation(int index, Location l) {
		if (l == null) {
			throw new NullPointerException();
		}
		Block prevBlock = blocks.get(index);
		blocks.set(index, null);
		Block block = Block.getInst(l, prevBlock.getLength(), prevBlock.getWidth());
		boolean result;
		result = isValidBlock(block);
		blocks.set(index, prevBlock);
		
		return result;
	}
	public boolean containsBlock(Block b){
		return blocks.contains(b);
	}
	
	public boolean canMove(int index, String direction) {
		try {
			return canMoveToLocation(index, blocks.get(index).getTopLeft().move(direction));
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
	public int findBlockContaining(Location p) {
		for (int i = 0; i < blocks.size(); i++) {
			Block b = blocks.get(i);
			if (b.containsLocation(p)) {
				return i;
			}
		}
		return -1;
	}
	public void printTray() {
		for (Block b : blocks) {
			b.printBlock();
		}
		System.out.println("---------");
	}
}