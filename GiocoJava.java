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
    private int score = 19;    
    private int highScore = 0;    
    private int velocita = 15;  
    private int nemicoX, nemicoY;
    private boolean nemicoAttivo = false;
    private int proiettileX, proiettileY;
    private boolean proiettileAttivo = false;
    private int velocitaProiettile = 10;

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
            writer.write("" + highScore); // Salviamo il record, non lo score attuale
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
            int r = 0;
            if(lettore.hasNextInt()){
                r = lettore.nextInt();
            }
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

        // DISEGNA IL NEMICO E IL PROIETTILE SE ATTIVI
        if(nemicoAttivo){
            g.setColor(Color.ORANGE);
            g.fillRect(nemicoX, nemicoY, 40, 40);
            
            if(proiettileAttivo){
                g.setColor(Color.YELLOW);
                g.fillOval(proiettileX, proiettileY, 10, 10);
            }
        }

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
            if(score == 10){
                velocita += 5;
            }
            if(score == 20){
                nemicoAttivo = true;
                nemicoX = 700;
                nemicoY = 300;
            }
        }

        // Logica gestione nemico e proiettile
        if(nemicoAttivo){
            if(!proiettileAttivo){
                proiettileX = nemicoX;
                proiettileY = nemicoY + 15;
                proiettileAttivo = true; // Attiviamo il proiettile
            }
        }

        //Movimento proiettile verso sinistra  
        if(proiettileAttivo){
            proiettileX -= velocitaProiettile;
            
            // Se il proiettile esce dallo schermo, lo resettiamo
            if (proiettileX < 0) {
                proiettileAttivo = false;
            }

            // CONTROLLO COLLISIONE: Se il proiettile colpisce il giocatore
            if (Math.abs(proiettileX - playerX) < 25 && Math.abs(proiettileY - playerY) < 25) {
                score = 0; // Hai perso! Reset punti
                nemicoAttivo = false;
                proiettileAttivo = false;
                velocita = 15; // Resettiamo anche la velocità
                System.out.println("Colpito! Game Over");
            }
            // CONTROLLO COLLISIONE: Giocatore contro Nemico
            if (nemicoAttivo) {
                if (Math.abs(playerX - nemicoX) < 35 && Math.abs(playerY - nemicoY) < 35) {
                    score = 0;           // Reset punti
                    nemicoAttivo = false; // Il nemico sparisce
                    velocita = 15;       // Reset velocità
                    System.out.println("Scontro frontale! Game Over");
                }
            }
        }
        
        repaint();  
    }

    @Override
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();   
        if (key == KeyEvent.VK_A && playerX > 0)  playerX -= velocita;       
        if (key == KeyEvent.VK_D && playerX < 750) playerX += velocita; 
        if (key == KeyEvent.VK_W && playerY > 0)    playerY -= velocita; 
        if (key == KeyEvent.VK_S && playerY < 530)  playerY += velocita; 
    }

    @Override public void keyTyped(KeyEvent e){}
    @Override public void keyReleased(KeyEvent e){}
    
    public static void main(String[] args){
        JFrame finestra = new JFrame("Benvenuti nel mio primo videogioco");
        GiocoJava gioco = new GiocoJava();
        
        finestra.add(gioco);
        finestra.setSize(800, 600);
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.setLocationRelativeTo(null); 
        finestra.setVisible(true);
    }
}
