import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

//SuppressWarnings("serial")
public class Cipher extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel root;
	private JTextField inputField;
	private JLabel resultLabel, instructions;
	private JButton operateBtn, switchBtn;
	private final int WIN_W = 800;
	private final int WIN_H = 200;
	private boolean mode = false; //false = encode, true = decode
	
	public Cipher() {
		setTitle("Playfair Cipher");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(WIN_W, WIN_H));
		setResizable(false);
		root = new JPanel();
		operateBtn = new JButton("Encrypt");
		switchBtn = new JButton("Switch to decrypt mode");
		instructions = new JLabel("Type the text you wish to encode below");
		resultLabel = new JLabel(" ");
		JPanel topPanel = new JPanel();
		JPanel btmPanel = new JPanel();
		inputField = new JTextField();
		operateBtn.addActionListener(new operateBtnListener());
		switchBtn.addActionListener(new switchBtnListener());
		inputField.setText("the secret is safe");
		root.setLayout(new GridLayout(2, 1));
		topPanel.setLayout(new GridLayout(2, 2));
		btmPanel.setLayout(new GridLayout(1, 1));
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		btmPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		topPanel.add(instructions);
		topPanel.add(operateBtn);
		topPanel.add(inputField);
		topPanel.add(switchBtn);
		btmPanel.add(resultLabel);
		root.add(topPanel);
		root.add(btmPanel);
		add(root);
		pack();
		setVisible(true);
	}
	
	private static int[][] setup() { //returns a multidimensional int array with the ASCII values of every cipher letter in order
		return new int[][] {{122, 99, 98, 109, 120},	//| z c b m x | 0
							{118, 110, 97, 103, 108},   //| v n a g l | 1
							{115, 102, 104, 107, 113},  //| s f h k q | 2
							{101, 116, 117, 111, 119},  //| e t u o w | 3
							{114, 121, 105, 112, 100}}; //| r y i p d | 4            
	}                                                   // -----------  y
														//x 0 1 2 3 4
	
	private class switchBtnListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (mode) {
				operateBtn.setText("Encrypt");
				instructions.setText("Type the text you wish to encode here");
				switchBtn.setText("Switch to decrypt mode");
			}
			else {
				operateBtn.setText("Decrypt");
				instructions.setText("Type the text you wish to decode here");
				switchBtn.setText("Switch to encrypt mode");
			}
			resultLabel.setText("");
			mode = !mode;
		}
	}
	
	private class operateBtnListener implements ActionListener {
		public void actionPerformed(ActionEvent event2) {
			int[][] playfair = setup();
			String rawstr = inputField.getText();
			rawstr = rawstr.toLowerCase();
			rawstr = rawstr.replaceAll(" +", "");
			char[] raw = rawstr.toCharArray();
			int counter = 0;
			for(int c = 1; c<raw.length; c++) { //starts at 1 to avoid out of bounds error
				if(raw[c] == raw[c-1]) {		 //if it finds the same character beside itself:
					char[] postMatchStorage = new char[raw.length - c]; //creates an array to store everything (including the second instance of the character) after the match
					for (int charsAfterMatch = c; charsAfterMatch<raw.length; charsAfterMatch++) { //stores them in that array
						postMatchStorage[counter] = raw[c+counter];
						counter = counter + 1;
					}
					counter = 0;
					raw = Arrays.copyOf(raw, raw.length + 1); //adds 1 to the array length
					raw[c] = 'x'; //inserts x in-between the duplicated characters
					for(int elementToFill = c + 1; elementToFill<raw.length; elementToFill++) { //fills in the rest of the array with everything that was after the match
						raw[elementToFill] = postMatchStorage[counter];
						counter = counter + 1;
					}
					counter = 0;
				}
			}
			if((raw.length % 2) != 0) { //adjusts array length by adding an x to the end if it does not have an even number of letters
				System.out.println("Array length is not even, adding an 'x'");
				raw = Arrays.copyOf(raw, raw.length + 1); //adds one to the array's length, thanks to StackOverflow user Evgeniy Dorofeev for their Java knawledge
				raw[raw.length-1] = 'x';
			}
			//System.out.println(new String(raw));
			String result = new String("");
			counter = 0; //still using counter
			int[] jcases = new int[raw.length];
			Arrays.fill(jcases, -1); //fills jcases with -1s so that the first character is not always assumed to be a j upon display (default = filled with 0)
			int[] intcode = new int[raw.length]; //begins converting every character to an int before processing, the final step
			for(int p = 0; p < intcode.length; p++) { //gets ready to iterate
				if(raw[p]=='j') { //handles the special case of j by replacing it with a z
					System.out.println("Found a 'j' at " + p);
					jcases[counter] = p;
					raw[p] = 'z';
					counter += 1;
				}
				intcode[p] = (int)raw[p]; //intcode retrieves the ascii decimal representation of each character in the char[] array
			} //string processing is now complete!
			if(!mode) { //encrypt
				for(int i = 0; i<intcode.length; i+=2) { //searches for the letters in the array
					//System.out.print((char)intcode[i]);
					//System.out.println((char)intcode[i+1]);
					int indexAx = -1, indexAy = -1, indexBx = -1, indexBy = -1; //set to -1 to check for not-found
					for(int y = 0; y < playfair.length; y++) {
						for(int x = 0; x < playfair[y].length; x++) {
							if(playfair[y][x] == intcode[i]) {
								indexAx = x;
								indexAy = y;
							}
						}
					}
					if(indexAx == -1 | indexAy == -1) { //if a character is not found, it replaces it with x
						System.out.println("Values for a letter was not found!");
						System.out.println("Replacing this value with the letter 'x'");
						indexAx = 4;
						indexAy = 0;
					}
					for(int y = 0; y < playfair.length; y++) {
						for (int x = 0; x < playfair[y].length; x++) {
							if (playfair[y][x] == intcode[i+1]) {
								indexBx = x;
								indexBy = y;
							}
						}
					}
					if(indexBx == -1 | indexBy == -1) {
						System.out.println("Values for a letter was not found!");
						System.out.println("Replacing this value with the letter 'x'");
						indexBx = 4;
						indexBy = 0;
					}
					//System.out.println((char)intcode[i] + ": (" + indexAx + ", " + indexAy + ") " + (char)intcode[i+1] + ": (" + indexBx + ", " + indexBy + ")");
					//CASES:
					if(indexAy == indexBy) { //SAME ROW
						System.out.println("Same row");
						if(indexAx+1>4) { //if it is the rightmost:
							result = result + (char)playfair[indexAy][0]; //take the character on the leftmost
						}
						else {
							result = result + (char)playfair[indexAy][indexAx+1]; //the character to right of the matched 
						}
						if(indexBx+1>4) { //why did i have 3 here before
							result = result + (char)playfair[indexBy][0];
						}
						else {
							result = result + (char)playfair[indexBy][indexBx+1];
						}
					}
					else if(indexAx == indexBx) { //SAME COLUMN
						System.out.println("Same column");
						if(indexAy+1>4) { //if it is in the bottom row with nothing below it...
							result = result + (char)playfair[0][indexAx]; //take the character at the top
						}
						else {
							result = result + (char)playfair[indexAy+1][indexAx];
						}
						if(indexBy+1>4) {
							result = result + (char)playfair[0][indexBx];
						}
						else {
							result = result + (char)playfair[indexBy+1][indexBx];
						}
					}
					else { //OPPOSITE CORNERS MATCH
						System.out.println("Rectangle");
						result = result + (char)playfair[indexAy][indexBx];
						result = result + (char)playfair[indexBy][indexAx];
					}
				}

			}
			else { //decrypt
				for(int i = 0; i<intcode.length; i+=2) { //searches for the letters in the array
					//System.out.print((char)intcode[i]);
					//System.out.println((char)intcode[i+1]);
					int indexAx = -1, indexAy = -1, indexBx = -1, indexBy = -1; //set to -1 to check for not-found
					for(int y = 0; y < playfair.length; y++) {
						for (int x = 0; x < playfair[y].length; x++) {
							if (playfair[y][x] == intcode[i]) {
								indexAx = x;
								indexAy = y;
							}
						}
					}
					if(indexAx == -1 | indexAy == -1) { 
						System.out.println("Values for a letter was not found!");
						System.out.println("Replacing this value with the letter 'x'");
						indexAx = 4;
						indexAy = 0;
					}
					for(int y = 0; y < playfair.length; y++) {
						for (int x = 0; x < playfair[y].length; x++) {
							if (playfair[y][x] == intcode[i+1]) {
								indexBx = x;
								indexBy = y;
							}
						}
					}
					if(indexBx == -1 | indexBy == -1) {
						System.out.println("Values for a letter was not found!");
						System.out.println("Replacing this value with the letter 'x'");
						indexBx = 4;
						indexBy = 0;
					}
					//System.out.println((char)intcode[i] + ": (" + indexAx + ", " + indexAy + ") " + (char)intcode[i+1] + ": (" + indexBx + ", " + indexBy + ")");
					//CASES:
					if(indexAy == indexBy) { //SAME ROW
						System.out.println("Same row");
						if(indexAx-1<0) { //if it is the leftmost:
							result = result + (char)playfair[indexAy][4]; //take the character on the rightmost
						}
						else {
							result = result + (char)playfair[indexAy][indexAx-1]; //the character to left of the matched 
						}
						if(indexBx-1<0) { //why did i have 3 here before
							result = result + (char)playfair[indexBy][4];
						}
						else {
							result = result + (char)playfair[indexBy][indexBx-1];
						}
					}
					else if(indexAx == indexBx) { //SAME COLUMN
						System.out.println("Same column");
						if(indexAy-1<0) { //if it is in the top row with nothing above it...
							result = result + (char)playfair[4][indexAx]; //take the character at the bottom
						}
						else {
							result = result + (char)playfair[indexAy-1][indexAx];
						}
						if(indexBy-1<0) {
							result = result + (char)playfair[4][indexBx];
						}
						else {
							result = result + (char)playfair[indexBy-1][indexBx];
						}
					}
					else { //OPPOSITE CORNERS MATCH
						System.out.println("Rectangle");
						result = result + (char)playfair[indexBy][indexAx];
						result = result + (char)playfair[indexAy][indexBx];
					} //ufretzzruyqzhvst
					System.out.println(result);
				}
			}
			System.out.println(result);
			char[] xcrypted = new char[result.length()];
			xcrypted = result.toCharArray();
			for(int j = 0; j < jcases.length; j++) { //because fuck it why not use j as the iteration var
				if(jcases[j] != -1) { //the array is filled with -1s, because a value that represents an actual index of a j would never be at -1
					System.out.println(jcases[j]);
					xcrypted[jcases[j]] = 'j';
				}
			}
			String xcryptedDisplay = new String();
			for(int chr = 0; chr < xcrypted.length; chr++) {
				xcryptedDisplay += xcrypted[chr];
			}
			resultLabel.setText(xcryptedDisplay);
		}
	}
	
	public static void main(String[] args) {
		new Cipher();
	}
		
}
