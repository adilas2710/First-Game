/*
This is a 2D game where the goal is to score as many points as possible 
by collecting red targets. The AI helped me significantly with debugging a
nd structuring the movement logic.

How it works: The game runs on a 60 FPS loop controlled by a Timer. 
The player moves a cyan square using the arrow keys; the code performs 
real-time collision detection by calculating the distance between the player
 and the target. When the X and Y coordinates of both objects overlap, 
 the score increases, and the target teleports to a new random location using 
 the Random class. Boundary checks have been implemented to keep the player within 
 the game area. */
import javax.swing.*;       
import java.awt.*;             
import java.awt.event.*;        
import java.util.Random;

// Ho rimosso "package java;" perché causava errore di permessi
public class GiocoJava extends JPanel implements ActionListener, KeyListener {

    private int playerX = 350;      
    private int playerY = 250;      
    private int puntoX, puntoY;     
    private int score = 0;          

    private Timer timer;            
    private Random rand = new Random();     

    public GiocoJava(){
        timer = new Timer(15, this);        
        timer.start();

        addKeyListener(this);
        setFocusable(true);

        rigeneraPunto();
    }

    private void rigeneraPunto(){
        // Genera coordinate casuali stando un po' lontani dai bordi
        puntoX = rand.nextInt(700) + 20; 
        puntoY = rand.nextInt(500) + 20; 
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        // Sfondo
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 600);

        // GIOCATORE - Usiamo playerX e playerY (Prima usavi puntoX!)
        g.setColor(Color.CYAN);
        g.fillRect(playerX, playerY, 30, 30);

        // OBIETTIVO
        g.setColor(Color.RED);
        g.fillOval(puntoX, puntoY, 20, 20);

        // Punteggio
        g.setColor(Color.WHITE);
        g.drawString("Punti: " + score, 20, 30);
        
        g.setFont(new Font("Arial", Font.ITALIC, 12)); 
        g.setColor(Color.GRAY); 
        g.drawString("made by adilas2710 & Gemini", 620, 550);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Logica di collisione
        if (Math.abs(playerX - puntoX) < 30 && Math.abs(playerY - puntoY) < 30){
            score ++;       
            rigeneraPunto();    
        }
        repaint();  
    }

    @Override
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();   
        if (key == KeyEvent.VK_LEFT && playerX > 0)  playerX -= 15;       
        if (key == KeyEvent.VK_RIGHT && playerX < 750) playerX += 15; 
        if (key == KeyEvent.VK_UP && playerY > 0)    playerY -= 15; 
        if (key == KeyEvent.VK_DOWN && playerY < 530)  playerY += 15; 
    }

    @Override public void keyTyped(KeyEvent e){}
    @Override public void keyReleased(KeyEvent e){}
    
    public static void main(String[] args){
        JFrame finestra = new JFrame("Benvenuti nel mio primo videogioco");
        GiocoJava gioco = new GiocoJava();
        
        // Usiamo "finestra" (il nome che hai scelto tu) coerentemente
        finestra.add(gioco);
        finestra.setSize(800, 600);
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.setLocationRelativeTo(null); // Opzionale: centra la finestra
        finestra.setVisible(true);
    }
}
