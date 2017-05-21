package font;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import main.Main;
import sprites.FontSprite;
import sprites.Sprite;

public class Font {
	
	private static ArrayList<FontSprite> Text;
	public static int alphabet[];
	public static int A;
    public static int B;
    public static int C;
    public static int D;
    public static int E;
    public static int F;
    public static int G;
    public static int H;
    public static int I;
    public static int J;
    public static int K;
    public static int L;
    public static int M;
    public static int N;
    public static int O;
    public static int P;
    public static int Q;
    public static int R;
    public static int S;
    public static int T;
    public static int U;
    public static int V;
    public static int W;
    public static int X;
    public static int Y;
    public static int Z;
	public static int zero;
    public static int one;
    public static int two;
    public static int three;
    public static int four;
    public static int five;
    public static int six;
    public static int seven;
    public static int eight;
    public static int nine;
    public static int question;
    public static int colon;
    public static int exclamation;


	public Font(int[] spriteSize, GL2 gl){
		A = Main.glTexImageTGAFile(gl, "Sprites/Font/A.tga", spriteSize);
		B = Main.glTexImageTGAFile(gl, "Sprites/Font/B.tga", spriteSize);
		C = Main.glTexImageTGAFile(gl, "Sprites/Font/C.tga", spriteSize);
		D = Main.glTexImageTGAFile(gl, "Sprites/Font/D.tga", spriteSize);
		E = Main.glTexImageTGAFile(gl, "Sprites/Font/E.tga", spriteSize);
		F = Main.glTexImageTGAFile(gl, "Sprites/Font/F.tga", spriteSize);
		G = Main.glTexImageTGAFile(gl, "Sprites/Font/G.tga", spriteSize);
		H = Main.glTexImageTGAFile(gl, "Sprites/Font/H.tga", spriteSize);
		I = Main.glTexImageTGAFile(gl, "Sprites/Font/I.tga", spriteSize);
		J = Main.glTexImageTGAFile(gl, "Sprites/Font/J.tga", spriteSize);
		K = Main.glTexImageTGAFile(gl, "Sprites/Font/K.tga", spriteSize);
		L = Main.glTexImageTGAFile(gl, "Sprites/Font/L.tga", spriteSize);
		M = Main.glTexImageTGAFile(gl, "Sprites/Font/M.tga", spriteSize);
		N = Main.glTexImageTGAFile(gl, "Sprites/Font/N.tga", spriteSize);
		O = Main.glTexImageTGAFile(gl, "Sprites/Font/O.tga", spriteSize);
		P = Main.glTexImageTGAFile(gl, "Sprites/Font/P.tga", spriteSize);
		Q = Main.glTexImageTGAFile(gl, "Sprites/Font/Q.tga", spriteSize);
		R = Main.glTexImageTGAFile(gl, "Sprites/Font/R.tga", spriteSize);
		S = Main.glTexImageTGAFile(gl, "Sprites/Font/S.tga", spriteSize);
		T = Main.glTexImageTGAFile(gl, "Sprites/Font/T.tga", spriteSize);
		U = Main.glTexImageTGAFile(gl, "Sprites/Font/U.tga", spriteSize);
		V = Main.glTexImageTGAFile(gl, "Sprites/Font/V.tga", spriteSize);
		W = Main.glTexImageTGAFile(gl, "Sprites/Font/W.tga", spriteSize);
		X = Main.glTexImageTGAFile(gl, "Sprites/Font/X.tga", spriteSize);
		Y = Main.glTexImageTGAFile(gl, "Sprites/Font/Y.tga", spriteSize);
		Z = Main.glTexImageTGAFile(gl, "Sprites/Font/Z.tga", spriteSize);
		zero = Main.glTexImageTGAFile(gl, "Sprites/Font/0.tga", spriteSize);
    	one = Main.glTexImageTGAFile(gl, "Sprites/Font/1.tga", spriteSize);
    	two = Main.glTexImageTGAFile(gl, "Sprites/Font/2.tga", spriteSize);
    	three = Main.glTexImageTGAFile(gl, "Sprites/Font/3.tga", spriteSize);
    	four = Main.glTexImageTGAFile(gl, "Sprites/Font/4.tga", spriteSize);
    	five = Main.glTexImageTGAFile(gl, "Sprites/Font/5.tga", spriteSize);
    	six = Main.glTexImageTGAFile(gl, "Sprites/Font/6.tga", spriteSize);
    	seven = Main.glTexImageTGAFile(gl, "Sprites/Font/7.tga", spriteSize);
    	eight = Main.glTexImageTGAFile(gl, "Sprites/Font/8.tga", spriteSize);
    	nine = Main.glTexImageTGAFile(gl, "Sprites/Font/9.tga", spriteSize);
    	question = Main.glTexImageTGAFile(gl, "Sprites/Font/question.tga", spriteSize);
    	colon = Main.glTexImageTGAFile(gl, "Sprites/Font/colon.tga", spriteSize);
    	exclamation = Main.glTexImageTGAFile(gl, "Sprites/Font/exclamation.tga", spriteSize);
    	
    	alphabet = new int[]{A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,zero,one,two,three,four,five,six,seven,eight,nine,question,colon,exclamation};
	}
	
	public static ArrayList<FontSprite> getTextures(String text, int x, int y, int[] spriteSize, GL2 gl){
		spriteSize[0] = 14;
		spriteSize[1] = 19;
		Text = new ArrayList<FontSprite>();
		for (int i = 0; i < text.length(); i++){
    		switch (text.charAt(i)){
    			case 'A': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[0], gl));
    				break;
				case 'B': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[1], gl));
					break;
				case 'C': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[2], gl));
					break;
				case 'D': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[3], gl));
					break;
				case 'E': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[4], gl));
					break;
				case 'F': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[5], gl));
					break;
				case 'G': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[6], gl));
					break;
				case 'H': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[7], gl));
					break;
				case 'I': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[8], gl));
					break;
				case 'J': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[9], gl));
					break;
				case 'K': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[10], gl));
					break;
				case 'L': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[11], gl));
					break;
				case 'M': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[12], gl));
					break;
				case 'N': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[13], gl));
					break;
				case 'O': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[14], gl));
					break;
				case 'P': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[15], gl));
					break;
				case 'Q': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[16], gl));
					break;
				case 'R': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[17], gl));
					break;
				case 'S': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[18], gl));
					break;
				case 'T': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[19], gl));
					break;
				case 'U': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[20], gl));
					break;
				case 'V': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[21], gl));
					break;
				case 'W': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[22], gl));
					break;
				case 'X': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[23], gl));
					break;
				case 'Y': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[24], gl));
					break;
				case 'Z': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[25], gl));
					break;
	    		case '0': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[26], gl));
					break;
	    		case '1': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[27], gl));
					break;
	    		case '2': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[28], gl));
					break;
	    		case '3': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[29], gl));
					break;
	    		case '4': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[30], gl));
					break;
	    		case '5': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[31], gl));
					break;
	    		case '6': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[32], gl));
					break;
	    		case '7': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[33], gl));
					break;
	    		case '8': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[34], gl));
					break;
	    		case '9': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[35], gl));
					break;
	    		case '?': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[36], gl));
					break;
	    		case ':': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[37], gl));
					break;
	    		case '!': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, alphabet[38], gl));
					break;
				default: break;
    		}
    	}
		return(Text);
	}

}
