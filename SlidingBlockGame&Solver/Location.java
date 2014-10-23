/*
 * the immutable Location object is used to construct a Block object
 */
public class Location implements Comparable<Location> {
	//Location[][] to store the current tray info
	//see if the current location is occupied or not
	private static Location[][] grid = null;
	private final short rowNum;
	private final short colNum;
	private Location(short rowNum, short colNum) {
		if (rowNum<0 || colNum<0) {
			throw new IndexOutOfBoundsException();
		}
		this.rowNum = rowNum;
		this.colNum = colNum;
	}
	//getters
	public short getRow(){
		return rowNum;
	}
	public short getCol(){
		return colNum;
	}
	public static Location getInst(int rowNum, int colNum) {
		if (grid[rowNum][colNum]==null) {
			grid[rowNum][colNum] = new Location((short) rowNum, (short) colNum);
		}
		return grid[rowNum][colNum];
	}
	//initialize the grid
	public static void initGrid(int rows, int cols) {
		if(grid!=null && (rows<grid.length || cols<grid[0].length)){
			throw new IllegalArgumentException();
		}
		Location[][] temp = new Location[rows][cols];
		//if this is an old grid
		if (grid!=null){
			for (int r=0; r< grid.length;r++) {
				for (int c=0;c<grid[0].length;c++) {
					temp[r][c] = grid[r][c];
				}
			}
			grid = temp;
		}
		//if this is a new grid
		else {
			grid = temp;
		}
	}
	public boolean isUpLeftTo(Location b) {
		boolean result = this.rowNum<=b.getRow() && this.colNum<=b.getCol();
		return result;
	}
	public boolean isDownRightTo(Location b) {
		boolean result = this.rowNum>=b.getRow() && this.colNum>=b.getCol();
		return result;
	}
	//move the Location upward
	public Location up() {
		return Location.getInst(rowNum-1, colNum);
	}
	public Location down() {
		return Location.getInst(rowNum+1, colNum);
	}
	public Location left() {
		return Location.getInst(rowNum, colNum-1);
	}
	public Location right() {
		return Location.getInst(rowNum, colNum+1);
	}
	public Location move(String direction){
		if (direction == null) {
			throw new NullPointerException();
		}
		if (direction.equals("right")){
			return right();
		}
		else if (direction.equals("down")){
			return down();
		}
		else if (direction.equals("left")){
			return left();
		}
		else if (direction.equals("up")){
			return up();
		}
		else throw new IllegalArgumentException("Wrong Direction!");
	}
	@Override
	public int hashCode() {
		return 257*rowNum + colNum;
	}
	@Override
	public String toString() {
		return rowNum+" "+colNum;
	}
	@Override
	public int compareTo(Location b){
		if (this.rowNum<b.getRow()){
			return -1;
		}
		else if (this.rowNum>b.getRow()){
			return 1;
		}
		else if (this.colNum>b.getCol()){
			return 1;
		}
		else if (this.colNum<b.getCol()){
			return -1;
		}
		else return 0;
	}
	public void printLocation() {
		System.out.print(rowNum+" "+colNum+" ");
	}
	@Override
	public boolean equals (Object obj){
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj.getClass() != this.getClass()){
			return false;
		}
		Location b = (Location) obj;
		if (rowNum!=b.getRow() || colNum!=b.getCol()) {
			return false;
		}
		return true;
	}
}
