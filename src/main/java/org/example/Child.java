package org.example;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Child extends Thread{
    private int id;
    private int playingTime;
    private int quietTime;
    //private int ball = 0;
    private boolean hasBall;
    private boolean isPlaying;
    private Semaphore mutex = new Semaphore(1);
    private Semaphore balls = new Semaphore(Basket.ballsInTheBasket);//full = 0 no inicio
    private final Semaphore basket;//empty = N no inicio

    private ImageIcon childPlaying = new ImageIcon("C:\\Users\\lucas\\IdeaProjects\\childsPlayRefatorado\\src\\main\\resources\\playing.png");
    //private ImageIcon childPlaying2 = new ImageIcon(getClass().getResource("cr2.png"));
    private ImageIcon childQuiet = new ImageIcon("C:\\Users\\lucas\\IdeaProjects\\childsPlayRefatorado\\src\\main\\resources\\quiet.jpg");
    //private ImageIcon childQuiet2 = new ImageIcon(getClass().getResource("cr_q2.png"));
    //private ImageIcon childWainting = new ImageIcon(getClass().getResource("cr_esp.png"));
    private ImageIcon basketImg = new ImageIcon("C:\\Users\\lucas\\IdeaProjects\\childsPlayRefatorado\\src\\main\\resources\\cesto.png");

    private JPanel panelChidlClass = new JPanel();

    public Child(int id, int playingTime, int quietTime, boolean hasBall, Semaphore basket) {
        this.id = id;
        this.playingTime = playingTime;
        this.quietTime = quietTime;
        this.hasBall = hasBall;
        //this.mutex = mutex;
        //this.balls = balls;
        this.basket = basket;
        this.isPlaying = false;
    }

    @Override
    public long getId() {
        return id;
    }

    public int getPlayingTime() {
        return playingTime;
    }

    public int getQuietTime() {
        return quietTime;
    }

    public boolean isHasBall() {
        return hasBall;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public Semaphore getMutex() {
        return mutex;
    }

    public Semaphore getBalls() {
        return balls;
    }

    public Semaphore getBasket() {
        return basket;
    }

    @Override
    public void run(){

        //Colocando o sexto na tela
        Basket.basketLabel.setText("Bolas no sexto: " + Basket.ballsInTheBasket);
        Basket.basketLabel.setIcon(basketImg);
        Basket.basketLabel.setSize(50,50);
        Janela.painel.add(Basket.basketLabel);
        Janela.painelDasCrianças.add(panelChidlClass);

        //Criando icones para crianças
        JLabel a = new JLabel();
        a.setBounds(10,10,100,100);
        a.setSize(50,50);

        JLabel b = new JLabel();
        b.setBounds(10,10,100,100);
        b.setSize(50,50);

        panelChidlClass.setLayout(new GridLayout(1,2));
        panelChidlClass.add(a);
        panelChidlClass.setVisible(true);



        while (true){
            try {
                if(hasBall){
                    setImagePlaying(a, panelChidlClass);
                    playWithBall();
                    removeImagePlaying(a, panelChidlClass);
                    setImageQuiet(a, panelChidlClass);
                } else{
                    setImageQuiet(a, panelChidlClass);
                    fetchBall();
                    setImagePlaying(a, panelChidlClass);
                    playWithBall();
                    removeImagePlaying(a, panelChidlClass);
                }
                setImageQuiet(a, panelChidlClass);
                rest();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public static void wait(int time) {
        LocalDateTime start = LocalDateTime.now();

        while(true) {
            LocalDateTime now = LocalDateTime.now();

            if(ChronoUnit.SECONDS.between(start, now) >= time) {
                return;
            }
        }

    }

    private void rest(){
        Janela.logTextArea.append("Child(" + id + ") " + " is NOT playing with a ball.\n");
        wait(this.quietTime);
    }

    private void catchBall() throws InterruptedException {
        Janela.logTextArea.append("Child(" + id + ") " + " took a ball from the basket.\n");
        if(Basket.ballsInTheBasket > 0){
            Basket.ballsInTheBasket -= 1;
        }
        Basket.basketLabel.setText("Bolas no sexto: " + Basket.ballsInTheBasket);
        balls.acquireUninterruptibly();
        hasBall = true;
    }

    private void dropBall() throws InterruptedException {


        if(balls.availablePermits() == 0){
            mutex.acquireUninterruptibly();
            balls.acquire();

            Janela.logTextArea.append("Child(" + id + ") " + " The basket is full. Waiting to drop the ball.\n");
            //dropBall();
        } else {
            Janela.logTextArea.append("Child(" + id + ") " + " dropped the ball into the basket.\n");
            Basket.ballsInTheBasket += 1;
            Basket.basketLabel.setText("Bolas no sexto: " + Basket.ballsInTheBasket);
        }
        hasBall = false;
        balls.release();
        mutex.release();


    }

    private void playWithBall() throws InterruptedException {
        balls.release();
        Janela.logTextArea.append("Child(" + id + ") " + " is playing with a ball.\n");
        wait(playingTime);
        Janela.logTextArea.append("Child(" + id + ") " + " finished playing.\n");
        dropBall();
        //hasBall = false;
    }

    private void fetchBall() throws InterruptedException {
        Janela.logTextArea.append("Child(" + id + ") " + " looking for ball.\n");


        if(balls.availablePermits() == 0){
            basket.acquire();
            mutex.acquire();
            Janela.logTextArea.append("Child(" + id + ") " + " The basket is empty. Waiting for some ball to play.\n");
        }else{
            catchBall();
            mutex.release();
        }


    }

    private void setImagePlaying(JLabel a, JPanel cp){
        a.setIcon(childPlaying);
        cp.add(a);
        cp.validate();
        cp.repaint();
        Janela.janela.validate();
        Janela.janela.repaint();
    }

    private void removeImagePlaying(JLabel a, JPanel cp){
        a.setIcon(childPlaying);
        cp.remove(a);
        cp.validate();
        cp.repaint();
        Janela.janela.validate();
        Janela.janela.repaint();
    }

    private void setImageQuiet(JLabel a, JPanel cp){
        a.setIcon(childQuiet);
        cp.add(a);
        cp.validate();
        cp.repaint();
        Janela.janela.validate();
        Janela.janela.repaint();
    }

    private void removeImageQuiet(JLabel a, JPanel cp){
        a.setIcon(childQuiet);
        cp.remove(a);
        cp.validate();
        cp.repaint();
        Janela.janela.validate();
        Janela.janela.repaint();
    }


    /*public int remainingQuiet(String idThread){
        try{
            basket.acquire();
            mutex.acquire();
            this.hasBall = true;
            System.out.println("Child(" + idThread + ") " + this.id + " is NOT playing with a ball.");
            Playground2.log("Child(" + idThread + ") " + this.id + " is NOT playing with a ball.");
            wait(this.quietTime);
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            mutex.release();
            balls.release();
        }
        return this.id;
    }

    public int playingBall(String idThread){
        try{
            balls.acquire();
            mutex.acquire();
            this.hasBall = false;
            System.out.println("Child(" + idThread + ") " + this.id + " is playing with a ball.");
            Playground2.log("Child(" + idThread + ") " + this.id + " is playing with a ball.");
            wait(this.playingTime);
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            mutex.release();
            basket.release();
        }
        return this.id;
    }*/





}
