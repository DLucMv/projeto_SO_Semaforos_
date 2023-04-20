package org.example;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;


public class Playground extends JFrame{

    private int automaticId = 0;
    private Semaphore basket;

    public Playground(){
        super("Playground");

        int b = Integer.parseInt(JOptionPane.showInputDialog("Capacidade do cesto: "));
        basket = new Semaphore(b);

        Janela.janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Janela.janela.setSize(1200, 720);
        Janela.janela.setLayout(new GridLayout(3,1));//Dividi a janela em 3 linhas

        Janela.log.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        Janela.log.setPreferredSize(new Dimension(500, 500));
        Janela.log.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Log"),
                                BorderFactory.createEmptyBorder(5,5,5,5)),
                        Janela.log.getBorder()));

        JPanel painel = new  JPanel();

        painel.setLayout(new BoxLayout(painel,BoxLayout.PAGE_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        Janela.logTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(Box.createVerticalStrut(10));
        painel.add(Janela.log);
        painel.add(Box.createVerticalStrut(5));
        painel.add(Box.createVerticalStrut(5));
        Janela.painelDasCrianças.setLayout(new GridLayout(2,5)); //2 crianças x coluna


        //Cria um botão para instanciar as crianças
        JPanel painelDoBotao = new JPanel();
        JButton createChildButton = new JButton("Criar criança");

        JLabel childPlayingTimeLabel = new JLabel("Tempo de brincadeira: ");
        painelDoBotao.add(childPlayingTimeLabel);
        JTextField childPlayingTimeTextField = new JTextField(2);
        painelDoBotao.add(childPlayingTimeTextField);

        JLabel childQuietTimeLabel = new JLabel("Tempo de estudo: ");
        painelDoBotao.add(childQuietTimeLabel);
        JTextField childQuietTimeTextField = new JTextField(2);
        painelDoBotao.add(childQuietTimeTextField);

        JLabel childHasBallLabel = new JLabel("A criança possui bola? ");
        painelDoBotao.add(childHasBallLabel);
        JTextField childHasBallTextField = new JTextField(1);
        painelDoBotao.add(childHasBallTextField);

        createChildButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                automaticId++;
                Integer id = automaticId; //Integer.valueOf(childIdTextField.getText());
                Integer playingTime = Integer.valueOf(childPlayingTimeTextField.getText());
                Integer quietTime = Integer.valueOf(childQuietTimeTextField.getText());
                String hasBall = childHasBallTextField.getText();
                createChild(id, playingTime, quietTime, hasBall);
                Janela.logTextArea.append("Criança "+ id + " criada." + "\n");
            }
        });
        painelDoBotao.add(createChildButton);


        Janela.janela.add(Janela.painel);
        Janela.painel.setLayout(new BoxLayout(Janela.painel, 1));
        Janela.painel.add(painelDoBotao); //linha 1
        Janela.janela.add(Janela.painelDasCrianças); // linha 2
        Janela.janela.add(painel); //linha 3



        Janela.painel.setVisible(true);
        Janela.painelDasCrianças.setVisible(true);
        Janela.janela.setVisible(true);


    }

    private void createChild(Integer id, Integer playingTime, Integer quietTime, @NotNull String hasBall) {

        boolean hb = false;
        char letra = 's';
        if(hasBall.charAt(0) == letra) {hb = true;}
        Child child = new Child(id, playingTime, quietTime, hb, basket);
        Thread thread = new Thread(child);
        thread.start();

    }

}
