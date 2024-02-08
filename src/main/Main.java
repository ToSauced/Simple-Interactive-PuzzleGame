package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

// 3x3 test case for .txt: 1,2,3,4,5,6,7, ,8

public class Main {
	
    public static final String PROJECT_DIR = System.getProperty("user.dir") + File.separator;
	public static final String DATA_DIR = PROJECT_DIR + "data" + File.separator;
	
	public static HashMap<String, Puzzle> unsolved_puzzles = new HashMap<>();
	
	public static Puzzle current_puzzle;
	
	public static void main(String[] args) {
		menu();

		// test
		// ArrayList<Integer> values = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 0, 7, 8)); // easily solveable array to show the functionality
		// Puzzle puzzle = new Puzzle("3x3", values);
		// unsolved_puzzles.replace("3x3", puzzle);

		// init();
		// printUnsolved();

		// puzzle.values = values; // set the values to the test array

		// // Call the autoSolve method
		// puzzle.autoSolve(); // magicalllllllll

		// // logic to remove puzzle from unsolved_puzzles
		// unsolved_puzzles.remove(puzzle.type);

		// // print the unsolved puzzles after solution
		// printUnsolved();
	}
	
	public static void menu() {
		int iteration = 0;
		while (true) {
			if (iteration == 0) {
				init();
			}
			System.out.println("1. Exit");
			System.out.println("2. Print Unsolved Puzzles");
			System.out.println("3. Solve Puzzle");
			Scanner scanner = new Scanner(System.in);
			int selection = scanner.nextInt();
			switch (selection) {
			case 1: // DONE
                System.out.println("Exiting...");
                System.exit(0);
				break;
			case 2: // TODO: make sure this works
				printUnsolved();
				break;
			case 3: // TODO
				if (current_puzzle != null) {
					System.out.println("Current puzzle is already selected, do you wish to continue solving the selected puzzle? [y/n]");
					String response = scanner.nextLine();
					if (response.equals("n")) {
						puzzleSelector();
					}
					// TODO: no error handling because were wickkked
				} else {
					puzzleSelector();
				}
				current_puzzle.solve();
				unsolved_puzzles.remove(current_puzzle.type);
				current_puzzle = null;
				break;
			default:
				System.err.println("Invalid choice, please try another [1-3].");
				break;
			}
			iteration++;
				
			// ...
		}
	}
	
	/**
	 * Initalize the unsolved_puzzles array, create puzzle objects for each file
	 */
	private static void init() {
		
		HashMap<String, String> labels = new HashMap<>();
		labels.put("3x3", DATA_DIR.concat("3x3.txt"));
		labels.put("4x4", DATA_DIR.concat("4x4.txt"));
		labels.put("5x5", DATA_DIR.concat("5x5.txt"));
		
		Puzzle puzzle1 = new Puzzle("3x3", fileReader(labels.get("3x3")));
		Puzzle puzzle2 = new Puzzle("4x4", fileReader(labels.get("4x4")));
		Puzzle puzzle3 = new Puzzle("5x5", fileReader(labels.get("5x5")));
		
		unsolved_puzzles.put("3x3", puzzle1);
		unsolved_puzzles.put("4x4", puzzle2);
		unsolved_puzzles.put("5x5", puzzle3);

		puzzle1.print();
		puzzle2.print();
		puzzle3.print();

	}
	
	private static void printUnsolved() {
		System.out.println("Unsolved Puzzles:");
		for (String key : unsolved_puzzles.keySet()) {
			System.out.println(key);
		}
	}
	
	/**
	 * Read the file and return the array of values in the puzzle
	 * @param path 
	 * 
	 * @return
	 */
	private static ArrayList<Integer> fileReader(String path) {
		
		File file = new File(path);
		ArrayList<Integer> values = new ArrayList<>();
		
		if (!file.exists()) {
			// good luck with the loop if you get this
			System.err.println("File doesn't exist... going back to main menu");
			System.err.println(path);
			menu();
		}
		
        try{
            Scanner out = new Scanner(file);
            if (out.hasNextLine()){
                String line = out.nextLine();
                String[] puzzle = line.split(",");
                for (String s : puzzle){
                    try {
                        values.add(Integer.parseInt(s));
                    } catch (NumberFormatException e){
                        values.add(0); // 0 is placeholder value for spaces within the puzzle
                    }
                }
            }
            out.close();
            return values;
        }catch(FileNotFoundException e){ System.err.println("Error reading file: " + e.getMessage()); return values; }
	}
	
	/**
	 * Menu option: ask the user for which puzzle to start solving (first prompt in 3. Solve Puzzle) 
	 */
	public static void puzzleSelector() {
		// if current puzzle is already selected ask if they wish to continue solving the selected
		// ask for input
		String[] options = {"3x3","4x4","5x5"};
		System.out.println("Please select the Puzzle you wish to solve [3x3,4x4,5x5]");
		Scanner scanner = new Scanner(System.in);
		String selection = scanner.nextLine();
		for (String option : options) {
			if (option.equals(selection)) { current_puzzle = unsolved_puzzles.get(selection); }
		}
		
		return;
	}

}
