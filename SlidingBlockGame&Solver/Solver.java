import java.io.*;
import java.util.*;

public class Solver {
	
	private static final Set<Tray> traySet = new HashSet<Tray>();
	private static final boolean iAmDebugging = true;
	private static String[] directions = {"right", "down", "left", "up"};
	

	public static void main(String[] args) {
		long start = System.nanoTime();
		if (args.length < 2 || args.length > 3) {
			printHelp();
			System.exit(1);
		}
		if (args.length == 2) {
			try {
				BufferedReader inputReader = new BufferedReader(new FileReader(args[0]));
				BufferedReader outputReader = new BufferedReader(new FileReader(args[1]));

				//set up the tray
				Scanner firstLine = new Scanner(inputReader.readLine());
				short rowSize = firstLine.nextShort();
				short colSize = firstLine.nextShort();
				//finish setting up the size of the tray
				firstLine.close();
				//initialize the tray
				Location.initGrid(rowSize+1, colSize+1);
				List<Block> inputBlocks = new ArrayList<Block>();
				String curr;
				while ((curr = inputReader.readLine()) != null) {
					inputBlocks.add(grabBlock(curr));
				}
				Tray initialTray = new Tray(rowSize, colSize, inputBlocks);
				inputReader.close();

			
				List<Block> desiredBlocks = new ArrayList<Block>();
				while ((curr = outputReader.readLine()) != null) {
					desiredBlocks.add(grabBlock(curr));
				}
				outputReader.close();

				// solve the puzzle
				if (!solve(initialTray, desiredBlocks)) {
					debugPrintln("Finished in: " + (System.nanoTime() - start) / 100000000.0 + " seconds");
					debugPrintln("Cannot be solved!");
					System.exit(1);
				} else {
					debugPrintln("total states: " + traySet.size());
					debugPrintln("Finished in: " + (System.nanoTime() - start) / 100000000.0 + " seconds");
					return;
				}
			} catch (IOException e) {
				System.out.println("Could not open file!");
			}
		} else if (args.length == 3) {
			System.out.println("Too many input files!"); 
		}
	}
	
	private static boolean isGoal(Tray tray, Collection<Block> goalBlocks) {
		for (Block b : goalBlocks) {
			if (!tray.containsBlock(b)) {
				return false;
			}
		}
		return true;
	}
	private static void debugPrintln(String string) {
		if (iAmDebugging) {
			System.out.println(string);
		}
	}
	private static boolean solve(Tray initialTray,
			Collection<Block> desiredBlocks) {

		FringeOptions fringe = null;
		// BFS if Tray is too big for DFS
		if (initialTray.getRows() > 10 && initialTray.getCols() > 10) {
			fringe = new BreadthFirstFringe();
		} else {
			fringe = new DepthFirstFringe();
		}

		fringe.put(new FringeObject(initialTray, null, null, null));

		while (!fringe.isEmpty()) {
			FringeObject curElem = fringe.take();
			Tray current = curElem.current;
//			current.printTray();

			if (isGoal(current, desiredBlocks)) {
				curElem.printMoves();
				return true;
			}

			if (traySet.contains(current)) {
				continue;
			}
			traySet.add(current);
			//if block number is smaller than blank number
			if (current.numBlocks() < current.numBlanks()) { 
				for (int i = 0; i < current.numBlocks(); i++) {
					for (String d : directions) {
						if (current.canMove(i, d)) {
							Location oldBlockPosition = current.getBlocks().get(i).getTopLeft();
							current.move(i, d);
							Location newBlockPosition = current.getBlocks().get(i).getTopLeft();
							fringe.put(new FringeObject(current.copy(), curElem, oldBlockPosition, newBlockPosition));
							if (d == "right") {
								current.move(i, "left");
							}
							else if (d == "down") {
								current.move(i, "up");
							}
							else if (d == "left") {
								current.move(i, "right");
							}
							else if (d == "up") {
								current.move(i, "down");
							}
							else {
								debugPrintln("Weird direction");
							}
						}
					}
				}
				// if there are more blanks
			} else { 
				Iterator<Location> blanksItr = current.blanksIterator();
				while (blanksItr.hasNext()) {
					Location curBlank = blanksItr.next();
					for (String d : directions) {
						int i = -1;
						try {
							if (d == "right") {
								i = current.findBlockContaining(curBlank.move("left"));
							}
							else if (d == "down") {
								i = current.findBlockContaining(curBlank.move("up"));
							}
							else if (d == "left") {
								i = current.findBlockContaining(curBlank.move("right"));
							}
							else if (d == "up") {
								i = current.findBlockContaining(curBlank.move("down"));
							}
							
						} catch (IndexOutOfBoundsException e) { 
							continue;
						}
						if (i != -1 && current.canMove(i, d)) {
							Location oldBlockPosition = current.getBlocks().get(i).getTopLeft();
							Tray nextConfig = current.copy();
							nextConfig.move(i, d);
							Location newBlockPosition = nextConfig.getBlocks().get(i).getTopLeft();
							fringe.put(new FringeObject(nextConfig, curElem, oldBlockPosition, newBlockPosition));
						}
					}
				}
			}
		}
		return false;
	}
	//get the blocks from input file
	private static Block grabBlock(String line) {
		Scanner scanner = new Scanner(line);
		short row1 = scanner.nextShort();
		short col1 = scanner.nextShort();
		short row2 = scanner.nextShort();
		short col2 = scanner.nextShort();
		scanner.close();
		return Block.getInst(row1, col1, row2, col2);
	}
	private static void printHelp() {
		System.out.println();
		System.out.println("Syntax: java Solver [-oinfo] initialConfigFile goalConfigFile");
		System.out.println("\t- initialConfigFile: the file containing the initial state");
		System.out.println("\t- goalConfigFile: the file containing the goal state");
		System.out.println("Option: [-oinfo]");
		System.out.println();
	}
}
