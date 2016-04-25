/*
Encrypting:
encrypt 2 letters at a time
	P1 = 't', P2 = 'h'
	let C1, C2 be the corresponding encoded letters (P1 -> C1, P2 -> C2)


Refer to the cipher table you created:
	if P1, P2 on same row: C1, C2 are the cells directly to the right
	if P1, P2 on same column: C1, C2 are the cells directly below
	if P1, P2 are opposite corners of some rectangle:
	C1, C2 complete the rectangle with C1 and P1 on the same row

Tests:
	Encrypt “the secret is safe”
	> thesecretissafe
	if there are 2 of the same letter paired together, insert an x
	> thesecretisxsafe

	th->uf (rule 3: rectangle)
	es->re (rule 2: same column)
	ec->tz (rule 3: rectangle)
	re->zr (rule 1: same row)
	ti->uy (rule 3: rectangle)
	sx->qz (rule 3: rectangle)
	sa->hv (rule 3: rectangle)
	fe->st (rule 3: rectangle)
	encrypted phrase: ufretzzruyqzhvst

	65-90 & 97-122
	
	
*/


import java.util.Random;
import java.util.Scanner;

public class Cipher {
	private static int[][] setup() { //working with ints during operation, btu will cast to char before display
		Random r = new Random();
		int randnum = 0;
		int counter = 0;
		int cipher[][] = new int[5][5];
		int[] used = new int[25];
		for (int a = 0; a<5; a++) {
			counter = counter + 1;
			for (int b = 0; b<5; b++) {
				System.out.println(counter);
				boolean found = false;
				randnum =  r.nextInt((26)) + 97; //26 is 122-96+1, maximumAsciiLowercaseAlphabetNumber-minimumAsciiLowercaseAlphabetNumber+1
				for (int i = 0; i<25; i++) { //25 being the maximum index of the list
					if (used[i] == randnum) {
						found = true; //to signal that it was not a clean end
						break;
					}
				}
				if (found) {
					System.out.println("Found " + randnum);
					//add an extra iteration:
					if(b>0) {
						b = b - 1;	
					}
					else {
						a = a - 1;
						b = 3;
					}
				}
				else { //will make better after
					used[counter] = randnum;
					cipher[a][b]  = randnum;
				}
				System.out.println("a = " + a);
				System.out.println("b = " + b);
			}
		}
		return cipher;
	}
	public static void main(String[] args) {
		int[][] playfair = setup();
		System.out.println(playfair[4][4]);
		Scanner in = new Scanner(System.in);
		System.out.println("Input the statement you would like to encode: ");
		String rawstr = in.nextLine();
		rawstr.replaceAll(" ", "");
		rawstr.replaceAll("[a-z][a-z]", "x"); //x is ascii code 120, all doubles of a character are replaced with it
		int[] unenc = new int[statement.length-1]; //assuming it's zero-indexed
		System.out.println(statement);
		for (int i = 0; i < unenc.length; i++) {
			unenc[i] = (int)statement[i]; //unenc retrieves the ascii decimal representation of each character in the char[] array
		}
	}
}

/*
How to set a whole row

//suppose you wanted to set row 2...
int M[][] = new int[5][5]
for (int column=0; column<5; column++) {
	M[2][column]=1;
}


How to set a whole column				

//suppose you wanted to set column 3...
int M[][] = new int[5][5]
for (int row=0; row<5; row++) {
	M[row][3]=0;
}


Multiply all odd numbers in the grid by 2

for (int r=0; r<5; r++) {
	for (int c=0; c<5; c++) {
		if (M[r][c] % 2 == 1) {
M[r][c] = M[r][c] * 2;
}
	}
}


Add 1 to all even locations in the grid		
for (int r=0; r<5; r++) {
	for (int c=0; c<5; c++) {
		if (M[r][c] % 2 == 0) {
M[r][c] = M[r][c] + 1;
}
	}
}



Search for the largest value in a row

int row = 2; //use row 2
int largest = M[row][0]; //set largest to column 0 of row 2 as a start point
for (int col=1; col<5; col++) {
if (M[row][col]>largest) {
	largest = M[row][col];
}		
}
//after the loop is done the variable largest contains the largest value in the row
*/
