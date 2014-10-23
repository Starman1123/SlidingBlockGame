
import java.util.*;

public class Block implements Comparable<Block> {
	private final Location topLeft;
	private final short length;
	private final short width;
	public static Map<Integer, Block> myGrid = new HashMap<Integer, Block>();
	//Block constructor that takes in a Location object and length and width of the Block
	private Block(Location topLeft, short length, short width) {
		if (length<=0 || width<=0){
			throw new IllegalArgumentException();
		}
		else if (topLeft==null) {
			throw new NullPointerException();
		}
		this.topLeft = topLeft;
		this.length = length;
		this.width = width;
	}
	//print the block
	public void printBlock() {
		topLeft.printLocation();
		this.getBotRight().printLocation();
		System.out.println("");
	}
	//return an Instance of the Class Block
	public static Block getInst(int row1, int col1, int row2, int col2) {
		Location topLeft = Location.getInst(row1, col1);
		int length2 = row2 - row1 + 1;
		int width2 = col2 - col1 + 1;
		return getInst(topLeft, (short) length2, (short) width2);
	}
	public static Block getInst(Location topLeft,int length,int width) {
		int hashNum = getHash(topLeft.getRow(), topLeft.getCol(),(short)length,(short)width);
		if (myGrid.containsKey(hashNum)){
			//do nothing
		} else {
			Block b = new Block(topLeft, (short)length, (short)width);
			myGrid.put(hashNum, b);
		}
		return myGrid.get(hashNum);
	}
	//Checks if two Blocks overlap
	public boolean ifOverlap(Block b) {
		int thisR1 = this.getTopLeft().getRow();
		int thisR2 = this.getBotRight().getRow();
		int thisC1 = this.getTopLeft().getCol();
		int thisC2 = this.getBotRight().getCol();
		int R1 = b.getTopLeft().getRow();
		int R2 = b.getBotRight().getRow();
		int C1 = b.getTopLeft().getCol();
		int C2 = b.getBotRight().getCol();
		int maxR1 = Math.max(thisR1, R1);
		int minR2 = Math.min(thisR2, R2);
		int maxC1 = Math.max(thisC1, C1);
		int minC2 = Math.min(thisC2, C2);

		return minR2 >= maxR1 && minC2 >= maxC1;
	}
	public int compareTo(Block b) {
		if (b!=null) {
			return this.topLeft.compareTo(b.getTopLeft());
		}
		else throw new NullPointerException();
	}
	private static int getHash(short r, short c, short l, short w) {
		return (512*r-r*(r-1)/2)*131328+(512-r)*(512*c-c*(c-1)/2)+(w-1)*(512-r)+l-1;
	}
	public Location getTopLeft() {
		return topLeft;
	}
	public Location getBotRight() {
		return Location.getInst((short)(topLeft.getRow()+length-1)
								,(short)topLeft.getCol()+width-1);
	}
	public Location getTopRight() {
		return Location.getInst(topLeft.getRow(), topLeft.getCol() + width - 1);
		
	}
	public Location getBotLeft() {
		return Location.getInst(topLeft.getRow() + length - 1, topLeft.getCol());
	}
	public short getLength(){
		return length;
	}
	public short getWidth(){
		return width;
	}
	public String toString(){
		return getTopLeft().toString()+" ,"+getBotRight().toString();
	}
	public int hashCode(){
		return getHash(topLeft.getRow(), topLeft.getCol(), length, width);
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Block other = (Block) obj;
		if (length != other.getLength())
			return false;
		else if (width != other.getWidth())
			return false;
		else if (!topLeft.equals(other.topLeft))
			return false;
		else return true;
	}
	public boolean sameSize(Block b){
		if (b==null){
			throw new NullPointerException();
		}
		else return this.getLength()==b.getLength() && this.getWidth()==b.getWidth();
	}
	//checks if the current Block contains Location p
	public boolean containsLocation(Location p) {
		int r = p.getRow();
		int c = p.getCol();
		int offR = r - topLeft.getRow();
		int offC = c - topLeft.getCol();
		return 0 <= offR && offR < length && 0 <= offC && offC < width;
	}
}
