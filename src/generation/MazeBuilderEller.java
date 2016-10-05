package generation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Random;

public class MazeBuilderEller extends MazeBuilder implements Runnable {

	/**
	 * Build a new maze randomly using Eller's algorithm for Maze generation
	 */
	public MazeBuilderEller() {
		super();
		System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze.");
	}
	
	
	/**
	 * Build a new maze deterministically using Eller's algorithm for Maze generation
	 * @param det if this argument is `true`, the maze will be built deterministically
	 */
	public MazeBuilderEller(boolean det) {
		super(det);
		System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze.");
	}
	
	
	//Initialize a 2-D array to keep track of set numbers for each cell in cells.
	private int[][] sets; 
	
	//Initialize an integer that will keep track of the next unused set number
	private int setCount = 1; 
	
	/**
	 * This method generates pathways into the maze by using Eller's algorithm to build the maze row by row, 
	 * using sets to keep track of which columns are ultimately connected. We created three main helper
	 * methods chooseSets(int row), fillInSets(int row), and lastRowChooseSets().
	 * 
	 */
	@Override		
	protected void generatePathways() {
		sets = new int[width][height];
		fillArrayWithZeros(sets); 
		//fill in the first row of sets with numbers 1 to width-1
		for (int i = 0; i < width; i++){
			sets[i][0] = setCount;
			setCount++; 
		}		 
		//delete random adjacent walls in first row
		chooseSets(0); 
		
		//displayArray(sets); /**used for debugging purposes**/
			
		
		//for each row other than the last one, determine random vertical connections
		//making sure we have 1 connection per set  
		for(int row = 0; row < height - 1; row++){
			//Make a hashtable per row, for the set values in each row.
			//Each key represents a different value of a set number 
			//Each key's value is an ArrayList of Integers, where these Integers are the values 
			//of the columns that have that particular key as the value of the set
			Hashtable<Integer, ArrayList<Integer>> hash = new Hashtable<Integer, ArrayList<Integer>>(); 
			for(int col = 0; col < width; col++){
				int current = sets[col][row]; 
				//Check whether the hash table already has a key with a value of current.
				//If it does, then add the column to the ArrayList that belongs to that key, 
				//and update the ArrayList as the key's new value. 
				if(hash.containsKey(current)){
					hash.get(current).add(col);
					hash.put(current, hash.get(current)); 
				}
				else{
					ArrayList<Integer> columnlist = new ArrayList<Integer>();
					columnlist.add(col); 
					hash.put(current, columnlist); 
				}
			}
			//Create a collection of the keys so that we may shuffle them	
			Collection<Integer> keys = hash.keySet();
			
			for(Integer key: keys){
				Collections.shuffle(hash.get(key)); 
				
		        //Choosing how many columns in the list to extend downwards
		        Random rand = new Random(); 
		        int numcols = rand.nextInt(hash.get(key).size()) + 1;
		        
		        //for each column value, create a new wall to the south of the cell and delete that wall
		        for(int j = 0; j < numcols; j++){
		        	int colnum = hash.get(key).get(j); 
		        	sets[colnum][row + 1] = key; 
		        	Wall wall = new Wall(colnum,row,CardinalDirection.South);
					cells.deleteWall(wall);	
		        }
		    }
			
		    fillInSets(row+1);
		    chooseSets(row+1); 
		}		
		lastRowChooseSets(); 
	}
	
	
	//Method used to choose sets within a row 
	private void chooseSets(int row){
		for(int col = 0; col < width - 1; col++){
			//Check whether adjacent cells have the same set number
			if(sets[col][row] != sets[col + 1][row]){
				//Math.random() generates a double in the range [0,1)
				double remove = Math.random();
				if(remove >= 0.5){
					//delete that wall in cells and change the sets[][] values
					Wall wall = new Wall(col,row,CardinalDirection.East);
					cells.deleteWall(wall);
					sets[col + 1][row] = sets[col][row]; 
				}	
			}
		}
	}
	
	
	//Method to assign a set number to cells not yet designated a set number after vertical connections are made
	private void fillInSets(int row){
		for(int col = 0; col < width - 1; col++){
			if(sets[col][row] == 0){
				sets[col][row] = setCount; 
				setCount++; 
			}
		}
	}
		
	
	//Method used to choose sets for the last row, breaks down all walls
	private void lastRowChooseSets(){
		for(int col = 0; col < width - 1; col++){
			//Check whether adjacent cells have the same set number
			if(sets[col][height - 1] != sets[col + 1][height - 1]){
				//delete that wall in cells and change the sets[][] values
				Wall wall = new Wall(col,height - 1,CardinalDirection.East);
				cells.deleteWall(wall);
				sets[col + 1][height - 1] = sets[col][height - 1];  
			}
		}
	}

	
	//Initially filling the sets 2-D array with 0's
	//A value of 0 at any point in the array means that it hasn't been assigned a set number
	private void fillArrayWithZeros(int[][] set){
		for(int row = 0; row < height; row++){
			for(int col = 0; col < width; col++){
				set[col][row] = 0; 
			}
		}
	}
	
	
	//Helper method used to display sets method for debugging purposes  
	private void displayArray(int[][] arr) {
		for(int row = 0; row < height; row++){
			for(int col = 0; col < width; col++){
				System.out.print(arr[col][row] + " "); 
			}
			System.out.println();
		}
		System.out.println();
	}
	

	// Get sets
	public int[][] getSets(){
		return sets;
	}

	
	// Get cells
	public Cells getCells(){
		return cells;
	}
	
	
}