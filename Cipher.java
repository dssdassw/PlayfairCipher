import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

@SuppressWarnings("serial")
public class Cipher extends JFrame {
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
		buildPanel();
		add(root);
		pack();
		setVisible(true);
	}
	
	private void buildPanel() {
		root = new JPanel();
		operateBtn = new JButton("Encrypt");
		switchBtn = new JButton("Switch to decrypt mode");
		instructions = new JLabel("Type the text you wish to encode below");
		resultLabel = new JLabel(" ");
		JPanel topPanel = new JPanel();
		JPanel btmPanel = new JPanel();
		JTextField inputField = new JTextField();
		inputField.setText("the secret is safe");
		root.setLayout(new GridLayout(2, 1));
		topPanel.setLayout(new GridLayout(2, 2));
		btmPanel.setLayout(new GridLayout(1, 1));
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		btmPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		operateBtn.addActionListener(new operateBtnListener());
		switchBtn.addActionListener(new switchBtnListener());
		topPanel.add(instructions);
		topPanel.add(operateBtn);
		topPanel.add(inputField);
		topPanel.add(switchBtn);
		btmPanel.add(resultLabel);
		root.add(topPanel);
		root.add(btmPanel);
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
		public void actionPerformed(ActionEvent e) {
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
		public void actionPerformed(ActionEvent e) {
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
					raw = Arrays.copyOf(raw, raw.length + 1); //adds one to the array's length, thanks to StackOverflow user Evgeniy Dorofeev for their Java knawledge
					raw[c] = 'x'; //inserts x in-between the duplicated characters
					for(int elementToFill = c + 1; elementToFill<raw.length; elementToFill++) { //fills in the rest of the array with everything that was after the match
						raw[elementToFill] = postMatchStorage[counter];
						counter = counter + 1;
					} //done!
					counter = 0;
				}
			}
			//System.out.println(new String(raw));
			String result = new String("");
			int[] intcode = new int[raw.length]; //begins converting every character to an int before processing, the final step
			for (int i = 0; i < intcode.length; i++) { //gets ready to iterate
				intcode[i] = (int)raw[i]; //intcode retrieves the ascii decimal representation of each character in the char[] array
			}
			for(int i = 0; i<intcode.length; i+=2) { //searches for the letters in the array
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
				if(indexAx == -1 | indexAy == -1) {
					System.out.println("Values for some letters were not found!"); //deal with this error better later
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
					System.out.println("Values for some letters were not found!"); //same as above
				}
				//System.out.println((char)intcode[i] + ": (" + indexAx + ", " + indexAy + ") " + (char)intcode[i+1] + ": (" + indexBx + ", " + indexBy + ")");
				//CASES:
				if(indexAy == indexBy) { //SAME ROW
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
					if(indexAy+1>4) { //if it is in the bottom row with nothing below it...
						result = result + (char)playfair[0][indexAx];
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
					result = result + (char)playfair[indexAy][indexBx];
					result = result + (char)playfair[indexBy][indexAx];
				}
			}
			resultLabel.setText(result);
		}
	}
	
	public static void main(String[] args) {
		new Cipher();
	}
		
}
