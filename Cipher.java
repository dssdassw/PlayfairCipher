//thanks eclipse, for easily allowing me to import only what I need
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
//the code differences between one side of the class and the other are like America vs Asia
//...if that's the case, is my code Australia?
//bar b q
//a kangaroo attack!
//my god the kangaroos knocked ovah his bar b q
//oh my god they kicked iz banana tree too
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
	private static int[] incrementJcasesByOne(int[] jcases) {
		for (int jcaseIndex = 0; jcaseIndex < jcases.length; jcaseIndex++) {
			if(jcases[jcaseIndex] != -1) {
				jcases[jcaseIndex]++;
			}
		}
		return jcases;
	}
	
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
			int counter = 0;
			String rawstr = inputField.getText();
			rawstr = rawstr.toLowerCase();
			rawstr = rawstr.replaceAll(" +", ""); //replaces all spaces with nothing, trims them out
			char[] raw = rawstr.toCharArray(); //char[]alling in my skiiiinnnn these wounds they will not heaallllll
			//here
			//System.out.println(new String(raw));
			String result = new String("");
			counter = 0; //still using counter
			int[] jcases = new int[raw.length];
			Arrays.fill(jcases, -1); //fills jcases with -1s so that the first character is not always assumed to be a j upon display (default = filled with 0)
			int[] intcode = new int[raw.length]; //begins converting every character to an int before processing, the final step
			for (int p = 0; p < raw.length; p++) { //gets ready to iterate
				if (raw[p]=='j') { //handles the special case of j by replacing it with a z, or a b if on decrypt mode
					System.out.println("Found a 'j' at " + p);
					jcases[counter] = p;
					if (mode) {
						raw[p] = 'x'; //because z & x are secretly lovers
					}                 //tbh it really doesn't matter what this is, just that it is
					else {
						raw[p] = 'z';
					}
					counter += 1;
				}
				intcode[p] = (int)raw[p]; //intcode retrieves the ascii decimal representation of each character in the char[] array
			} //string processing is now complete!
			System.out.print("string to work on = ");
			for (int checker = 0; checker<intcode.length; checker++) {
				System.out.print((char)intcode[checker]);
			}
			System.out.print("\n");
			counter = 0;
			if (!mode) { //encrypt
				for (int c = 1; c < intcode.length; c++) { //starts at 1 to avoid out of bounds error
					if (intcode[c] == intcode[c-1]) {		 //if it finds the same character beside itself:
						System.out.println("Duplicate character found! Separating with 'x'");
						int[] postMatchStorage = new int[intcode.length - c]; //creates an array to store everything (including the second instance of the character) after the match
						for (int charsAfterMatch = c; charsAfterMatch < intcode.length; charsAfterMatch++) { //stores them in that array
							postMatchStorage[counter] = intcode[c+counter]; //iterates past the point of the match via counter
							counter = counter + 1;
						}
						counter = 0; //reset counter to be used later (though now GC will not clean it up, so maybe I should clutter my code with variables instead)
						if (intcode[c] == (int)'x' | intcode[c-1] == (int)'x') {
							System.out.println("The duplicate was x, are you trying to mess this up? Are you the nazis? Are you trying to break the code to find out American battle plans before they happen?");
						}
						else {
							intcode = Arrays.copyOf(intcode, intcode.length + 1); //adds 1 to the array length
							intcode[c] = (int)'x'; //inserts x in-between the duplicated characters
							for (int elementToFill = c + 1; elementToFill<intcode.length; elementToFill++) { //fills in the rest of the array with everything that was after the match
								intcode[elementToFill] = postMatchStorage[counter];
								counter = counter + 1;
							}
						}
						counter = 0; //more counter resetting, WHO WOULD'VE THOUGHT, CERTAINLY NOT THE GC
					}
				}
				if ((intcode.length % 2) != 0) { //adjusts array length by adding an x to the end if it does not have an even number of letters
					System.out.println("Array length is not even, adding an 'x'");

					intcode = Arrays.copyOf(intcode, intcode.length + 1); //adds one to the array's length, thanks to StackOverflow user Evgeniy Dorofeev for their Java knawledge
					intcode[intcode.length-1] = (int)'x';
				}
				//done prepping to encrypt!
				for (int i = 0; i < intcode.length; i+=2) { //searches for the letters in the array
					//System.out.print((char)intcode[i]);
					//System.out.println((char)intcode[i+1]);
					int indexAx = -1, indexAy = -1, indexBx = -1, indexBy = -1; //set to -1 to check for not-found
					for (int y = 0; y < playfair.length; y++) {
						for (int x = 0; x < playfair[y].length; x++) {
							if (playfair[y][x] == intcode[i]) {
								indexAx = x;
								indexAy = y;
							}
						}
					}
					if (indexAx == -1 | indexAy == -1) { //if a character is not found, it replaces it with x
						System.out.println("Values for a letter was not found!");
						System.out.println("Replacing this value with the letter 'v'");
						indexAx = 0;
						indexAy = 1;
					}
					for (int y = 0; y < playfair.length; y++) {
						for (int x = 0; x < playfair[y].length; x++) {
							if (playfair[y][x] == intcode[i+1]) {
								indexBx = x;
								indexBy = y;
							}
						}
					}
					if (indexBx == -1 | indexBy == -1) {
						System.out.println("Values for a letter was not found!");
						System.out.println("Replacing this value with the letter 'v'");
						indexBx = 0;
						indexBy = 1;
					}
					//System.out.println((char)intcode[i] + ": (" + indexAx + ", " + indexAy + ") " + (char)intcode[i+1] + ": (" + indexBx + ", " + indexBy + ")");
					//CASES:
					if (indexAy == indexBy) { //SAME ROW
						System.out.println("Same row");
						if (indexAx + 1 > 4) { //if it is the rightmost:
							result = result + (char)playfair[indexAy][0]; //take the character on the leftmost
						}
						else {
							result = result + (char)playfair[indexAy][indexAx+1]; //the character to right of the matched 
						}
						if (indexBx+1>4) { //why did i have 3 here before
							result = result + (char)playfair[indexBy][0];
						}
						else {
							result = result + (char)playfair[indexBy][indexBx+1];
						}
					}
					else if (indexAx == indexBx) { //SAME COLUMN
						System.out.println("Same column");
						if (indexAy + 1 > 4) { //if it is in the bottom row with nothing below it...
							result = result + (char)playfair[0][indexAx]; //take the character at the top
						}
						else {
							result = result + (char)playfair[indexAy+1][indexAx];
						}
						if (indexBy + 1 > 4) {
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
				if ((intcode.length % 2) != 0) { //adjusts array length by adding an x to the end if it does not have an even number of letters
					System.out.println("Array length is not even, adding an 'x'");
					intcode = Arrays.copyOf(intcode, intcode.length + 1); //adds one to the array's length, thanks to StackOverflow user Evgeniy Dorofeev for their Java knawledge
					intcode[intcode.length - 1] = (int)'x';
				} //because of reasons, both encrypt and decrypt needed their own personal length checkers
				//done prepping to decrypt!
				for (int i = 0; i < intcode.length; i+=2) { //searches for the letters in the array
					//System.out.print((char)intcode[i]);
					//System.out.println((char)intcode[i+1]);
					int indexAx = -1, indexAy = -1, indexBx = -1, indexBy = -1; //set to -1 to check for not-found
					for (int y = 0; y < playfair.length; y++) {
						for (int x = 0; x < playfair[y].length; x++) {
							if (playfair[y][x] == intcode[i]) {
								indexAx = x;
								indexAy = y;
							}
						}
					} //there will be no unfound, all characters are valid in here as long as they aren't breaking it on purpose
					for (int y = 0; y < playfair.length; y++) { //they aren't breaking it on purpose are they
						for (int x = 0; x < playfair[y].length; x++) { //are they nazis?
							if (playfair[y][x] == intcode[i+1]) { //are they trying to crack the code?
								indexBx = x; //do these questions matter?
								indexBy = y; //am I trying to entertain myself? Yes. I am. The worst part is it works.
							}
						}
					}
					//System.out.println((char)intcode[i] + ": (" + indexAx + ", " + indexAy + ") " + (char)intcode[i+1] + ": (" + indexBx + ", " + indexBy + ")");
					//CASES:
					if (indexAy == indexBy) { //SAME ROW
						System.out.println("Same row");
						if (indexAx - 1 < 0) { //if it is the leftmost:
							result = result + (char)playfair[indexAy][4]; //take the character on the rightmost
						}
						else {
							result = result + (char)playfair[indexAy][indexAx-1]; //the character to left of the matched 
						}
						if (indexBx-1<0) { //why did i have 3 here before
							result = result + (char)playfair[indexBy][4];
						}
						else {
							result = result + (char)playfair[indexBy][indexBx-1];
						}
					}
					
					else if (indexAx == indexBx) { //SAME COLUMN
						System.out.println("Same column");
						if (indexAy - 1 < 0) { //if it is in the top row with nothing above it...
							result = result + (char)playfair[4][indexAx]; //take the character at the bottom
						}
						else {
							result = result + (char)playfair[indexAy-1][indexAx];
						}
						if (indexBy - 1 < 0) {
							result = result + (char)playfair[4][indexBx];
						}
						else {
							result = result + (char)playfair[indexBy-1][indexBx];
						}
					}
					else { //OPPOSITE CORNERS MATCH
						System.out.println("Rectangle");
						result = result + (char)playfair[indexAy][indexBx]; //also added to result in the opposite order of encrypting
						result = result + (char)playfair[indexBy][indexAx];
					}
				}
			}
			char[] xcrypted = new char[result.length()];
			xcrypted = result.toCharArray();         //edit: removed swearing
			for (int j = 0; j < jcases.length; j++) { //because [bleep] it why not use j as the iteration var
				if (jcases[j] != -1) { //the array is filled with -1s, because a value that represents an actual index of a j would never be at -1
					xcrypted[jcases[j]] = 'j';
				}
			}
			String xcryptedDisplay = new String();
			System.out.println(xcryptedDisplay); //oh, It's xcrypted since it can be either the encrypted code or decrypted, this block doesn't discriminate obviously
			for (int chr = 0; chr < xcrypted.length; chr++) {
				xcryptedDisplay += xcrypted[chr];
			}
			resultLabel.setText(xcryptedDisplay);
		}
	}
	
	public static void main(String[] args) {
		new Cipher();
	}
		
}
