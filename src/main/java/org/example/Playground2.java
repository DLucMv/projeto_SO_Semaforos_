package org.example;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;

public class Playground2 extends JFrame {

    private static JTextArea logTextArea;
    //Botões
    private JButton createChildButton;
    private JButton setUpButton;

    //Imagens
    private Image fundo;

    //Valores de SeyUp
    private Semaphore mutex;
    private Semaphore playing;
    private Semaphore quiet;
    private int automaticId = 0;
    private int maxChildsPlaying;
    private int basketCapacity;

    public Playground2(){
        // Configurações básicas da janela
        super("Playground2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 720);
        setLayout(new GridLayout(1,3));
        setLocationRelativeTo(null);


        //Cria um log
        logTextArea = new JTextArea(20, 40); // 20 linhas e 40 colunas
        logTextArea.setEditable(false); // Desabilita edição
        JScrollPane scrollPane = new JScrollPane(logTextArea);


        //Cria um painel com o botão de SetUp
        JPanel painelDoSetUp = new JPanel();
        setUpButton = new JButton("SetUp Inicial");

        //Cria os compos do setUp inicial da aplicação
        JLabel maxChildPlaying = new JLabel("Numero máximo de crianças brincando: ");
        painelDoSetUp.add(maxChildPlaying);
        JTextField maxChildPlayingTextField = new JTextField(2);
        painelDoSetUp.add(maxChildPlayingTextField);

        JLabel basket = new JLabel("Capacidade do sexto: ");
        painelDoSetUp.add(basket);
        JTextField basketTextField = new JTextField(2);
        painelDoSetUp.add(basketTextField);

        setUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Integer maxChildsPlaying = Integer.valueOf(maxChildPlayingTextField.getText());
                Integer basketCapacity = Integer.valueOf(basketTextField.getText());
                setUp(maxChildsPlaying, basketCapacity);
                logTextArea.append("Parametros iniciais: \n"
                        + " Máximo de criaças brincando: " + maxChildsPlaying
                        + " Capacidade do cesto: " + basketCapacity + "\n");
            }
        });
        painelDoSetUp.add(setUpButton);


        // Cria um painel para colocar o botão de Criar Criança
        JPanel painelDoBotao = new JPanel();
        createChildButton = new JButton("Criar criança");

        //Cria os campos com os parâmetros das crianças
        /*JLabel childIdLabel = new JLabel("Id da criança: ");
        painelDoBotao.add(childIdLabel);
        JTextField childIdTextField = new JTextField(2);
        painelDoBotao.add(childIdTextField);*/

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
                logTextArea.append("Criança "+ id + " criada." + "\n");
            }
        });
        painelDoBotao.add(createChildButton);


        // Cria um painel para organizar os componentes
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.PAGE_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        painelPrincipal.add(Box.createVerticalStrut(10));
        painelPrincipal.add(Box.createVerticalStrut(5));
        //painelPrincipal.add(painelCampo, BorderLayout.CENTER);
        painelPrincipal.add(painelDoSetUp, BorderLayout.NORTH);
        painelPrincipal.add(painelDoBotao, BorderLayout.SOUTH);

        painelPrincipal.add(scrollPane, BorderLayout.CENTER);




        // Adiciona os componentes à janela
        getContentPane().add(painelPrincipal);

    }


    private void createChild(Integer id, Integer playingTime, Integer quietTime, @NotNull String hasBall) {
        boolean hb = false;
        char letra = 's';
        if(hasBall.charAt(0) == letra) {hb = true;}
        Child child = new Child(id, playingTime, quietTime, hb, quiet);
        Thread thread = new Thread(child);
        thread.start();

    }

    private void setUp(Integer basket, Integer maxChilds){
        mutex = new Semaphore(basket);
        playing = new Semaphore(maxChilds);
        quiet = new Semaphore(0);

        System.out.println("Tamanho do sexto: " + basket + "  Num max crian brincando:" + maxChilds);

    }


    /*public static void log(String message) {
        Playground2.logTextArea.append(message + "\n");
    }*/

}
