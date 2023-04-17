package org.example;

public class Quiet extends Thread{
    Child child;

    Quiet(Child child){
        this.child = child;
    }

    @Override
    public void run(){
        while (true){
            child.remainingQuiet(this.getName());
        }
    }

}