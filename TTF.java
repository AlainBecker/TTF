import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;

import javax.swing.*; 
import javax.swing.border.Border;

public class TTF extends JFrame{// Ten Twenty Four
	public static int rows = 3;//Width of the playing board.
	public static int columns = 3;//Length of the playing board.
	public static int score = 0;
	public static int highscore =0;
	public int slidespawnnum = 1;
	
	private JPanel cp1 = null,cp2 = null,cp3 = null;
	private JButton b1= null, b3 = null,b2= null,b4 = null;
	public JLabel lab1=null,lab2=null,lab3=null,lab4=null,lab5=null,lab6=null,lab7=null;
	public JLabel[][] cell= new JLabel[rows][columns];
	public int val[][] = new int[rows][columns];
	public int undo[][] = new int[rows][columns];
	private boolean rewrite = false;
	private Random random = new Random();
	private static final long serialVersionUID = 1L;
	public static void main(String[] args) {
		TTF JZ = new TTF();
		JZ.setVisible(true);
	}
	
	public TTF() {super("1024      ©JZ 2015");
		cp1 = new JPanel(new BorderLayout(0,0));
		cp3 = new JPanel(new GridLayout(2,3,0,0));
		lab1 = new JLabel("Current Tile: ");
		lab2 = new JLabel("0");
		lab3 = new JLabel("Highest Tile Achieved: ");
		lab4 = new JLabel("0");
		lab5 = new JLabel("1024 - Ten Twenty Four");
		lab5.setForeground(Color.BLUE);
		lab5.setToolTipText("Welcome to 1024-Join the numbers and get to the 1024 tile!");
		lab6 = new JLabel("Join the numbers and get to the 1024 tile!");
		
		lab1.setBackground(Color.lightGray);
		lab1.setBorder(BorderFactory.createMatteBorder(6, 6, 0, 6, Color.gray));
		lab1.setOpaque(true);
		lab2.setBackground(Color.lightGray);
		lab2.setBorder(BorderFactory.createMatteBorder(0, 6, 0, 6, Color.gray));
		lab2.setOpaque(true);
		lab3.setBackground(cellcolor(maxvalue()));
		lab3.setBorder(BorderFactory.createMatteBorder(6, 0, 0, 6, Color.gray));
		lab3.setOpaque(true);
		lab4.setBackground(cellcolor(maxvalue()));
		lab4.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 6, Color.gray));
		lab4.setOpaque(true);
		
		cp3.add(lab1);
		cp3.add(lab3);
		cp3.add(lab5);
		cp3.add(lab2);
		cp3.add(lab4);
		
		//cp3.add(lab6);
		b2 = new JButton("Undo");
		b2.setToolTipText("Click to undo last move.");
		b2.setEnabled(false);
		cp3.add(b2);
		cp1.add(cp3, BorderLayout.NORTH);
		
		cp2 = new JPanel( new GridLayout(rows,columns,0,0) );
		cp2.setOpaque(true);
		cp2.setFocusable(true);
		cp2.setBackground(Color.gray);
		Border border = BorderFactory.createMatteBorder(3, 3, 0, 0, Color.gray);
		cp2.setBorder(border);
	    construct();
        cp1.add(cp2, BorderLayout.CENTER);
        
		this.setContentPane(cp1);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(columns*100,rows*110);
		b2.addActionListener(new buttons());
		cp2.requestFocusInWindow();
		InputMap in = cp2.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
			in.put(KeyStroke.getKeyStroke("D"), "right");
			in.put(KeyStroke.getKeyStroke("A"), "left");
	        in.put(KeyStroke.getKeyStroke("W"), "up");
	        in.put(KeyStroke.getKeyStroke("S"), "down");
	        in.put(KeyStroke.getKeyStroke("UP"),"up");
	        in.put(KeyStroke.getKeyStroke("LEFT"), "left");
	        in.put(KeyStroke.getKeyStroke("RIGHT"), "right");
	        in.put(KeyStroke.getKeyStroke("DOWN"), "down");
	        
	        cp2.getActionMap().put("left", new Slide("left"));
	        cp2.getActionMap().put("right", new Slide("right"));
	        cp2.getActionMap().put("down", new Slide("down"));
	        cp2.getActionMap().put("up", new Slide("up"));
	}
	
	private void popup(String result) {
		b1 = new JButton("Restart");
		b1.setToolTipText("Click here to restart the game");
		b3 = new JButton("Continue");
		b3.setToolTipText("Click here to keep playing.");
	    JPanel pu = new JPanel(new GridLayout(0, 2));
		if(result == "win"){
			result = "won and reached 1024!";
			b3.setVisible(true);
		}else{
			result = "lost the game";
			b3.setVisible(false);
		}
		JLabel pulab=new JLabel ("You "+result+"! ");
		JLabel pulab2=new JLabel (" Would you like to:");
	    pu.add(pulab);
	    pu.add(pulab2);
	    pu.add(b3);
	    pu.add(b1);
	    b1.addActionListener(new buttons());
	    b3.addActionListener(new buttons());
	    final JOptionPane optionPane = new JOptionPane(pu, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
		final JDialog dialog = new JDialog();
		dialog.setTitle("Results Screen");
		dialog.setModal(true);
		dialog.setContentPane(optionPane);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.pack();
		dialog.setVisible(true);
	    
	}
	
	private class Slide extends AbstractAction{
		private static final long serialVersionUID = 1L;
		private char d = ' ';
		public Slide(String Direction) {
			d = Direction.charAt(0);
		}
		public void actionPerformed(ActionEvent e) {
			undo();
			Border cellborder = BorderFactory.createLineBorder(Color.GRAY, 3);
			for(int x = 0; x < rows; x++) {
		       for(int y = 0; y < columns ; y++) {
		        	cell[x][y].setBorder(cellborder);
		       }
			}
			b2.setEnabled(true);
			switch (d){ 
			case 'r': right(); 
			break; 
			case 'l': left(); 
			break; 
			case 'u': up(); 
			break; 
			case 'd': down(); 
			break; 
			} 
			if(gg()){
				rewrite = false;
				writefile(hs);
				writefile(String.valueOf(maxvalue()));
				writefile("\n");
				writefile(date);
				popup("nope");
			}
			if(score == 1024){
				popup("win");
			}
		}
		private void right(){
			for(int x = 0; x < rows; x++){
				for(int y = columns-2; y>=0  ; y--){
					if(val[x][y+1]==val[x][y]){
						val[x][y+1]=val[x][y]*2;
						val[x][y] = 0;
					}
		        }
			}
			for(int z = 0; z < columns ; z++){
				for(int x = 0; x < rows; x++){
					for(int y = columns-2; y>=0  ; y--){
						if(val[x][y+1]==0&&val[x][y]!=0){
							val[x][y+1]=val[x][y];
							val[x][y] = 0;
						}
					}
				}
			}
			set();
			zero();
			slidespawn();
		}
		private void left() {
			for(int x = 0; x < rows; x++){
				for(int y = 1; y < columns ; y++){
					if(val[x][y-1]==val[x][y]){
						val[x][y-1]=val[x][y]*2;
						val[x][y] = 0;
					}
		        }
			}
			for(int z = 0; z < columns ; z++){
			for(int x = 0; x < rows; x++){
				for(int y = 1; y < columns ; y++){
					if(val[x][y-1]==0&&val[x][y]!=0){
						val[x][y-1]=val[x][y];
						val[x][y] = 0;
					}
				}
			}
		}
			set();
			zero();
			slidespawn();
		}
		private void up() {
			for(int x = 1; x < rows; x++){
				for(int y = 0; y < columns ; y++){
					if(val[x][y]==val[x-1][y]){
						val[x-1][y]=val[x][y]*2;
						val[x][y] = 0;
					}
				}
			}
			for(int z = 0; z < rows ; z++){
				for(int x = 1; x < rows; x++){
					for(int y = 0; y < columns ; y++){
						if(val[x-1][y]==0&&val[x][y]!=0){
							val[x-1][y]=val[x][y];
							val[x][y] = 0;
						}
					}
				}
			}
			set();
			zero();
			slidespawn();
		}
		private void down() {
			for(int x = rows-1; x >0; x--){
				for(int y = 0; y < columns ; y++){
					if(val[x][y]==val[x-1][y]){
						val[x][y]=val[x-1][y]*2;
						val[x-1][y] = 0;
					}
				}
			}
			for(int z = 0; z < rows ; z++){
				for(int x = rows-1; x >0; x--){
					for(int y = 0; y < columns ; y++){
						if(val[x][y]==0&&val[x-1][y]!=0){
							val[x][y]=val[x-1][y];
							val[x-1][y] = 0;
						}
					}
				}
			}
			set();
			zero();
			slidespawn();
		}
		private void set() {
			 for(int x = 0; x < rows; x++) {
		        	for(int y = 0; y < columns; y++) {
		        		if(maxvalue()>7){
		        			lab1.setForeground(Color.white);
		        			lab2.setForeground(Color.white);
		        		}else if(maxvalue()<7){
		        			lab1.setForeground(Color.DARK_GRAY);
		        			lab2.setForeground(Color.DARK_GRAY);
		        		}
		        		lab2.setText(String.valueOf(maxvalue()));
		        		lab2.setBackground(cellcolor(maxvalue()));
		        		lab1.setBackground(cellcolor(maxvalue()));
		        		String celltext = String.valueOf(val[x][y]);
		        		if(val[x][y]>120){
		        			cell[x][y].setFont(bignum);
		        		}else if(val[x][y]<120){
		        			cell[x][y].setFont(smallnum);
		        		}
		        		cell[x][y].setText(celltext);
		        		cell[x][y].setToolTipText("Combine this "+celltext+" tile with another "+celltext+" tile to merge into a "+val[x][y]*2 + " tile!");
		        		cell[x][y].setBackground(cellcolor(val[x][y]));
		        		if(val[x][y]>7){
		        			cell[x][y].setForeground(Color.white);
		        		}else if(val[x][y]<7){
		        			cell[x][y].setForeground(Color.DARK_GRAY);
		        		}
		    		}
		        }
		}
		private void zero() {
			for(int x = 0; x < rows; x++){
				for(int y = 0; y < columns ; y++){
					if((val[x][y])==0){
						cell[x][y].setText("");
						cell[x][y].setToolTipText("This is a zero tile, a 2 tile may randomly spawn here next move!");
					}
				}
			}
			
		}
		private void slidespawn() {
			if(highscore < score){
				highscore = score;
				lab4.setText(String.valueOf(highscore));
				lab4.setBackground(cellcolor(highscore));
				lab3.setBackground(cellcolor(highscore));
				rewrite = false;
				writefile(hs);
				writefile(String.valueOf(highscore));
			}
			if(allowspawn()){
				int slidespawnnum =1;
		        for(int spawncount = 0; spawncount <slidespawnnum; ) {
		    		int spawnnumx = random.nextInt(rows);
		    		//System.out.println(spawnnumx );
		    		int spawnnumy = random.nextInt(columns);
		    		//System.out.println(spawnnumy );
		    		for(int x = 0; x < rows; x++) {
		    			for(int y = 0; y < columns; y++) {
		    				if (x ==spawnnumx &&y ==spawnnumy && spawncount<slidespawnnum &&val[x][y]!=2&&val[x][y]==0){
		    					Border cellborder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 3);
		    					cell[x][y].setBorder(cellborder);
		    					val[x][y]=2;
		    					spawncount++;
		    					set();
		    					zero();
		    				}
		    			}
		    		}	
		        }
			}else{
				for(int x = 0; x < rows; x++) {
	    			for(int y = 0; y < columns; y++) {
	    				if (val[x][y]!=2&&val[x][y]==0){
	    	        		val[x][y]=2;
	    				}
	    			}
				}
			}
		set();
    	zero();
		}
	}
	
	private class buttons implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JOptionPane.getRootFrame().dispose(); 
			if(e.getSource()==b1){
				construct();
				cp2.updateUI();
			}
			if(e.getSource()==b2){
				 for(int x = 0; x < rows; x++) {
			        	for(int y = 0; y < columns; y++) {
			        		val[x][y] = undo[x][y];
			        		String celltext = String.valueOf(val[x][y]);
			        		if(val[x][y]>120){
			        			cell[x][y].setFont(bignum);
			        		}else if(val[x][y]<120){
			        			cell[x][y].setFont(smallnum);
			        		}
			        		cell[x][y].setText(celltext);
			        		cell[x][y].setToolTipText("Combine this "+celltext+" tile with another "+celltext+" tile to merge into a "+val[x][y]*2 + " tile!");
			        		cell[x][y].setBackground(cellcolor(val[x][y]));
			        		if(val[x][y]>7){
			        			cell[x][y].setForeground(Color.white);
			        		}else if(val[x][y]<7){
			        			cell[x][y].setForeground(Color.DARK_GRAY);
			        		}
			    		}
			        }
				lab2.setText(String.valueOf(maxvalue()));
	        	lab2.setBackground(cellcolor(maxvalue()));
	        	lab1.setBackground(cellcolor(maxvalue()));
	        	for(int x = 0; x < rows; x++){
					for(int y = 0; y < columns ; y++){
						if((val[x][y])==0){
							cell[x][y].setText("");
							cell[x][y].setToolTipText("This is a zero tile, a 2 tile may randomly spawn here next move!");
						}
					}
				}
	        	Border cellborder = BorderFactory.createLineBorder(Color.GRAY, 3);
				for(int x = 0; x < rows; x++) {
			       for(int y = 0; y < columns ; y++) {
			        	cell[x][y].setBorder(cellborder);
			       }
				}
				b2.setEnabled(false);
			}
		}
	}
	
	public final Font smallnum = new Font("Lithograph", Font.BOLD, 60);
    public final Font bignum = new Font("Lithograph", Font.BOLD, 36);
	public void construct(){
		try{
		max=0;
		lab2.setText("0");
    	lab2.setBackground(Color.LIGHT_GRAY);
    	lab1.setBackground(Color.LIGHT_GRAY);
    	score = 0;
		Scanner inf = readfile(UDF+"/JZ Highscore File.txt",1);
		highscore= inf.nextInt();
		
		lab4.setText(String.valueOf(highscore));
		lab4.setBackground(cellcolor(highscore));
		lab3.setBackground(cellcolor(highscore));
		if(highscore>7){
			lab3.setForeground(Color.white);
			lab4.setForeground(Color.white);
		}else if(highscore<7){
			lab3.setForeground(Color.DARK_GRAY);
			lab4.setForeground(Color.DARK_GRAY);
		}
		}catch (NullPointerException ex ){
			writefile(hs);
			writefile(String.valueOf(maxvalue()));
			construct();
		}
		Border cellborder = BorderFactory.createLineBorder(Color.GRAY, 3);
		cp2.removeAll();
        for(int x = 0; x < rows; x++) {
        	for(int y = 0; y < columns ; y++) {
    		cell[x][y] = new JLabel("", SwingConstants.CENTER);
    		cell[x][y].setFont(smallnum);
			cell[x][y].setForeground(Color.DARK_GRAY);
    		cell[x][y].setOpaque(true);
        	val[x][y] = 0;
        	}
        }
        int total =(rows*columns)/5;
        for(int spawncount = 0; spawncount <total; ) {
    		int spawnnumx = random.nextInt(rows);
    		//System.out.println(spawnnumx );
    		int spawnnumy = random.nextInt(columns);
    		//System.out.println(spawnnumy );
    		for(int x = 0; x < rows; x++) {
    			for(int y = 0; y < columns; y++) {
    				if (x ==spawnnumx &&y ==spawnnumy && spawncount<total &&val[x][y]!=2){
    	        		val[x][y]=2;
    					spawncount++;
    				}
    			}
    		}	
        }
        for(int x = 0; x < rows; x++) {
        	for(int y = 0; y < columns; y++) {
        		//val[x][y]= (int) Math.pow(2, x*y)*4;
        		//val[x][y]= 512;
        		String celltext = String.valueOf(val[x][y]);
        		if(val[x][y]!=0){
        			cell[x][y].setText(celltext);
        		}
        		cell[x][y].setBackground(cellcolor(val[x][y]));
    			cell[x][y].setBorder(cellborder);
    			if(val[x][y]>7){
        			cell[x][y].setForeground(Color.white);
        		}else if(val[x][y]<7){
        			cell[x][y].setForeground(Color.DARK_GRAY);
        		}
    			cell[x][y].setToolTipText("Combine this "+celltext+" tile with another "+celltext+" tile to merge into a "+val[x][y]*2 + " tile!");
    			cp2.add(cell[x][y]);
    		}
        }
        //System.out.println(gg());
       // System.out.println(maxvalue());
	}
	
	public Color cellcolor(int cellval){
		Color color = Color.lightGray;
	    switch (cellval) {
	    case 0:     color = Color.lightGray;
	    			break;
        case 2:     color = new Color(238,228,218);
                    break;
        case 4:     color = new Color(237,224,200);
                    break;
        case 8:     color = new Color(242,177,121);
                    break;
        case 16:    color = new Color(245,149,98);
                    break;
        case 32:    color = new Color(246,124,95);
                    break;
        case 64:    color = new Color(246,94,59);
                    break;
        case 128:   color = new Color(237,207,114);
                    break;
        case 256:   color = new Color(237,204,97);
                    break;
        case 512:   color = new Color(237,200,80);
                    break;
        case 1024:  color = new Color(237,197,63);
                    break;
        case 2048:  color = new Color(237,194,46);
                    break;
        case 4096:  color = Color.green;
        			break;
        case 8192:  color = Color.blue;
					break;
        case 16384:  color = Color.black;
					break;
        default:    color = Color.GRAY;
                    break;
	    }
	    return color;
	}

    private int max = 0;
	public int maxvalue() {
	    for(int x = 0; x < rows; x++) {
			for(int y = 0; y < columns; y++) {
	            if (max < val[x][y]) {
	                max = val[x][y];
	                score = max;
	            }
	        }
	    }
	    return max;
	}
	
	public boolean gg() {
		boolean dead = true;
		for(int x = 0; x < rows; x++) {
			for(int y = 0; y < columns; y++){
				if (val[x][y] == 0) {
					dead = false;
				}
		        if (x > 0) {
		        if (val[x-1][y] == val[x][y]) {
		        	dead = false;
		          	}
		        }
		        if (y > 0) {
		        	if (val[x][y-1] == val[x][y]) {
		        		dead = false;
		        	}
		        }
			}
		}
	    return dead;
	}

	public boolean allowspawn() {
		Boolean spawnallowed = true;
		int spaces = 0;
		for(int x = 0; x < rows; x++) {
			for(int y = 0; y < columns; y++){
				if (val[x][y] == 0) {
					spaces++;
		        }
			}
		}
		if(spaces <=slidespawnnum){
			 spawnallowed = false ;
		}
		return spawnallowed;
	}
	
	public void undo() {
		for(int x = 0; x < rows; x++) {
			for(int y = 0; y < columns; y++){
				undo[x][y] = val[x][y];
			}
		}
	}
	private static String Spacer = "\n\n\n\n\n\n\n\n\n\n\n\n";
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static Date dat = new Date();
	private static String date = dateFormat.format(dat);
	public static String UDF= System.getProperty("user.home") + "/Desktop";
	public static String hs= "Your Highest Tile Achieved is:";
	public static String completed ="Completed!!! Please check your desktop for the results,\n"+
		 							"In a file named \"JZ Ch11 Output File.txt\" located at "+ UDF;
	public Scanner readfile(String name, int skippedlines) {
		Scanner inf =null;
		try{
			File infile = new File(name);
			inf = new Scanner(infile);
			for(int x = 0; x < skippedlines; x++) {
				inf.nextLine();
			}
		}catch (IOException  ex){
			System.out.println("File is missing!");
		}
		return inf;
	}
	
	public void writefile(String in)  {
		try{
		File output = new File(UDF,"JZ Highscore File.txt");
		PrintWriter op = new PrintWriter(new FileWriter(output, rewrite));
		op.println(in);
		op.close();
		rewrite = true;
		}catch (IOException  ex){
			System.out.println("File is missing!");
		}
	}
	
	public static boolean restarted = false;
	public void restart() {
		System.out.println(" ");
		System.out.println("Do you want to try again(yes/no)?");
		System.out.print("(y/n?):");
		Scanner key = new Scanner(System.in);
		String ag = key.nextLine();
		String aga = ag.replaceAll("\\s+","");
		char cho =aga.toLowerCase().charAt(0);
		if (cho== 'y'){
			System.out.println(Spacer);
			restarted = true;
			main(null);
		}else{
			if(cho== 'n'){
			System.out.println(Spacer);
			System.out.println("Thank you for playing!!! Hope you enjoyed this Game XD");
			System.out.println(completed);
		}else{
			System.out.println(" ");
			System.out.println("You need to enter Yes or No!!!");
			try {
				Thread.sleep(600);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Spacer);
			restart();//recursion time XD
		}
			}
	}
}

