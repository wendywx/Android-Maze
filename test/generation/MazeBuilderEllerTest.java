package generation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import falstad.MazeController;

import generation.Factory;
import generation.Order;
import generation.MazeContainer;


public class MazeBuilderEllerTest{
	
	private Cells cells; 
	private int[][] sets; 

	@Before
	public void setUp() throws Exception {
		
		//Creating a MazeBuilderEller object so that we may access its cells and sets fields
		MazeBuilderEller mazeEller = new MazeBuilderEller(true);
		MazeController mc = new MazeController(Order.Builder.Eller);  
		//Building an order based using the MazeController as a parameter 
		mazeEller.buildOrder(mc);
		//Call MazeBuilderEller's generate pathways method
		mazeEller.generatePathways();
		//Assigning private variables sets and cells using MazeBuilderEller's get methods
		sets = mazeEller.getSets(); 
		cells = mazeEller.getCells(); 

	}
	//Testing if setUp is able to properly assign the sets field to 

	@Test
	//Tests whether we are able to successfully attain the sets field from the MazeBuilderEller class
	public void setsExists(){
		assertNotNull(sets); 
	}
	
	@Test
	//Tests whether we are able to successfully attain the cells field from the MazeBuilder/MazeBuilderEller class
	public void cellsExists(){
		assertNotNull(cells); 
	}
	
	@Test
	//Testing whether every cell represented in sets 2-D array is filled with a set number > 0 
	//This tests fillArray() and chooseSets()
	public void setNumbers(){
		Boolean bool = new Boolean(true); 
		for(int i = 0; i < sets.length; i++){
			for(int j = 0; j < sets[0].length; j++){
				if(sets[j][i] <= 0){
					bool = false; 
				}
			}
		}
		assertTrue(bool); 
	}
	
	@Test
	//Testing whether the set numbers in the sets array correspond the correct corresponding cells object
	//A correct mapping, when going from left to right in each row, 
	//would entail no wall in between set numbers that are the same, and a wall between set numbers that are different
	public void wallDeletion(){
		for(int i = 0; i < sets.length; i++){
			for(int j = 0; j < sets[0].length; j++){
				if(sets[j][i] == sets[j+1][i]){
					assertFalse(cells.hasWall(j, i, CardinalDirection.East)); 
				}
				else{
					assertTrue(cells.hasWall(j, i, CardinalDirection.East)); 
				
				}
				System.out.println(sets[j][i]);
			}
		}
		
	}
	
}
