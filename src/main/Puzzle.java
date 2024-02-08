package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Only one puzzle at a time i.e., 3x3 or 5x5 etc
 */
public class Puzzle {
	
	public String type; // also the name in this case
	public ArrayList<Integer> values;
	public ArrayList<Integer> solution;
	
	private static final String MOVEMENT_RIGHT = "right";
	private static final String MOVEMENT_LEFT = "left";
	private static final String MOVEMENT_UP = "up";
	private static final String MOVEMENT_DOWN = "down";
	
	private static final String TYPE_3x3 = "3x3";
	private static final String TYPE_4x4 = "4x4";
	private static final String TYPE_5x5 = "5x5";
	
	private static final int WALL_LEFT = 1;
	private static final int WALL_RIGHT = 0;

	public Puzzle(String type, ArrayList<Integer> values) {
		this.type = type; // 3x3,4x4,5x5
		this.values = values;
		solution = getSolution(values);
	}
	
	public Boolean isSolved() {
		if (values.equals(solution)) {
			return true;
		}
		return false;
	}

	/**
	 * WOMP WOMP simple solve method to solve the puzzle and easily print out the steps, and test the logic
	 */
	public void autoSolve() {
		Random random = new Random();
		Boolean isSolved = false;

		while (!isSolved) {
			HashMap<String, Integer> valid_options = determineOptions();
			List<String> keys = new ArrayList<>(valid_options.keySet());
			String randomKey = keys.get(random.nextInt(keys.size()));
			this.values = sw(valid_options.get(randomKey));
			isSolved = isSolved();
			print();
		}
		System.out.println("Puzzle Solved!");
	}
	
	/**
	 * Basically uses simple java methods to put the array list in order for comparison later
	 * 
	 * @return
	 */
	private ArrayList<Integer> getSolution(ArrayList<Integer> tosort) {
		ArrayList<Integer> solution = new ArrayList<>(tosort);
		solution.removeAll(Collections.singleton(0)); // remove instances of 0 to add at the end
		Collections.sort(solution);
		solution.add(0);
		return solution;
	}
	
	/**
	 * Takes in an input: determines what piece to move into the empty slot
	 * INPUT OPTIONS SHOULD BE right, left, up, down
	 */
	public void solve() {

		Scanner scanner = new Scanner(System.in);
		
		Boolean isSolved = false;
		
		while (!isSolved) {

			// These will be the values in the array list which touch the empty square when formatted
			HashMap<String, Integer> valid_options = determineOptions();
			
			// .. (solving logic)
			// ask for input (print out a line that displays the valid options
			System.out.println("Valid Movement Options: ");
			for (String key : valid_options.keySet()) {
				System.out.println("| " + key);
			}
			String selection = scanner.nextLine();
			this.values = sw(valid_options.get(selection));
			
			// cleanup logic
			isSolved = isSolved();
			print();
		}
		System.out.println("Puzzle Solved!");
		return;
	}
	
	private HashMap<String, Integer> determineOptions() { // i.e., up, down, right, left and the integer will the position
		// need to add consideration for rows: the code needs to know boundaries based on its position
		/**
		 * The walls could be determined by like the (value % {table_size x})
		 * 
		 * for example 1 % 3 = 1; 4 % 3 = 1 && 3 % 3 = 0, 6 % 3 = 0
		 * the walls are found from ((index+1) % {table_size x}), left_wall_value = 1, right_wall_value = 0, every other value would be in the middle somewhere
		 */
		int emptyPos = values.indexOf(0);
		
		HashMap<String, Integer> options = new HashMap<>();
		// todo: try switching + and - to see if it works
		options.put(MOVEMENT_LEFT, emptyPos - 1);
		options.put(MOVEMENT_RIGHT, emptyPos + 1);
		switch(this.type) {
		case TYPE_3x3:
			options.put(MOVEMENT_DOWN, emptyPos + 3);
			options.put(MOVEMENT_UP, emptyPos - 3);
			break;
		case TYPE_4x4:
			options.put(MOVEMENT_DOWN, emptyPos + 4);
			options.put(MOVEMENT_UP, emptyPos - 4);
			break;
		case TYPE_5x5:
			options.put(MOVEMENT_DOWN, emptyPos + 5);
			options.put(MOVEMENT_UP, emptyPos - 5);
			break;
		}

		emptyPos = emptyPos + 1;
		
		double row;
		switch(this.type) {
		
		case TYPE_3x3:
			System.out.println("Empty Pos: " + emptyPos + " Wall Value" + (emptyPos % 3) + " Row Value" + (emptyPos / 3));
			switch((emptyPos % 3)) {
			case WALL_RIGHT:
				options.remove(MOVEMENT_RIGHT);
				break;
			case WALL_LEFT:
				options.remove(MOVEMENT_LEFT);
				break;
			}
			row = ( (double) emptyPos / 3.0);

			// .. basically if ((emptyPos / 3)) < 1 then remove(MOVEMENT_UP) OR (emptyPos / 3) > 2 then remove (MOVEMENT_DOWN)
			if (row <= 1) {
				options.remove(MOVEMENT_UP);
				break;
			}
			if (row > 2) {
				options.remove(MOVEMENT_DOWN);
				break;
			}
			break;
		case TYPE_4x4:
			switch((emptyPos % 4)) {
			case WALL_RIGHT:
				options.remove(MOVEMENT_RIGHT);
				break;
			case WALL_LEFT:
				options.remove(MOVEMENT_LEFT);
				break;
			}
			row = ( (double) emptyPos / 4.0);
			
			if (row <= 1) {
				options.remove(MOVEMENT_UP);
				break;
			}
			if (row > 3) {
				options.remove(MOVEMENT_DOWN);
				break;
			}
			break;
		case TYPE_5x5:
			switch((emptyPos % 5)) {
			case WALL_RIGHT:
				options.remove(MOVEMENT_RIGHT);
				break;
			case WALL_LEFT:
				options.remove(MOVEMENT_LEFT);
				break;
			}
			row = ( (double) emptyPos / 5.0);
			
			if (row <= 1) {
				options.remove(MOVEMENT_UP);
				break;
			}
			if (row > 4) {
				options.remove(MOVEMENT_DOWN);
				break;
			}
		}
		
		return options;
	}
	
	/**
	 * Based on the movement determine the index of the value to switch to the empty square
	 * Movement is in relation to the emptyPos (i.e., right means moving the object to the right left into the current Position)
	 * 
	 * @param val1
	 * @param val2
	 * @return
	 */
	private ArrayList<Integer> sw(int switchPos) {
		
		// TODO: calculate the positions
		// [spoiler: yeah it did, fix this, its just switching] the logic of determining the actually position may be moved to determine options

		ArrayList<Integer> new_values = new ArrayList<>(values);
		
		int emptyPos = new_values.indexOf(0);
		System.out.println("Switching Position of 0: " + emptyPos + " Position of switch: " + switchPos);
		
		int switchValue = new_values.get(switchPos);
		// switching logic
		new_values.set(emptyPos, switchValue);
		new_values.set(switchPos, 0);
		
		return new_values;
	}
	
	/**
	 * Format the table for printing purposes
	 */
	public void print() {
		StringBuilder sb = new StringBuilder();

        int n = (int) Math.sqrt(values.size());
        String border = "+---".repeat(n) + "+\n";
        sb.append(border);
        for (int i = 0; i < values.size(); i++){
            sb.append("| ");
            if (values.get(i) == 0){
                sb.append("  ");
            } else {
                sb.append(String.format("%2d", values.get(i)));
            }
            if (i % n == n - 1){
                sb.append("|\n");
                sb.append(border);
            }
        }

		System.out.println(sb.toString());
	}
	

}
