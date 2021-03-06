import games.components.WorldPanel;
import games.objects.Sprite;
import games.objects.SpriteEvent;
import games.objects.World;
import games.utils.RandomNumber;
import games.utils.SpriteListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Nikita on 2/18/14.
 */
public class gameWindow implements ActionListener{
    gameDataParser parser = new gameDataParser();
    gameDataExporter exporter = new gameDataExporter();
    WorldPanel viewPort;
    World startworld;
    World world;
    World death;
    Image startt;
    Image mainworld;
    Image deathh;
    Image pipeup;
    Image pipedown;
    Image coinmage;
    Sprite bird;
    Sprite pd1;
    Sprite pd2;
    Sprite pd3;
    Sprite pu1;
    Sprite pu2;
    Sprite pu3;
    Sprite coin;
    JPanel controls;
    JButton play;
    JButton store;
    JButton options;
    JButton showinfo;
    JLabel scorelabel;
    JTextField score;
    JFrame frame;
    final int pipeDistance = 180;
    final int pipeMinHeight = -150;
    final int pipeMaxHeight = 1;
    final int pipeHeightDistance = 344;
    int coinink = 1;
    int highscore = 0;
    int coinses = 0;
    boolean rising = false;
    boolean playing = false;
    boolean p1hit = false;
    boolean p2hit = false;
    boolean p3hit = false;
    //Store begin
    JDialog storeMenu;
    JPanel lists;
    JList<String> storelist;
    JScrollPane stlister;
    DefaultListModel stmodel = new DefaultListModel();
    JList<String> bought;
    JScrollPane blister;
    DefaultListModel bmodel = new DefaultListModel();
    JPanel buttons;
    JTextField money;
    JButton buy;
    JButton use;
    //Store end
    int scorenum = 0;
    int coinnum = 0;
    final int PipeX = 350;
    final int downPipeY = 345;
    int actgm = 0;
    ArrayList<Integer> storeunlocks;
    ArrayList<String> birdpaths;
    String birdpath;
    void init() throws IOException{
        lists = new JPanel(new GridLayout(1,2));
        lists.setSize(390,240);
        lists.setPreferredSize(new Dimension(390, 240));
        buttons = new JPanel(new GridLayout(1,3));
        buttons.setSize(390,30);
        buttons.setPreferredSize(new Dimension(390, 30));
        storeMenu = new JDialog(frame,"Store",true);
        storeMenu.setResizable(false);
        storeMenu.setSize(400, 305);
        storeMenu.setLayout(new FlowLayout());
        storelist = new JList<String>();
        stlister = new JScrollPane(storelist);
        bought = new JList<String>();
        blister = new JScrollPane(bought);
        bought.setSelectionModel(new DisabledItemSelectionModel());
        money = new JTextField("0 Coins");
        money.setEditable(false);
        buy = new JButton("Show description");
        buy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> stitems = new ArrayList<String>(Arrays.asList(parser.getStoreitems()));
                int storeindex = stitems.indexOf(storelist.getSelectedValue());
                if (storeindex == -1){
                    JOptionPane.showMessageDialog(null,"Choose something!");
                    return;
                }
                int cost = parser.getStoreCosts()[storeindex];
                int reqlevel = parser.getLevelRequrements()[storeindex];
                String description = parser.getDescriptions()[storeindex];
                int option = JOptionPane.showConfirmDialog(null,description+"\n-----------------------------\n                 Buy?","Description",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if (option == JOptionPane.NO_OPTION)return;
                if (reqlevel > highscore){
                    JOptionPane.showMessageDialog(null,"Low high score");
                }
                if (cost>coinses){
                    JOptionPane.showMessageDialog(null,"Low funds");
                    return;
                }
                bmodel.addElement(stmodel.get(storelist.getSelectedIndex()));
                stmodel.remove(storelist.getSelectedIndex());
                storeunlocks.add(storeindex);
                coinses = coinses - cost;
                money.setText(coinses+"");
            }
        });
        lists.add(stlister);
        lists.add(blister);
        buttons.add(buy);
        buttons.add(money);
        storeMenu.add(lists);
        storeMenu.add(buttons);
        parser.getFile();
        highscore = parser.getHighScore();
        coinses = parser.getCoins();
        actgm = parser.getGamemode();
        storeunlocks = parser.getUnlocked();
        birdpath = parser.getBirdpath();
        birdpaths = parser.getBirds();
        coinmage = ImageIO.read(this.getClass().getResourceAsStream("world/coin.gif"));
        coin = new Sprite(PipeX,320,25,35,0,coinmage,"Coin",true);
        pipeup = ImageIO.read(this.getClass().getResourceAsStream("world/pipeup.gif"));
        pipedown = ImageIO.read(this.getClass().getResourceAsStream("world/pipedown.gif"));
        mainworld = ImageIO.read(this.getClass().getResourceAsStream("world/birdplay.gif"));
        startt = ImageIO.read(this.getClass().getResourceAsStream("world/flappybirdstart.gif"));
        deathh = ImageIO.read(this.getClass().getResourceAsStream("world/death.gif"));
        pd1 = new Sprite(PipeX,downPipeY,pipedown.getWidth(null),pipedown.getHeight(null),0,pipedown,"Down Pipe",true);
        pd2 = new Sprite((int)pd1.getLocation().getX()+pipeDistance,(int)pd1.getLocation().getY(),(int)pd1.getSize().getWidth(),(int)pd1.getSize().getHeight(),pd1.getDegrees(),pd1.getCostumes().get(pd1.getCostumeindex()),pd1.getName(),true);
        pd3 = new Sprite((int)pd1.getLocation().getX()+pipeDistance*2,(int)pd1.getLocation().getY(),(int)pd1.getSize().getWidth(),(int)pd1.getSize().getHeight(),pd1.getDegrees(),pd1.getCostumes().get(pd1.getCostumeindex()),pd1.getName(),true);
        pu1 = new Sprite(PipeX,pipeMaxHeight,pipeup.getWidth(null),pipeup.getHeight(null),0,pipeup,"Up pipe",true);
        pu2 = new Sprite((int)pu1.getLocation().getX()+pipeDistance,(int)pu1.getLocation().getY(),(int)pu1.getSize().getWidth(),(int)pu1.getSize().getHeight(),pu1.getDegrees(),pu1.getCostumes().get(pu1.getCostumeindex()),pu1.getName(),true);
        pu3 = new Sprite((int)pu1.getLocation().getX()+pipeDistance*2,(int)pu1.getLocation().getY(),(int)pu1.getSize().getWidth(),(int)pu1.getSize().getHeight(),pu1.getDegrees(),pu1.getCostumes().get(pu1.getCostumeindex()),pu1.getName(),true);
        frame = new JFrame();
        world = new World(mainworld,"Main world");
        startworld = new World(startt,"Start world");
        death = new World(deathh,"Death world");
        viewPort = new WorldPanel(startworld, false) {
            @Override
            protected void onUpdate() {
            }

            @Override
            protected void afterUpdate() {
            }
        };
        viewPort.setFocusable(true);
        viewPort.addKeyListener(mainListener);
        bird = new Sprite(290/2,400/2,26,26,0,ImageIO.read(this.getClass().getResourceAsStream(birdpath)),"bird",true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                exporter.composeExport(highscore,storeunlocks,actgm,birdpath,coinses,birdpaths);
            }
        });
        controls = new JPanel();
        play = new JButton("Play!");
        play.addActionListener(this);
        play.addKeyListener(mainListener);
        showinfo = new JButton("Info");
        showinfo.addActionListener(this);
        showinfo.addKeyListener(mainListener);
        store = new JButton("Store");
        store.addActionListener(this);
        store.addKeyListener(mainListener);
        options = new JButton("Options");
        options.addActionListener(this);
        options.addKeyListener(mainListener);
        controls.setLayout(new GridLayout(1, 5));
        controls.add(play);
        controls.add(store);
        controls.add(options);
        controls.add(showinfo);
        world.addSprite(bird);
        world.addSprite(pd1);
        world.addSprite(pd2);
        world.addSprite(pd3);
        world.addSprite(pu1);
        world.addSprite(pu2);
        world.addSprite(pu3);
        world.addSprite(coin);
        viewPort.setPreferredSize(new Dimension(350, 400));
        controls.setPreferredSize(new Dimension(350, 20));
        frame.setTitle("Flappy bird");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 470);
        frame.setLayout(new FlowLayout());
        frame.add(viewPort);
        frame.add(controls);
        frame.setVisible(true);
        updateStore();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (frame.isVisible()){
                    viewPort.update();
                    try {
                        spriteCheck();
                        pipeCheck();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int[] velocity = {1};
                final int[] ct = {0};
                ActionListener a = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (playing){
                            while (rising);
                            bird.setLocation((int)bird.getLocation().getX(),(int)bird.getLocation().getY() + velocity[0]);
                            if (rising){
                                velocity[0] =1;ct[0]=0;return;}
                            if (bird.getDegrees() != 90){
                                if (bird.getDegrees() == 360){
                                    bird.setDegrees(0);
                                }
                                else {
                                    bird.setDegrees(bird.getDegrees()+2);
                                }
                            }
                            if (rising){
                                velocity[0] =1;ct[0]=0;return;}
                            bird.setLocation((int) bird.getLocation().getX(), (int) bird.getLocation().getY() + velocity[0]);
                            if (rising){
                                velocity[0] =1;ct[0]=0;return;}
                            if (bird.getDegrees() != 90){
                                if (bird.getDegrees() == 360){
                                    bird.setDegrees(0);
                                }
                                else {
                                    bird.setDegrees(bird.getDegrees()+2);
                                }
                            }
                            if (rising){
                                velocity[0] =1;ct[0]=0;return;}
                            velocity[0]=2;
                            bird.setLocation((int) bird.getLocation().getX(), (int) bird.getLocation().getY() + velocity[0]);
                            if (rising){
                                velocity[0] =1;ct[0]=0;return;}
                            if (bird.getDegrees() != 90){
                                if (bird.getDegrees() == 360){
                                    bird.setDegrees(0);
                                }
                                else {
                                    bird.setDegrees(bird.getDegrees()+2);
                                }
                            }
                            velocity[0]=1;
                            if (rising){
                                velocity[0] =1;ct[0]=0;return;}
                            bird.setLocation((int) bird.getLocation().getX(), (int) bird.getLocation().getY() + velocity[0]);
                            if (rising){velocity[0] =1;ct[0]=0;return;}
                            if (bird.getDegrees() != 90){
                                if (bird.getDegrees() == 360){
                                    bird.setDegrees(0);
                                }
                                else {
                                    bird.setDegrees(bird.getDegrees()+2);
                                }
                            }
                            if (rising){
                                velocity[0] =1;ct[0]=0;return;}
                            bird.setLocation((int) bird.getLocation().getX(), (int) bird.getLocation().getY() + velocity[0]);
                            if (rising){velocity[0] =1;ct[0]=0;return;}
                            if (bird.getDegrees() != 90){
                                if (bird.getDegrees() == 360){
                                    bird.setDegrees(0);
                                }
                                else {
                                    bird.setDegrees(bird.getDegrees()+2);
                                }
                            }
                        }
                    }
                };
                Timer t = new Timer(40,a);
                t.start();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run(){
                while (frame.isVisible()){
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }
                    pipeMove();
                }
            }
        }).start();
    }
    KeyAdapter mainListener = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            if (e.getKeyChar() == 'i') {
                showinfo.doClick();
                return;
            }
            if (e.getKeyChar() == 'p') {
                play.doClick();
                return;
            }
            if (e.getKeyChar() == 'o') {
                options.doClick();
                return;
            }
            if (e.getKeyChar() == 's') {
                store.doClick();
                return;
            }
            if (e.getKeyChar() == ' ') {
                if (playing == false) {
                    play.doClick();
                    return;
                }
                rising = true;
                if (playing) {
                    final int[] count = {0};
                    final int[] countt = {9};
                    new Timer(15, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (count[0] == 6) {
                                Timer sourcetimer = (Timer) e.getSource();
                                sourcetimer.stop();
                            }
                            bird.setDegrees(315);
                            bird.setLocation((int) bird.getLocation().getX(), (int) bird.getLocation().getY() - countt[0]);
                            count[0]++;
                        }
                    }).start();
                    while (count[0] == 8) ;
                }
                rising = false;
            }
        }
    };
    public gameWindow() throws Exception {
        init();
    }
    public void updateStore(){
        for (int i : storeunlocks){
            switch (i){
                case 0:
                    if (coinink !=3) coinink = 2;
                    break;
                case 1:
                    coinink = 3;
                    break;
            }
        }
    }
    public void doHide() throws InterruptedException{
        controls.setVisible(false);
        for (int i = 0;i<5;i++){
            frame.setSize(frame.getWidth(), frame.getHeight() - (4 * i));
            Thread.sleep(70);
        }
    }
    public void dead() throws Exception{
        coinses = coinses + coinnum;
        pd1.setLocation(PipeX,downPipeY);
        pd2.setLocation((int)pd1.getLocation().getX()+pipeDistance,(int)pd1.getLocation().getY());
        pd3.setLocation((int)pd2.getLocation().getX()+pipeDistance,(int)pd1.getLocation().getY());
        pu1.setLocation(PipeX,pipeMaxHeight);
        pu2.setLocation((int)pu1.getLocation().getX()+pipeDistance,(int)pu1.getLocation().getY());
        pu3.setLocation((int) pu2.getLocation().getX() + pipeDistance, (int) pu1.getLocation().getY());
        if (scorenum>highscore)highscore=scorenum;
        viewPort.setWorld(death);
        playing = false;
        bird.setLocation((int)bird.getLocation().getX(),viewPort.getHeight()/2);
        play.setEnabled(false);
        for (int i = 0;i<5;i++){
            frame.setSize(frame.getWidth(), frame.getHeight() + (4 * i));
            Thread.sleep(70);
        }
        rising = false;
        controls.setVisible(true);
        play.setEnabled(true);
    }
    public void spriteCheck() throws Exception {
        if (Sprite.isCollided(bird,pu1)||Sprite.isCollided(bird,pu2)||Sprite.isCollided(bird,pu3)||Sprite.isCollided(bird,pd1)||Sprite.isCollided(bird,pd2)||Sprite.isCollided(bird,pd3)||bird.getLocation().getY()>= viewPort.getHeight()||bird.getLocation().getY()<=0&&playing){
            dead();
        }
        if (Sprite.isCollided(bird,coin)){
            coinnum= coinnum + coinink;
            coin.setLocation(PipeX+pipeDistance+30,(int)RandomNumber.randomInt((long)bird.getLocation().getY()-30,(long)bird.getLocation().getY()+50));
        }
    }
    public void pipeCheck(){
        if (pu1.getLocation().getX() ==bird.getLocation().getX() && !p1hit){
            p1hit = true;
            scorenum++;
        }
        if (pu2.getLocation().getX() ==bird.getLocation().getX() && !p2hit){
            p2hit = true;
            scorenum++;
        }
        if (pu3.getLocation().getX() ==bird.getLocation().getX() && !p3hit){
            p3hit = true;
            scorenum++;
        }
        if (pu1.getLocation().getX() <=0&&pd1.getLocation().getX() <=0){
            p1hit = false;
            int puh = (int)RandomNumber.randomInt((long)pipeMinHeight,(long)pipeMaxHeight,5);
            int pdh = puh + pipeHeightDistance;
            pu1.setLocation(PipeX+pipeDistance,puh);
            pd1.setLocation(PipeX+pipeDistance,pdh);
        }
        if (pu2.getLocation().getX() <=0&&pd2.getLocation().getX() <=0){
            p2hit = false;
            int puh = (int)RandomNumber.randomInt((long)pipeMinHeight,(long)pipeMaxHeight,5);
            int pdh = puh + pipeHeightDistance;
            pu2.setLocation(PipeX+pipeDistance,puh);
            pd2.setLocation(PipeX+pipeDistance,pdh);
        }
        if (pu3.getLocation().getX() <=0&&pd3.getLocation().getX() <=0){
            p3hit = false;
            int puh = (int)RandomNumber.randomInt((long)pipeMinHeight,(long)pipeMaxHeight,5);
            int pdh = puh + pipeHeightDistance;
            pu3.setLocation(PipeX+pipeDistance,puh);
            pd3.setLocation(PipeX+pipeDistance,pdh);
        }
        if (coin.getLocation().getX() <=0){
            coin.setLocation(PipeX+pipeDistance+50,(int)RandomNumber.randomInt((long)bird.getLocation().getY()-30,(long)bird.getLocation().getY()+30));
        }
    }
    public void pipeMove(){
        if (playing){
            pu1.setLocation((int)pu1.getLocation().getX()-5,(int)pu1.getLocation().getY());
            pd1.setLocation((int)pd1.getLocation().getX()-5,(int)pd1.getLocation().getY());
            pu2.setLocation((int)pu2.getLocation().getX()-5,(int)pu2.getLocation().getY());
            pd2.setLocation((int)pd2.getLocation().getX()-5,(int)pd2.getLocation().getY());
            pu3.setLocation((int)pu3.getLocation().getX()-5,(int)pu3.getLocation().getY());
            pd3.setLocation((int)pd3.getLocation().getX()-5,(int)pd3.getLocation().getY());
            coin.setLocation((int)coin.getLocation().getX()-5,(int)coin.getLocation().getY());
        }
    }
    public String[] toArray(ArrayList<String> collection){
        String[] array = new String[collection.size()];
        for (int i = 0;i<collection.size()-1;i++){
            array[i] = collection.get(i);
        }
        return array;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == play){
            coinnum = 0;
            scorenum = 0;
            try {
                doHide();
                viewPort.setWorld(world);
                playing = true;
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

        else if (e.getSource() == store){
            stmodel.clear();
            bmodel.clear();
            money.setText(String.valueOf(coinses)+" Coins");
            for (String i : parser.getStoreitems()){
                stmodel.addElement(i);
            }
            ArrayList<Integer> removes = new ArrayList<Integer>();
            for (int i : storeunlocks){
                bmodel.addElement("Bought " + stmodel.get(i));
                removes.add(i);
            }
            for (int i : removes){
                if (stmodel.isEmpty()){
                    break;
                }
                if (stmodel.size() == removes.size()){
                    stmodel.clear();
                    break;
                }
                stmodel.remove(i);
            }
            storelist.setModel(stmodel);
            bought.setModel(bmodel);
            storeMenu.setVisible(true);
            updateStore();
        }

        else if (e.getSource() == options){
            JOptionPane.showMessageDialog(null,"No options available yet!");
        }

        else if (e.getSource() == showinfo){
            JOptionPane.showMessageDialog(null,"Score: "+scorenum+"\nHighscore: "+highscore+"\nCoins: "+coinnum+"\nAll coins: "+coinses);
        }
    }
    class DisabledItemSelectionModel extends DefaultListSelectionModel {

        private static final long serialVersionUID = 1L;

        @Override
        public void setSelectionInterval(int index0, int index1) {
            super.setSelectionInterval(-1, -1);
        }
    }
}
