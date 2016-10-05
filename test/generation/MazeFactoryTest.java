package generation;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import generation.Factory;
import generation.Order;

public class MazeFactoryTest {
	private MazeContainer maze;  // setup makes this a width x height cells object
	private StubOrder stub; // setup makes this a 1x1 cells object 
	@Before
	public void setUp() throws Exception {

		//create MazeFactory object 
		MazeFactory mazeFactory = new MazeFactory(true);

		//create a Stub object, can use any algorithm
		StubOrder stub = new StubOrder(0, Order.Builder.Prim); 

		//call order using Stub
		mazeFactory.order(stub); 
		mazeFactory.waitTillDelivered(); 
		maze = (MazeContainer)stub.getMaze();

	}


	/**
	 * Test case: Check existence of an exit in maze
	 * <p>
	 * Method under test: Distance(int[][] input), getExitPosition()
	 * <p>
	 * Correct behavior:
	 * It is correct if precondition of computeDistances() works and getExitPosition() 
	 * return an int[] exit position somewhere on the border.
	 */
	@Test
	public void exitExists() {
		int[] exit = null;
		Distance dist = maze.getMazedists(); 
		int[][] distances = dist.getDists(); 
		if (distances != null) {
			// confirms that exit exits
			exit = dist.getExitPosition();
		}
		assertNotNull("Maze has no exit", exit);

	}
	
	
	/**
	 * Test case: Check that every cell has a solution (distance to exit)
	 * <p>
	 * Method under test: getDistanceToExit(int x, int y)
	 * <p>
	 * Correct behavior:
	 * It is correct if the value returned by getDistanceToExit for each (x,y) coordinate
	 * is not infinity.
	 */
	@Test
	public void distToExit() {
		Distance dist = maze.getMazedists(); 
		int[][] distances = dist.getDists();
		for(int row = 0; row < distances.length; row++){
			for(int col = 0; col < distances[0].length; col++){
				assertTrue(maze.getDistanceToExit(row, col) != Integer.MAX_VALUE);
			}
		}	
	}
	
	
	/**
	 * Test case: Check whether generated maze has rooms
	 * <p>
	 * Method under test: isInRoom(int x, int y)
	 * <p>
	 * Correct behavior:
	 * It is correct if at any point when looping through each cell in the maze the
	 * method isInRoom returns true.
	 */	
	@Test
	public void roomsExist() {
		Cells cells = maze.getMazecells();
		Distance dist = maze.getMazedists(); 
		int[][] distances = dist.getDists();
		int row = 0;
		int col = 0;
		Boolean bool = new Boolean(false);
		while(row < distances.length && bool == false) {
			while(col < distances[0].length && bool == false) {
				if(cells.isInRoom(row, col)){
					bool = true;
				}
				col = col + 1;
			}
			row = row + 1;
		}
		assertTrue("Maze has no rooms", bool = true);
	}
	
	
	/**
	 * Test case: Check correctness of the getStartingPosition method
	 * <p>
	 * Method under test: getStartingPosition()
	 * <p>
	 * Correct behavior:
	 * It is correct if the distance from the starting position is equal to
	 * the maximum distance on the maze.
	 */
	@Test
	public void startingPointTest() {
		Distance dist = maze.getMazedists();
		int maxDist = dist.getMaxDistance();
		int[] start = maze.getStartingPosition();
		int startDist = maze.getDistanceToExit(start[0], start[1]);
		assertTrue("Distance from start to exit position is not equal to max distance", maxDist == startDist);
	}
	

	/**
	 * Test case: Correctness of getNeighborCloserToExit method
	 * <p>
	 * Method under test: getNeighbotCloserToExit(int x, int y)
	 * <p>
	 * Correct behavior:
	 * It is correct if the distance to the exit of returned coordinates is less than the
	 * distance to exit of the given coordinates.
	 */
	@Test
	public void neighborCloserToExitTest() {
		int width = maze.getWidth();
		int height = maze.getHeight();
		int initialDist = maze.getDistanceToExit(width/2, height/2);
		int[] neighbor = maze.getNeighborCloserToExit(width/2, height/2);
		int neighborDist = maze.getDistanceToExit(neighbor[0], neighbor[1]);
		assertTrue("Neighbor is not closer to exit", neighborDist < initialDist);
	}

}
