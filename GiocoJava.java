import java.io.*;
import java.util.Scanner;
import javax.swing.*;       
import java.awt.*;             
import java.awt.event.*;        
import java.util.Random;


public class GiocoJava extends JPanel implements ActionListener, KeyListener {

    private int playerX = 350;      
    private int playerY = 250;      
    private int puntoX, puntoY;     
    private int score = 0;    
    private int highScore = 0;      

    private Timer timer;            
    private Random rand = new Random();     
    //COSTRUTTORE
    public GiocoJava(){
        highScore = caricaRecord();
        timer = new Timer(15, this);        
        timer.start();

        addKeyListener(this);
        setFocusable(true);

        rigeneraPunto();
    }
    //Creazione file e salvataggio
    private void salvaRecord(){
        try {
            FileWriter writer = new FileWriter("record.txt");
            writer.write("" + score);
            writer.close();
        } catch (IOException e) {
            System.out.println("Errore nel salvataggio!");
        }
    }

    //Carica record
private int caricaRecord(){
    try {
        File f = new File("record.txt");
        if(!f.exists()) return 0; // Se il file non esiste ancora
        Scanner lettore = new Scanner(f);
        int r = lettore.nextInt();
        lettore.close(); 
        return r;
    } catch (Exception e){
        return 0;
    }
}
    private void rigeneraPunto(){
        // Genera coordinate casuali stando un po' lontani dai bordi
        puntoX = rand.nextInt(700) + 20; 
        puntoY = rand.nextInt(500) + 20; 
    }

@Override
protected void paintComponent(Graphics g){
    super.paintComponent(g);

    // 1. DISEGNA LO SFONDO
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, 800, 600);

    // 2. IMPOSTA IL COLORE PER I TESTI PRINCIPALI
    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.BOLD, 20)); // Font grande per i punti
    
    g.drawString("Punti: " + score, 20, 30);
    g.drawString("Record: " + highScore, 20, 60);

    // 3. DISEGNA GLI OGGETTI
    g.setColor(Color.CYAN);
    g.fillRect(playerX, playerY, 30, 30);

    g.setColor(Color.RED);
    g.fillOval(puntoX, puntoY, 20, 20);

    // 4. DISEGNA I CREDITI (In piccolo e grigio)
    g.setFont(new Font("Arial", Font.ITALIC, 12)); 
    g.setColor(Color.GRAY); 
    g.drawString("made by adilas2710 & Gemini", 600, 550);
}

    @Override
    public void actionPerformed(ActionEvent e) {
        // Logica di collisione
        if (Math.abs(playerX - puntoX) < 30 && Math.abs(playerY - puntoY) < 30){
            score ++;       
            rigeneraPunto();    

            if (score > highScore){
                highScore = score;
                salvaRecord();
            }
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
