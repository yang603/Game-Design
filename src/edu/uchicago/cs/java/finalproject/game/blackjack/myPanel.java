package edu.uchicago.cs.java.finalproject.game.blackjack;

import edu.uchicago.cs.java.finalproject.game.model.CommandCenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ethan on 11/16/2014.
 */
public class myPanel extends JFrame {

    private JTextField Jtf_player_add;
    private JButton Jb_add;
    private JButton Jb_player_hit;
    private JButton Jb_player_stand;
    private JButton Jb_player_deal;
    private JButton Jb_player_exit;
    private JPanel MyPanel;
    private JPanel Jp_player;
    private JLabel Jl_player_bet;
    private JLabel Jl_player_account;
    private JPanel Jp_dealer;
    private JLabel Jl_dealer_account;
    private JLabel Jl_dealer_bet;
    private JPanel Jp_dealer_cards;
    private JPanel Jp_player_cards;
    private JTextArea Jta_game_record;
    private JPanel Jp_info;
    private Poker card;
    private Random ran;

    int[] dealer=new int[2];
    int[] player=new int[2];
    int count=0;
    int stand=0;
    ArrayList<Integer> dcards = new ArrayList<Integer>();
    ArrayList<Integer> pcards = new ArrayList<Integer>();


//    public static void main(String[] args) {
//        myPanel mp = new myPanel();
//    }

    public myPanel() {
        JOptionPane.showConfirmDialog(MyPanel,"                                        !!!Welcome to Interstellar Casino!!!"+
                "\r\n                                          !!!Please read my rule carefully!!!"+
                "\r\n1.If you lose all your money or try to skip this Casino, your ship will belong to me!!!" +
                "\r\n2.If you win the all my money, i will get your three more life to get my moeny back!");
        card = new Poker();
        createPanel();
        this.add(MyPanel);
        this.setSize(1380, 720);
        this.setLocation(0,0);
        this.setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void createPanel() {
        createDealer();
        createPlayer();
        createCardsInfo();
        MyPanel = new JPanel();
        MyPanel.setLayout(new BorderLayout());
        MyPanel.add(Jp_dealer, BorderLayout.NORTH);
        MyPanel.add(Jp_info, BorderLayout.CENTER);
        MyPanel.add(Jp_player, BorderLayout.SOUTH);
    }


    private void createDealer() {
        Jp_dealer = new JPanel();
        Jp_dealer.setLayout(new GridLayout(1, 2));
        Jl_dealer_account = new JLabel("Account: 1000 $");
        Jl_dealer_bet = new JLabel("Bet: 0 $");
        Jp_dealer.add(Jl_dealer_account);
        Jp_dealer.add(Jl_dealer_bet);
        Jp_dealer.setBorder(BorderFactory.createTitledBorder("Dealer"));
    }

    private void createPlayer() {
        mylistener ml = new mylistener();
        Jp_player = new JPanel();
        Jp_player.setLayout(new GridLayout(4, 2));

        Jl_player_account = new JLabel("Account: 1000 $");
        Jl_player_bet = new JLabel("Bet: 0 $");
        Jtf_player_add = new JTextField();

        Jb_add = new JButton("Add Money");
        Jb_add.addActionListener(ml);
        Jb_add.setActionCommand("Add");

        Jb_player_hit = new JButton("Hit");
        Jb_player_hit.addActionListener(ml);
        Jb_player_hit.setActionCommand("Hit");

        Jb_player_stand = new JButton("Stand");
        Jb_player_stand.addActionListener(ml);
        Jb_player_stand.setActionCommand("Stand");

        Jb_player_deal = new JButton("Deal");
        Jb_player_deal.addActionListener(ml);
        Jb_player_deal.setActionCommand("Deal");

        Jb_player_exit = new JButton("Exit");
        Jb_player_exit.addActionListener(ml);
        Jb_player_exit.setActionCommand("Exit");

        Jp_player.add(Jl_player_account);
        Jp_player.add(Jl_player_bet);
        Jp_player.add(Jtf_player_add);
        Jp_player.add(Jb_add);
        Jp_player.add(Jb_player_hit);
        Jp_player.add(Jb_player_stand);
        Jp_player.add(Jb_player_deal);
        Jp_player.add(Jb_player_exit);
        Jp_player.setBorder(BorderFactory.createTitledBorder("Player"));
    }


    private void createCardsInfo() {
        Jp_info = new JPanel();
        Jp_info.setLayout(new GridLayout(1, 3));
        Jp_dealer_cards = new JPanel();
        Jp_dealer_cards.setBorder(BorderFactory.createTitledBorder("Dealer's cards"));
        Jp_player_cards = new JPanel();
        Jp_player_cards.setBorder(BorderFactory.createTitledBorder("player's cards"));
        Jta_game_record = new JTextArea();
        Jta_game_record.setText("Are you ready? \r\nPlease press Deal to start your adventure!");
        JScrollPane Jsp_game_record = new JScrollPane(Jta_game_record);
        Jp_info.add(Jp_dealer_cards);
        Jp_info.add(Jsp_game_record);
        Jp_info.add(Jp_player_cards);
        Jp_info.setBorder(BorderFactory.createTitledBorder("Information"));
    }


    class mylistener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Add")) {
                if(count==0) {
                    double money = Double.parseDouble(Jtf_player_add.getText());
                    String[] account = Jl_player_account.getText().split(" ");
                    double budget = Double.parseDouble(account[1]);
                    if (money <= 0) {
                        Jta_game_record.append("\r\nInvalid input, please try again!");
                        Jl_player_bet.setText("Bet: 0 $");
                    } else if (budget < money) {
                        Jta_game_record.append("\r\nNot enough money");
                        Jl_player_bet.setText("Bet: 0 $");
                    } else {
                        Jl_player_bet.setText("Bet: " + money + " $");
                        Jl_player_account.setText("Account: " + (budget - money) + " $");
                    }
                    Jtf_player_add.setText("");
                    Jtf_player_add.updateUI();
                    Jl_player_bet.updateUI();
                    Jl_player_account.updateUI();
                }
            }else if (e.getActionCommand().equals("Hit")){
                if(count!=0) {
                    ran = new Random();
                    int i =1+ ran.nextInt(51);
                    while(card.cardsNum.get(i)==0){//6 deck shoe limits
                        i=1+ ran.nextInt(51);
                    }
                    JLabel picLabel = new JLabel(new ImageIcon(card.cards.get(i)));
                    pcards.add(i);
                    card.cardsNum.put(i,card.cardsNum.get(i)-1);
                    Jp_player_cards.add(picLabel);
                    Jp_player_cards.updateUI();
                    count++;
                    cardPoints(i, "player");
                    isWin();
                }else{
                    Jta_game_record.append("\r\nPlease press Deal to start game!");
                }

            }else if (e.getActionCommand().equals("Stand")){
                if(count!=0) {
                    ran = new Random();
                    int min=0;
                    while (min<=18) {
                        int j = 1 + ran.nextInt(51);
                        while(card.cardsNum.get(j)==0){//6 deck shoe limits
                            j=1+ ran.nextInt(51);
                        }
                        dcards.add(j);
                        card.cardsNum.put(j,card.cardsNum.get(j)-1);
                        JLabel picLabel = new JLabel(new ImageIcon(card.cards.get(j)));
                        Jp_dealer_cards.add(picLabel);
                        Jp_dealer_cards.updateUI();
                        cardPoints(j, "dealer");
                        min = Math.min(dealer[0],dealer[1]);
                        System.out.println(min);
                    }
                    count++;
                    stand=1;
                    isWin();
                }else{
                    Jta_game_record.append("\r\nPlease add money and press Deal to start game!");
                }
            }else if (e.getActionCommand().equals("Deal")){
                if(count!=0){
                    Jta_game_record.append("\r\nYou can not quit the current game!");
                }else {
                    Jp_player_cards.removeAll();
                    Jp_dealer_cards.removeAll();
                    String[] account = Jl_player_bet.getText().split(" ");
                    double money = Double.parseDouble(account[1]);
                    if (money<=0){
                        Jta_game_record.append("\r\nInvalid input, please add money and try again!");
                        Jl_player_bet.setText("Bet: 0 $");
                    }else {
                        count++;
                        newGame();
                    }
                }
            }else if (e.getActionCommand().equals("Exit")){
                JOptionPane.showConfirmDialog(MyPanel,"You coward! You lose you ship!!!");
                CommandCenter.setNumFalcons1(0);
                CommandCenter.setPaused(!CommandCenter.isPaused());
                quit();
            }
        }
    }

    public void quit(){
        this.dispose();
    }

    public void newGame(){
        for(int i=0;i<2;i++) {
            ran = new Random();
            int j=1+ran.nextInt(51);
            while(card.cardsNum.get(j)==0){//6 deck shoe limits
                j=1+ ran.nextInt(51);
            }
            JLabel picLabel = new JLabel(new ImageIcon(card.cards.get(j)));
            pcards.add(j);
            card.cardsNum.put(j,card.cardsNum.get(j)-1);
            Jp_player_cards.add(picLabel);
            Jp_player_cards.updateUI();
            cardPoints(j, "player");

            if(i==1){
                j=1+ran.nextInt(51);
                while(card.cardsNum.get(j)==0){//6 deck shoe limits
                    j=1+ ran.nextInt(51);
                }
                dcards.add(j);
                card.cardsNum.put(j,card.cardsNum.get(j)-1);
                picLabel = new JLabel(new ImageIcon(card.cards.get(53)));
                Jp_dealer_cards.add(picLabel);
                cardPoints(j,"dealer");
            }else {
                String[] account = Jl_dealer_account.getText().split(" ");
                double budget = Double.parseDouble(account[1]);
                double money=0;
                if(budget<100) {
                    money = budget;
                }else{
                    money = ran.nextInt((int) budget - (int) (budget * 0.3));
                }

                Jl_dealer_bet.setText("Bet: " + money + " $");
                Jl_dealer_account.setText("Account: "+(budget-money)+" $");

                Jl_dealer_bet.updateUI();
                Jl_dealer_account.updateUI();

                j=1+ran.nextInt(51);
                while(card.cardsNum.get(j)==0){//6 deck shoe limits
                    j=1+ ran.nextInt(51);
                }
                picLabel = new JLabel(new ImageIcon(card.cards.get(j)));
                dcards.add(j);
                card.cardsNum.put(j,card.cardsNum.get(j)-1);
                Jp_dealer_cards.add(picLabel);
                Jp_dealer_cards.updateUI();
                cardPoints(j,"dealer");
            }
        }

        isWin();

    }

    public void cardPoints(int points, String who) {
        int output = points % 13;
        if (output == 1) {
            if (who.equals("player")) {
                player[0] = player[0] + 1;
                player[1] = player[1] + 11;
            } else {
                dealer[0] = dealer[0] + 1;
                dealer[1] = dealer[1] + 11;
            }
        } else if (output <= 10&&output>0) {
            if (who.equals("player")) {
                player[0] = player[0] + output;
                player[1] = player[1] + output;
            } else {
                dealer[0] = dealer[0] + output;
                dealer[1] = dealer[1] + output;
            }
        } else if (output > 10||output==0) {
            if (who.equals("player")) {
                player[0] = player[0] + 10;
                player[1] = player[1] + 10;
            } else {
                dealer[0] = dealer[0] + 10;
                dealer[1] = dealer[1] + 10;
            }
        }
    }

    public void isWin(){
//        Jta_game_record.append("\r\nPlayer soft hand"+player[0]+"\r\nPlayer hard hand"+player[1]);
//        Jta_game_record.append("\r\nDealer current known soft hand"+dealer[0]+"\r\nDealer current known hard hand"+dealer[1]);
        if(count==1){
            if (player[1]==21&&dealer[1]!=21) {
                Jta_game_record.append("\r\nBlackJack! player Win!");
                cleanMoney("player");
                count = 0;
                stand=0;

            } else if (player[1]!=21&&dealer[1]==21) {
                Jta_game_record.append("\r\nBlackJack! Dealer Win!");
                cleanMoney("dealer");
                count = 0;
                stand=0;

            } else if (player[1]==21&&dealer[1]==21) {
                Jta_game_record.append("\r\nBlackJack! Tied!");
                cleanMoney("tie");
                count = 0;
                stand=0;
            }
        }else if(count>1&&stand!=1){
            if (player[0] > 21 && player[1] > 21) {
                Jta_game_record.append("\r\nPlayer bust!\r\nDealer Win!");
                cleanMoney("dealer");
                count = 0;
                stand=0;

            } else if (dealer[0] > 21 && dealer[1] > 21) {
                Jta_game_record.append("\r\nDealer bust!\r\nPlayer Win!");
                cleanMoney("player");
                count = 0;
                stand=0;
            }

        } else if(stand==1){
            if (player[0] > 21) {
                Jta_game_record.append("\r\nPlayer bust!\r\nDealer Win!");
                cleanMoney("dealer");
                count = 0;
                stand=0;

            } else if (dealer[0] > 21) {
                Jta_game_record.append("\r\nDealer bust!\r\nPlayer Win!");
                cleanMoney("player");
                count = 0;
                stand=0;
            }

            int pvalue = player[1];
            if(player[1]>21){
                pvalue = player[0];
            }

            int dvalue = dealer[1];
            if(dealer[1]>21){
                dvalue = dealer[0];
            }

            if (pvalue > dvalue) {
                Jta_game_record.append("\r\nplayer Win!");
                cleanMoney("player");
                count = 0;
                stand=0;

            } else if (pvalue < dvalue) {
                Jta_game_record.append("\r\nDealer Win!");
                cleanMoney("dealer");
                count = 0;
                stand=0;

            } else if (pvalue == dvalue&&pvalue!=0&&dvalue!=0) {

                Jta_game_record.append("\r\nTied!");
                cleanMoney("tie");
                count = 0;
                stand=0;
            }
        }


    }

    public void cleanMoney(String iswho){
        if(iswho.equals("player")){
            String[] dealer_bet = Jl_dealer_bet.getText().split(" ");
            String[] player_bet = Jl_player_bet.getText().split(" ");
            String[] account = Jl_player_account.getText().split(" ");
            double reward = Double.parseDouble(account[1])+ Double.parseDouble(player_bet[1]) +Double.parseDouble(dealer_bet[1]) ;
            Jl_player_bet.setText("Bet: " + 0 + " $");
            Jl_player_account.setText("Account: " + reward + " $");
            Jl_dealer_bet.setText("Bet: " + 0 + " $");
            Jl_player_bet.updateUI();
            Jl_player_account.updateUI();
            Jl_dealer_bet.updateUI();

            Jp_player_cards.removeAll();
            Jp_dealer_cards.removeAll();

        }else  if(iswho.equals("dealer")){
            String[] dealer_bet = Jl_dealer_bet.getText().split(" ");
            String[] player_bet = Jl_player_bet.getText().split(" ");
            String[] account = Jl_dealer_account.getText().split(" ");
            double reward = Double.parseDouble(account[1])+ Double.parseDouble(player_bet[1]) +Double.parseDouble(dealer_bet[1]) ;
            Jl_player_bet.setText("Bet: " + 0 + " $");
            Jl_dealer_account.setText("Account: " + reward + " $");
            Jl_dealer_bet.setText("Bet: " + 0 + " $");
            Jl_player_bet.updateUI();
            Jl_player_account.updateUI();
            Jl_dealer_bet.updateUI();

            Jp_player_cards.removeAll();
            Jp_dealer_cards.removeAll();

        }
        else  if(iswho.equals("tie")){
            String[] dealer_bet = Jl_dealer_bet.getText().split(" ");
            String[] player_bet = Jl_player_bet.getText().split(" ");
            String[] daccount = Jl_dealer_account.getText().split(" ");
            String[] paccount = Jl_player_account.getText().split(" ");
            double dreturn = Double.parseDouble(daccount[1])+ Double.parseDouble(dealer_bet[1]) ;
            double preturn = Double.parseDouble(paccount[1])+ Double.parseDouble(player_bet[1]) ;

            Jl_dealer_account.setText("Account: " + dreturn + " $");
            Jl_dealer_bet.setText("Bet: " + 0 + " $");
            Jl_player_account.setText("Account: " + preturn + " $");
            Jl_player_bet.setText("Bet: " + 0 + " $");
            Jl_dealer_bet.updateUI();
            Jl_dealer_account.updateUI();
            Jl_player_bet.updateUI();
            Jl_player_account.updateUI();

            Jp_player_cards.removeAll();
            Jp_dealer_cards.removeAll();

        }

        Jta_game_record.append("\r\nPlayer soft hand"+player[0]+"\r\nPlayer hard hand"+player[1]);
        Jta_game_record.append("\r\nDealer current known soft hand"+dealer[0]+"\r\nDealer current known hard hand"+dealer[1]);
        player[0]=0;
        player[1]=0;
        dealer[0]=0;
        dealer[1]=0;
        Jp_player_cards.removeAll();
        Jp_dealer_cards.removeAll();
        Jp_player_cards.updateUI();
        Jp_dealer_cards.updateUI();
        for(int item : pcards){
            JLabel pic = new JLabel(new ImageIcon(card.cards.get(item)));
            Jp_player_cards.add(pic);
            Jp_player_cards.updateUI();
        }
        for(int item : dcards){
            JLabel pic = new JLabel(new ImageIcon(card.cards.get(item)));
            Jp_dealer_cards.add(pic);
            Jp_dealer_cards.updateUI();
        }
        pcards.clear();
        dcards.clear();
        checkbudget();
    }

    public void checkbudget(){

        String[] dealeraccount = Jl_dealer_account.getText().split(" ");
        String[] playeraccount = Jl_player_account.getText().split(" ");
        double pa=Double.parseDouble(playeraccount[1]);
        double da=Double.parseDouble(dealeraccount[1]);

        if(pa==0){
            Jta_game_record.append("\r\nGame is over, player has no money to lose!");
            JOptionPane.showConfirmDialog(MyPanel,"Sorry! Your ship now belongs to me!");
            CommandCenter.setNumFalcons1(0);
            CommandCenter.setPaused(!CommandCenter.isPaused());
            this.dispose();
        }else if(da==0){
            Jta_game_record.append("\r\nGame is over, dealer has no money to lose!");
            JOptionPane.showConfirmDialog(MyPanel,"Ok! There is your bonus, your life +3 !");
            CommandCenter.setNumFalcons1(CommandCenter.getNumFalcons1()+3);
            if(CommandCenter.getFalcon2()!=null){
            CommandCenter.setNumFalcons2(CommandCenter.getNumFalcons2()+3);
            }
            CommandCenter.setPaused(!CommandCenter.isPaused());
            this.dispose();
        }

    }
}
