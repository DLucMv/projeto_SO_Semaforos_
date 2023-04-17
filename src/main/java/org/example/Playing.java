package org.example;

public class Playing extends Thread{
    Child child;

    Playing(Child child){
        this.child = child;
    }

    @Override
    public void run(){
        while (true){
            child.playingBall(this.getName());
        }
    }

}
