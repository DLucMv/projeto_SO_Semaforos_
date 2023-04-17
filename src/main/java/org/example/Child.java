package org.example;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Semaphore;

public class Child extends Thread{
    private int id;
    private int playingTime;
    private int quietTime;
    private boolean hasBall;
    private boolean isPlaying;
    private Semaphore mutex, playing, quiet; //playing = empty && quiet = full

    public Child(int id, int playingTime, int quietTime, boolean hasBall, Semaphore mutex, Semaphore playing, Semaphore quiet) {
        this.id = id;
        this.playingTime = playingTime;
        this.quietTime = quietTime;
        this.hasBall = hasBall;
        this.mutex = mutex;
        this.playing = playing;
        this.quiet = quiet;
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

    public Semaphore getPlaying() {
        return playing;
    }

    public Semaphore getQuiet() {
        return quiet;
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

    public int remainingQuiet(String idThread){
        try{
            quiet.acquire();
            mutex.acquire();
            this.hasBall = true;
            System.out.println("Child(" + idThread + ") " + this.id + " is NOT playing with a ball.");
            Playground2.log("Child(" + idThread + ") " + this.id + " is NOT playing with a ball.");
            wait(this.quietTime);
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            mutex.release();
            playing.release();
        }
        return this.id;
    }

    public int playingBall(String idThread){
        try{
            playing.acquire();
            mutex.acquire();
            this.hasBall = false;
            System.out.println("Child(" + idThread + ") " + this.id + " is playing with a ball.");
            Playground2.log("Child(" + idThread + ") " + this.id + " is playing with a ball.");
            wait(this.playingTime);
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            mutex.release();
            quiet.release();
        }
        return this.id;
    }





}
