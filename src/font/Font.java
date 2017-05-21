package font;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import main.Main;
import sprites.FontSprite;
import sprites.Sprite;

public class Font {
	
	private static ArrayList<FontSprite> Text;
	public int image;

	public Font(int[] spriteSize, GL2 gl){

	}
	
	public static ArrayList<FontSprite> getTextures(String text, int x, int y, int[] spriteSize, GL2 gl){
		spriteSize[0] = 14;
		spriteSize[1] = 19;
		Text = new ArrayList<FontSprite>();
		for (int i = 0; i < text.length(); i++){
    		switch (text.charAt(i)){
    			case 'A': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.A, gl));
    				break;
				case 'B': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.B, gl));
					break;
				case 'C': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.C, gl));
					break;
				case 'D': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.D, gl));
					break;
				case 'E': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.E, gl));
					break;
				case 'F': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.F, gl));
					break;
				case 'G': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.G, gl));
					break;
				case 'H': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.H, gl));
					break;
				case 'I': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.I, gl));
					break;
				case 'J': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.J, gl));
					break;
				case 'K': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.K, gl));
					break;
				case 'L': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.L, gl));
					break;
				case 'M': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.M, gl));
					break;
				case 'N': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.N, gl));
					break;
				case 'O': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.O, gl));
					break;
				case 'P': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.P, gl));
					break;
				case 'Q': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.Q, gl));
					break;
				case 'R': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.R, gl));
					break;
				case 'S': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.S, gl));
					break;
				case 'T': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.T, gl));
					break;
				case 'U': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.U, gl));
					break;
				case 'V': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.V, gl));
					break;
				case 'W': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.W, gl));
					break;
				case 'X': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.X, gl));
					break;
				case 'Y': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.Y, gl));
					break;
				case 'Z': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.Z, gl));
					break;
	    		case '0': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.zero, gl));
					break;
	    		case '1': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.one, gl));
					break;
	    		case '2': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.two, gl));
					break;
	    		case '3': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.three, gl));
					break;
	    		case '4': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.four, gl));
					break;
	    		case '5': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.five, gl));
					break;
	    		case '6': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.six, gl));
					break;
	    		case '7': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.seven, gl));
					break;
	    		case '8': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.eight, gl));
					break;
	    		case '9': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.nine, gl));
					break;
	    		case '?': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.question, gl));
					break;
	    		case ':': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.colon, gl));
					break;
	    		case '!': Text.add(new FontSprite(x + ((spriteSize[0]) * i), y, spriteSize, Main.images.exclamation, gl));
					break;
				default: break;
    		}
    	}
		return(Text);
	}

}
