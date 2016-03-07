import javax.swing.*;    
import java.util.*;  
import java.awt.*;  
import java.io.*;
import java.awt.event.*;  ; 
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Sokoban extends JPanel {    
    
    public LevelReader lr;    
    public BufferedImage player,wall,empty,box,goal,boxongoal;
    public Contents[][] curLevel;     
    public boolean isReady=false;       
    public int level = 0;  
    public int x, y;
    public int rx, ry;  
    public enum Directions {LEFT, RIGHT, UP, DOWN};    
    
    public Sokoban (String fileName) {    
        lr = new LevelReader();  
        lr.readLevels(fileName);          
        initLevel(level);  
        int[] loc = PlayerLocation();  
        rx = loc[0];  
        ry = loc[1];  
        
	
        try {                
            player = ImageIO.read(new File("ironman.gif"));
            wall = ImageIO.read(new File("wall.png")); 
            box = ImageIO.read(new File("box.png"));
            empty = ImageIO.read(new File("empty.png"));
	    goal = ImageIO.read(new File("goal.png"));
	    boxongoal=ImageIO.read(new File("boxongoal.png"));
        } catch (IOException ex) {
            
        }	
        this.addKeyListener(new MyKeyListener());  
        this.setFocusable(true);  
        this.requestFocus();  
        this.setBackground(Color.BLACK);  
    }  
    
    public void initLevel(int level) {  
        int x = lr.getWidth(level);    
        int y = lr.getHeight(level);    
	
        curLevel = new Contents[x][];    
        for (int i = 0; i < x; i++) {    
            curLevel[i] = new Contents[y];    
            for (int p = 0; p < y; p++) {    
                curLevel[i][p] = lr.getTile(level, i, p);    
            }    
        }           
        this.setPreferredSize(new Dimension(2000,2000));    
	
    }    
    
    public void paintComponent(Graphics g) {    
        super.paintComponent(g);    
        Graphics2D g2 = (Graphics2D) g;    
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);    
        if (isReady=true) {    
            int  w = 100, h = 100;   
            int x2 = 100, y2 = 100;
	    
            for (Contents l[] : curLevel) {    
                for (Contents c : l) {   
                    switch (c) {    
			
		    case BOX:    
                        g2.drawImage(box,x2,y2,null);    
                        
                        break;    
		    case WALL:    
                        g2.drawImage(wall,x2,y2,null);
			
                        break;    
		    case EMPTY:    
                        g2.drawImage(empty,x2,y2,null);        
	                
                        break;   
		    case GOAL:   
			g2.drawImage(goal,x2,y2,null); 
			
                        break;    
		    case PLAYER:    
                        g2.drawImage(player,x2,y2,null);
			
                        break;    
		    case BOXONGOAL:    
			g2.drawImage(boxongoal,x2,y2,null);    
			
                        break;    
		    case PLAYERONGOAL:    
			g2.drawImage(goal,x2,y2,null);
			g2.drawImage(player,x2,y2,null);
                        break;    
                    }  
                    y2+=50;
                }    
                x2+=50;
                y2-=((curLevel[x].length)*50) ;
            }   
        }    
    }    

     public int[] PlayerLocation(){  
        int[] location = new int[2];  
        for(int x = 0; x < curLevel.length; x++) {  
            for(int y = 0; y < curLevel[x].length; y++) {  
                if(curLevel[x][y] == Contents.PLAYER) {  
                    location[0] = x;  
                    location[1] = y;  

                    return location;  
                }  
            }  
        }  
        return location;  
    }     
    
    public Contents[][] increaseLevel(int lvl) {  
        try {initLevel(lvl + 1); } catch(Exception e) {}  
        level += 1;  
        return curLevel;  
    }  

     public Contents[][] decreaseLevel(int lvl) {  
        try {initLevel(lvl + 1); } catch(Exception e) {}  
        level -= 1;  
        return curLevel;  
    }  

    public void reset(){  
    for (int i = 0; i < curLevel.length; i++) {    
            for (int p = 0; p < curLevel[i].length; p++) {    
                curLevel[i][p] = Contents.EMPTY;  
            }}   
        initLevel(level);        
        int loc[] = PlayerLocation();  
        rx = loc[0];  
        ry = loc[1];  
        repaint();  
    }  

    public void nextLevel(){  
        for (int i = 0; i < curLevel.length; i++) {    
            for (int p = 0; p < curLevel[i].length; p++) {    
                curLevel[i][p] = Contents.EMPTY;  
            }}    

        curLevel = increaseLevel(level);  
        int loc[] = PlayerLocation();  
        rx = loc[0];  
        ry = loc[1];  
        repaint();  
    }  

      public void previousLevel(){  
        for (int i = 0; i < curLevel.length; i++) {    
            for (int p = 0; p < curLevel[i].length; p++) {    
                curLevel[i][p] = Contents.EMPTY;  
            }}    

        curLevel = decreaseLevel(level);  
        int loc[] = PlayerLocation();  
        rx = loc[0];  
        ry = loc[1];  
        repaint();  
    }  

    public boolean complete() {    
        for (int i = 0; i < curLevel.length; i++) {    
            for (int p = 0; p < curLevel[i].length; p++) {    
                if (curLevel[i][p] == Contents.BOX)    
                     return false;    
            }    
        }    

        return true;    
    }
    public class MyKeyListener implements KeyListener {  
        public void keyPressed(KeyEvent e) {  
	    
            switch (e.getKeyCode()) {  
	    case KeyEvent.VK_UP:  
                movePlayer(Directions.UP);  
		
                repaint();
                break;  

	    case KeyEvent.VK_DOWN:  
                movePlayer(Directions.DOWN); 
              
                repaint();
                break;  

	    case KeyEvent.VK_LEFT:  
                movePlayer(Directions.LEFT); 
                
                repaint();
                break;  

	    case KeyEvent.VK_RIGHT:  
                movePlayer(Directions.RIGHT);
                
                repaint();
                break;  

	    case KeyEvent.VK_R:  
                reset();  
                break;  

	    case KeyEvent.VK_N:  
                nextLevel();  
                break;  

	    case KeyEvent.VK_P:
		previousLevel();
		break;
            }}  

        public void keyReleased(KeyEvent e) {};  
	
        public void keyTyped(KeyEvent e) {};  
    }  
    public void movePlayer(Directions direction)  
    {  

        switch(direction){  
	case UP:  
            if(canMove(Directions.UP) && (curLevel[rx][ry-1] == Contents.GOAL || curLevel[rx][ry-1] == Contents.BOXONGOAL)){
                if(curLevel[rx][ry-1] == Contents.BOXONGOAL){
                    if(curLevel[rx][ry] == Contents.PLAYERONGOAL){
                        moveBox(Directions.UP);
                        curLevel[rx][ry - 1] = Contents.PLAYERONGOAL;  
                        curLevel[rx][ry] = Contents.GOAL; 
                    }
                    else {
                        moveBox(Directions.UP);
                        curLevel[rx][ry - 1] = Contents.PLAYERONGOAL;  
                        curLevel[rx][ry] = Contents.EMPTY;
                    }
                }
                else{
                    if(curLevel[rx][ry] == Contents.PLAYERONGOAL){
                        if(curLevel[rx][ry+1] == Contents.GOAL) {
                            moveBox(Directions.UP);
                            curLevel[rx][ry-1] = Contents.PLAYERONGOAL;
                            curLevel[rx][ry-1] = Contents.GOAL;
                        }
                        else{
                            moveBox(Directions.UP);
                            curLevel[rx][ry-1] = Contents.PLAYER;  
                            curLevel[rx][ry] = Contents.GOAL; 
                        }
                    }
                    else{
                        moveBox(Directions.UP);
                        curLevel[rx][ry-1] = Contents.PLAYERONGOAL;  
                        curLevel[rx][ry] = Contents.EMPTY; 
                    }
                }

                ry -= 1;  
                repaint();  
            } 
            else if(canMove(Directions.UP)){  
                moveBox(Directions.UP);    
                if(curLevel[rx][ry] == Contents.PLAYERONGOAL){
                    curLevel[rx][ry - 1] = Contents.PLAYER;  
                    curLevel[rx][ry] = Contents.GOAL; 
                }
                else{
                    curLevel[rx][ry - 1] = Contents.PLAYER;  
                    curLevel[rx][ry] = Contents.EMPTY;  
                }
                ry -= 1;  
                repaint();  
            }  

            break;  
            case DOWN:  

            if(canMove(Directions.DOWN) && (curLevel[rx][ry+1] == Contents.GOAL || curLevel[rx][ry+1] == Contents.BOXONGOAL)){
                if(curLevel[rx][ry+1] == Contents.BOXONGOAL){
                    if(curLevel[rx][ry] == Contents.PLAYERONGOAL){
                        moveBox(Directions.DOWN);
                        curLevel[rx][ry + 1] = Contents.PLAYERONGOAL;  
                        curLevel[rx][ry] = Contents.GOAL; 
                    }
                    else{
                        moveBox(Directions.DOWN);
                        curLevel[rx][ry + 1] = Contents.PLAYERONGOAL;  
                        curLevel[rx][ry] = Contents.EMPTY;
                    }
                }
                else{
                    if(curLevel[rx][ry] == Contents.PLAYERONGOAL){
                        if(curLevel[rx][ry+1] == Contents.GOAL) {
                            moveBox(Directions.DOWN);
                            curLevel[rx][ry+1] = Contents.PLAYERONGOAL;
                            curLevel[rx][ry+1] = Contents.GOAL;
                        }
                        else{
                            moveBox(Directions.DOWN);
                            curLevel[rx][ry+1] = Contents.PLAYER;  
                            curLevel[rx][ry] = Contents.GOAL; 
                        }
                    }
                    else{
                        moveBox(Directions.DOWN);
                        curLevel[rx][ry+1] = Contents.PLAYERONGOAL;  
                        curLevel[rx][ry] = Contents.EMPTY; 
                    }
                }

                ry += 1;  
                repaint();  
            } 
            else if(canMove(Directions.DOWN)){  
                moveBox(Directions.DOWN);    
                if(curLevel[rx][ry] == Contents.PLAYERONGOAL){
                    curLevel[rx][ry + 1] = Contents.PLAYER;  
                    curLevel[rx][ry] = Contents.GOAL; 
                }
                else{
                    curLevel[rx][ry + 1] = Contents.PLAYER;  
                    curLevel[rx][ry] = Contents.EMPTY;  
                }
                ry += 1;  
                repaint();  
            }  

            break;  
            case LEFT:  
            //  
            if(canMove(Directions.LEFT) && (curLevel[rx-1][ry] == Contents.GOAL || curLevel[rx-1][ry] == Contents.BOXONGOAL)){
                if(curLevel[rx-1][ry] == Contents.BOXONGOAL){
                    if(curLevel[rx][ry] == Contents.PLAYERONGOAL){
                        moveBox(Directions.LEFT);
                        curLevel[rx - 1][ry] = Contents.PLAYERONGOAL;  
                        curLevel[rx][ry] = Contents.GOAL; 
                    }
                    else{
                        moveBox(Directions.LEFT);
                        curLevel[rx- 1][ry] = Contents.PLAYERONGOAL;  
                        curLevel[rx][ry] = Contents.EMPTY;
                    }
                }
                else{
                    if(curLevel[rx][ry] == Contents.PLAYERONGOAL){
                        if(curLevel[rx-1][ry] == Contents.GOAL) {
                            moveBox(Directions.LEFT);
                            curLevel[rx-1][ry] = Contents.PLAYERONGOAL;
                            curLevel[rx][ry] = Contents.GOAL;
                        }
                        else{
                            moveBox(Directions.LEFT);
                            curLevel[rx - 1][ry] = Contents.PLAYER;  
                            curLevel[rx][ry] = Contents.GOAL; 
                        }
                    }
                    else{
                        moveBox(Directions.LEFT);
                        curLevel[rx - 1][ry] = Contents.PLAYERONGOAL;  
                        curLevel[rx][ry] = Contents.EMPTY; 
                    }
                }

                rx -= 1;  
                repaint();  
            } 
            else if(canMove(Directions.LEFT)){  
                moveBox(Directions.LEFT);    
                if(curLevel[rx][ry] == Contents.PLAYERONGOAL){
                    curLevel[rx - 1][ry] = Contents.PLAYER;  
                    curLevel[rx][ry] = Contents.GOAL; 
                }
                else{
                    curLevel[rx - 1][ry] = Contents.PLAYER;  
                    curLevel[rx][ry] = Contents.EMPTY;  
                }
                rx -= 1;  
                repaint();  
            }  

            break;  
            case RIGHT:  
            //   
            if(canMove(Directions.RIGHT) && (curLevel[rx+1][ry] == Contents.GOAL || curLevel[rx+1][ry] == Contents.BOXONGOAL)){
                if(curLevel[rx+1][ry] == Contents.BOXONGOAL){
                    if(curLevel[rx][ry] == Contents.PLAYERONGOAL){
                        moveBox(Directions.RIGHT);
                        curLevel[rx + 1][ry] = Contents.PLAYERONGOAL;  
                        curLevel[rx][ry] = Contents.GOAL; 
                    }
                    else {
                        moveBox(Directions.RIGHT);
                        curLevel[rx+ 1][ry] = Contents.PLAYERONGOAL;  
                        curLevel[rx][ry] = Contents.EMPTY;
                    }
                }
                else{
                    if(curLevel[rx][ry] == Contents.PLAYERONGOAL){
                        if(curLevel[rx+1][ry] == Contents.GOAL) {
                            moveBox(Directions.RIGHT);
                            curLevel[rx+1][ry] = Contents.PLAYERONGOAL;
                            curLevel[rx][ry] = Contents.GOAL;
                        }
                        else{
                            moveBox(Directions.RIGHT);
                            curLevel[rx + 1][ry] = Contents.PLAYER;  
                            curLevel[rx][ry] = Contents.GOAL; 
                        }
                    }
                    else{
                        moveBox(Directions.RIGHT);
                        curLevel[rx + 1][ry] = Contents.PLAYERONGOAL;  
                        curLevel[rx][ry] = Contents.EMPTY; 
                    }
                }

                rx += 1;  
                repaint();  
            } 
            else if(canMove(Directions.RIGHT)){  
                moveBox(Directions.RIGHT);    
                if(curLevel[rx][ry] == Contents.PLAYERONGOAL){
                    curLevel[rx + 1][ry] = Contents.PLAYER;  
                    curLevel[rx][ry] = Contents.GOAL; 
                }
                else{
                    curLevel[rx + 1][ry] = Contents.PLAYER;  
                    curLevel[rx][ry] = Contents.EMPTY;  
                }
                rx += 1;  
                repaint();  
            }  
            break;  

        }  
    }  

    public void moveBox(Directions direction)   
    {  
        switch(direction){  
            case UP://compares to players position 
            if(curLevel[rx][ry - 1] == Contents.BOXONGOAL  && curLevel[rx][ry - 2] == Contents.GOAL)  
            {  
                curLevel[rx][ry - 2] = Contents.BOXONGOAL;  
                curLevel[rx][ry - 1] = Contents.GOAL;  
            }  
            if(curLevel[rx][ry - 1] == Contents.BOXONGOAL  && curLevel[rx][ry - 2] == Contents.EMPTY)  
            {  
                curLevel[rx][ry - 2] = Contents.BOX;  
                curLevel[rx][ry - 1] = Contents.GOAL;  
            }  
            if(curLevel[rx][ry - 1] == Contents.BOX  && curLevel[rx][ry - 2] == Contents.EMPTY)  
            {  
                curLevel[rx][ry - 2] = Contents.BOX;  
                curLevel[rx][ry - 1] = Contents.EMPTY;  
            }  
            if(curLevel[rx][ry - 1] == Contents.BOX  && curLevel[rx][ry - 2] == Contents.GOAL)  
            { curLevel[rx][ry - 2] = Contents.BOXONGOAL;
                curLevel[rx][ry - 1] = Contents.EMPTY;  
                if(complete()){nextLevel();}
            }     
            break;  
            case DOWN:  
            if( curLevel[rx][ry + 1] == Contents.BOXONGOAL && curLevel[rx][ry + 2] == Contents.GOAL)   
            {  
                curLevel[rx][ry + 2] = Contents.BOXONGOAL;  
                curLevel[rx][ry + 1] = Contents.GOAL;  
            }
            if( curLevel[rx][ry + 1] == Contents.BOXONGOAL && curLevel[rx][ry + 2] == Contents.EMPTY)   
            {  
                curLevel[rx][ry + 2] = Contents.BOX;  
                curLevel[rx][ry + 1] = Contents.GOAL;  
            }
            if( curLevel[rx][ry + 1] == Contents.BOX && curLevel[rx][ry + 2] == Contents.EMPTY)   
            {  
                curLevel[rx][ry + 2] = Contents.BOX;  
                curLevel[rx][ry + 1] = Contents.EMPTY;  
            }  
            if( curLevel[rx][ry + 1] == Contents.BOX && curLevel[rx][ry + 2] == Contents.GOAL)   
            {  
                curLevel[rx][ry + 2] = Contents.BOXONGOAL;
                curLevel[rx][ry + 1] = Contents.EMPTY; 
                if(complete()){nextLevel();}
            }  

            break;  
            case LEFT: 
            if(curLevel[rx - 1][ry] == Contents.BOXONGOAL && curLevel[rx - 2][ry] == Contents.GOAL){   
                curLevel[rx - 2][ry] = Contents.BOXONGOAL;  
                curLevel[rx - 1][ry] = Contents.GOAL;  
            } 
            if(curLevel[rx - 1][ry] == Contents.BOXONGOAL && curLevel[rx - 2][ry] == Contents.EMPTY){   
                curLevel[rx - 2][ry] = Contents.BOX;  
                curLevel[rx - 1][ry] = Contents.GOAL;  
            } 
            if(curLevel[rx - 1][ry] == Contents.BOX && curLevel[rx - 2][ry] == Contents.EMPTY){   
                curLevel[rx - 2][ry] = Contents.BOX;  
                curLevel[rx - 1][ry] = Contents.EMPTY;  
            }  
            if(curLevel[rx - 1][ry] == Contents.BOX && curLevel[rx - 2][ry] == Contents.GOAL)   
            { curLevel[rx - 2][ry] = Contents.BOXONGOAL;
                curLevel[rx - 1][ry] = Contents.EMPTY;
                if(complete()){nextLevel();}
            }  

            break;  
            case RIGHT: 
            if(curLevel[rx + 1][ry] == Contents.BOXONGOAL && curLevel[rx + 2][ry] == Contents.GOAL )  
            {  
                curLevel[rx + 2][ry] = Contents.BOXONGOAL;  
                curLevel[rx + 1][ry] = Contents.GOAL;  
            }  
            if(curLevel[rx + 1][ry] == Contents.BOXONGOAL && curLevel[rx + 2][ry] == Contents.EMPTY )  
            {  
                curLevel[rx + 2][ry] = Contents.BOX;  
                curLevel[rx + 1][ry] = Contents.GOAL;  
            }  
            if(curLevel[rx + 1][ry] == Contents.BOX && curLevel[rx + 2][ry] == Contents.EMPTY )  
            {  
                curLevel[rx + 2][ry] = Contents.BOX;  
                curLevel[rx + 1][ry] = Contents.EMPTY;  
            }  
            if(curLevel[rx + 1][ry] == Contents.BOX && curLevel[rx + 2][ry] == Contents.GOAL )  
            {curLevel[rx + 2][ry] = Contents.BOXONGOAL;
                curLevel[rx + 1][ry] = Contents.EMPTY; 
                if(complete()){nextLevel();}
            }  

            break;  
        }  
    }  

    public boolean canMove(Directions direction)  
    {         
        switch(direction){  
            case UP:  

            if(curLevel[rx][ry - 1] == Contents.EMPTY ||   
            curLevel[rx][ry - 1] == Contents.GOAL || (curLevel[rx][ry - 1] == Contents.BOX  && curLevel[rx][ry - 2] == Contents.EMPTY)|| (curLevel[rx][ry - 1] == Contents.BOXONGOAL && curLevel[rx][ry - 2] == Contents.EMPTY)|| (curLevel[rx][ry - 1] == Contents.BOX  && curLevel[rx][ry - 2] == Contents.GOAL)|| (curLevel[rx][ry - 1] == Contents.BOXONGOAL  && curLevel[rx][ry - 2] == Contents.GOAL)) { return true; }  
            else {return false;}  
            case DOWN:  
            if(curLevel[rx][ry + 1] == Contents.EMPTY ||   
            curLevel[rx][ry + 1] == Contents.GOAL || (curLevel[rx][ry + 1] == Contents.BOX&& curLevel[rx][ry + 2] == Contents.EMPTY)|| (curLevel[rx][ry + 1] == Contents.BOXONGOAL && curLevel[rx][ry + 2] == Contents.EMPTY)|| (curLevel[rx][ry + 1] == Contents.BOX&& curLevel[rx][ry + 2] == Contents.GOAL)|| (curLevel[rx][ry + 1] == Contents.BOXONGOAL && curLevel[rx][ry + 2] == Contents.GOAL))  
            { return true; }  
            else {return false;}   
            case LEFT:  
            if(curLevel[rx - 1][ry] == Contents.EMPTY ||   
            curLevel[rx - 1][ry] == Contents.GOAL || (curLevel[rx - 1][ry] == Contents.BOX && curLevel[rx-2][ry] == Contents.EMPTY)|| (curLevel[rx - 1][ry] == Contents.BOXONGOAL && curLevel[rx-2][ry] == Contents.EMPTY)|| (curLevel[rx - 1][ry] == Contents.BOX && curLevel[rx-2][ry] == Contents.GOAL)|| (curLevel[rx - 1][ry] == Contents.BOXONGOAL && curLevel[rx-2][ry] == Contents.GOAL)) { return true; }  
            else {return false;}  
            case RIGHT:  
            if(curLevel[rx + 1][ry] == Contents.EMPTY ||   
            curLevel[rx + 1][ry] == Contents.GOAL || (curLevel[rx+1][ry] == Contents.BOX && curLevel[rx+2][ry] == Contents.EMPTY)|| (curLevel[rx+1][ry] == Contents.BOXONGOAL && curLevel[rx+2][ry] == Contents.EMPTY)|| (curLevel[rx+1][ry] == Contents.BOX && curLevel[rx+2][ry] == Contents.GOAL)|| (curLevel[rx+1][ry] == Contents.BOXONGOAL && curLevel[rx+2][ry] == Contents.GOAL)) { return true; }  
            else {return false;}  
        }  
        return true;  
    }  

       public static void main(String[] args){  
        JFrame f = new JFrame("Sokoban!");
	
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  
        f.setLayout(new FlowLayout());  
	//	f.add(label = new JLabel("Instructions: "));
	//	f.add(label = new JLabel("Add "));
        f.add(new Sokoban("m1.txt"));  
        f.pack();  
        f.setVisible(true);  
    }  
}


  
