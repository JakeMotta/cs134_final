package sprites;

import com.jogamp.opengl.GL2;

import main.Main;

public class Images {
	
	protected int image;

	// Block images
	private int blockImg;
	private int bd1;
	private int bd2;
	private int bd3;
	private int bd4;
	private int bd5;
	private int bd6;
	private int bd7;
	private int bd8;
	private int bd9;
	private int bd10;
	
	// Lava images
	private int lava;
	private int lavaTop[] = new int[13];
	
	// Alphabet images
	public int A;
    public int B;
    public int C;
    public int D;
    public int E;
    public int F;
    public int G;
    public int H;
    public int I;
    public int J;
    public int K;
    public int L;
    public int M;
    public int N;
    public int O;
    public int P;
    public int Q;
    public int R;
    public int S;
    public int T;
    public int U;
    public int V;
    public int W;
    public int X;
    public int Y;
    public int Z;
	public int zero;
    public int one;
    public int two;
    public int three;
    public int four;
    public int five;
    public int six;
    public int seven;
    public int eight;
    public int nine;
    public int question;
    public int colon;
    public int exclamation;
	
	public Images(int[] spriteSize, GL2 gl) {
		
		// Block images
		blockImg = Main.glTexImageTGAFile(gl, "Sprites/Blocks/block1.tga", spriteSize);
		bd1 = Main.glTexImageTGAFile(gl, "Sprites/Blocks/bd1.tga", spriteSize);
		bd2 = Main.glTexImageTGAFile(gl, "Sprites/Blocks/bd2.tga", spriteSize);
		bd3 = Main.glTexImageTGAFile(gl, "Sprites/Blocks/bd3.tga", spriteSize);
		bd4 = Main.glTexImageTGAFile(gl, "Sprites/Blocks/bd4.tga", spriteSize);
		bd5 = Main.glTexImageTGAFile(gl, "Sprites/Blocks/bd5.tga", spriteSize);
		bd6 = Main.glTexImageTGAFile(gl, "Sprites/Blocks/bd6.tga", spriteSize);
		bd7 = Main.glTexImageTGAFile(gl, "Sprites/Blocks/bd7.tga", spriteSize);
		bd8 = Main.glTexImageTGAFile(gl, "Sprites/Blocks/bd8.tga", spriteSize);
		bd9 = Main.glTexImageTGAFile(gl, "Sprites/Blocks/bd9.tga", spriteSize);
		bd10 = Main.glTexImageTGAFile(gl, "Sprites/Blocks/bd10.tga", spriteSize);
		
		// Lava images
		lava = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava.tga", spriteSize);
		lavaTop[0] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top1.tga", spriteSize);
		lavaTop[1] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top2.tga", spriteSize);
		lavaTop[2] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top3.tga", spriteSize);
		lavaTop[3] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top4.tga", spriteSize);
		lavaTop[4] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top5.tga", spriteSize);
		lavaTop[5] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top6.tga", spriteSize);
		lavaTop[6] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top7.tga", spriteSize);
		lavaTop[7] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top8.tga", spriteSize);
		lavaTop[8] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top9.tga", spriteSize);
		lavaTop[9] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top10.tga", spriteSize);
		lavaTop[10] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top11.tga", spriteSize);
		lavaTop[11] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top12.tga", spriteSize);
		lavaTop[12] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top13.tga", spriteSize);
		
		// Alphabet images
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
	}
	
	// Get image of lava
	public int getLavaImage() {
		image = lava;
		return image;
	}
	
	// Get lava top images
	public int getLavaTopImage(int num) {
		image = lavaTop[num];
		return image;
	}

	// Get different block images
	public int getBlockImage(int num) {
		switch (num) {
			case 100:
				image = blockImg;
				break;
			case 99:
				image = bd1;
				break;
			case 90:
				image = bd2;
				break;
			case 80:
				image = bd3;
				break;
			case 70:
				image = bd4;
				break;
			case 60:
				image = bd5;
			case 50:
				image = bd6;
				break;
			case 40:
				image = bd7;
				break;
			case 30:
				image = bd8;
				break;
			case 20:
				image = bd9;
				break;
			case 10:
				image = bd10;
				break;
    	}
		return image;
	}
		
}
