package myq;

import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * @author 孟赟强
 * @date 2019/5/14-9:12
 **/
public class MainFrame extends JFrame {
    Mainpanel panel;//创建一个容器
    JComboBox jc = new JComboBox();//创建菜单栏，添加菜单项，分别对应每一个选项
    Menu setmuc = new Menu("设置音乐");//创建设置音乐菜单项
    Sound sound;
    AudioClip audioClip;
    boolean isPlayOrStop = false;//音乐是否播放
    int musicIndex = 0;//音乐索引
    int[] btnSize = {625, 50, 80, 30};//功能按钮位置大小
    ArrayList<MenuItem> menuItemList = new ArrayList<>();

    MainFrame() {
        //在界面最上方声明游戏名称
        super("推箱子Lv1.0");
        setSize(720, 720);
        setVisible(true);
        //禁止用户改变窗体大小
        setResizable(false);
        //设置窗口显示位置
        setLocation(300, 20);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container cont = getContentPane();
        cont.setLayout(null);
        cont.setBackground(Color.black);
        //创建菜单栏，将选项、设置音乐与帮助放在菜单栏内
        MenuBar bar = new MenuBar();
        bar.add(setmuc);
        setMenuBar(bar);
        for (int i = 0; i < musicList.size(); i++) {//初始化音乐设置
            MenuItem menuItem = new MenuItem(musicList.get(i).name);
            initMenu(setmuc, menuItem, musicActionListener);
            menuItemList.add(menuItem);
        }
        //显示版本
        JLabel lb = new JLabel("JAVA推箱子v1.0版！！！", SwingConstants.CENTER);
        lb.setBounds(100, 20, 400, 20);
        lb.setForeground(Color.green);
        add(lb);
        //设置更换音乐的提示文字
        JLabel lb2 = new JLabel("更换音乐", SwingConstants.CENTER);
        lb2.setBounds(625, 500, 55, 20);
        lb2.setForeground(Color.white);
        add(lb2);
        //添加功能按钮
        for (int i = 0; i < btnList.size(); i++) {
            JButton jButton = new JButton(btnList.get(i));
            jButton.setBounds(btnSize[0], btnSize[1] + 50 * i, btnSize[2], btnSize[3]);
            jButton.addActionListener(actionListener);
            jButton.setName(String.valueOf(i));
            add(jButton);
        }
        //添加音乐选项下拉选择框
        for (Music music : musicList) {
            jc.addItem(music.name);
        }
        jc.setSelectedIndex(musicIndex);
        jc.setToolTipText(musicList.get(musicIndex).name);
        jc.addItemListener(itemListener);
        jc.setBounds(625, 530, 80, 20);
        cont.add(jc);
        //创建sound对象调用sound打开音乐文件
        sound = new Sound();
        audioClip = sound.loadSound();
        panel = new Mainpanel();
        add(panel);
        panel.Tuixiangzi(panel.level);
        panel.requestFocus();
        validate();
    }

    ActionListener musicActionListener = e -> {
        MenuItem menuItem = (MenuItem) e.getSource();
        int index = menuItemList.indexOf(menuItem);
        jc.setSelectedIndex(index);
    };
    ActionListener actionListener = e -> {
        JButton jButton = (JButton) e.getSource();
        switch (jButton.getName()) {
            case "0"://重置
                reset(panel);
                break;
            case "1"://悔一步
                backOneStep(panel, this);
                break;
            case "2"://上一关
                setPass(panel, this, -1);
                break;
            case "3"://上一关
                setPass(panel, this, +1);
                break;
            case "4"://选关
                choicePass(panel, this);
                break;
            case "5"://第一关
                setPass(panel, this, 1 - panel.level);
                break;
            case "6"://最终关
                setPass(panel, this, panel.max - panel.level);
                break;
            case "7"://帮助
                help();
                break;
            case "8"://音乐
                isPlayOrStop = !isPlayOrStop;
                if (isPlayOrStop) {//播放
                    audioClip=sound.loadSound();
                    audioClip.loop();
                    panel.requestFocus();
                } else {//停止
                    audioClip.stop();
                    panel.requestFocus();
                }
                break;
        }
    };

    /**
     * 音乐下拉列表事件
     */
    ItemListener itemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            //定义一个整型变量，给每个音乐编号以便调用
            int index = jc.getSelectedIndex();
            if (musicIndex == index) {
                return;
            }
            sound.setMusic(musicList.get(index).fileName);
            menuItemList.get(musicIndex).setEnabled(true);
            musicIndex = index;
            menuItemList.get(musicIndex).setEnabled(false);
            panel.requestFocus();
        }
    };

    /**
     * 帮助
     */
    void help(){
        JOptionPane.showMessageDialog(this, "JAVA推箱子Lv1.0版\n操作方法\n"
                + "上——w/↑\n下——s/↓\n左——a/←\n右——d/→\n退出——Esc");
    }
    /**
     * 重置
     *
     * @param panel
     */
    void reset(Mainpanel panel) {
        panel.Tuixiangzi(panel.level);
        panel.remove();
        panel.requestFocus();
    }

    /**
     * 选择关卡
     *
     * @param panel
     * @param component
     */
    void choicePass(Mainpanel panel, Component component) {
        String lel = JOptionPane.showInputDialog(component, "请输入您要转到的关卡号：(1~50)");
        panel.level = Integer.parseInt(lel);
        panel.remove();
        //判断输入关卡是否在范围内
        if (panel.level > panel.maxlevel() || panel.level < 1) {
            JOptionPane.showMessageDialog(component, "没有这一关！！！");
        } else {
            panel.Tuixiangzi(panel.level);
        }
        panel.requestFocus();
    }

    void setPass(Mainpanel panel, Component component, int moveLeve) {
        int level = panel.level + moveLeve;
        if (level < 1) {
            JOptionPane.showMessageDialog(component, "本关是第一关");
        } else if (level > panel.max) {
            JOptionPane.showMessageDialog(component, "本关已是最后一关");
        } else if (level > panel.maxlevel() || level < 1) {
            JOptionPane.showMessageDialog(component, "没有这一关！！！");
        } else {
            panel.level=level;
            panel.remove();
            panel.Tuixiangzi(level);
            panel.requestFocus();
        }
    }

    /**
     * 悔一步
     *
     * @param panel
     */
    void backOneStep(Mainpanel panel, Component component) {
        if (panel.isMystackEmpty()) {//判断箱子是否被移动
            JOptionPane.showMessageDialog(component, "您还未移动！！！");
        } else {//接收移动后返回的数值
            switch(panel.back())
            {
                case 10:panel.backup(10);break;
                case 11:panel.backup(11);break;
                case 20:panel.backdown(20);break;
                case 21:panel.backdown(21);break;
                case 30:panel.backleft(30);break;
                case 31:panel.backleft(31);break;
                case 40:panel.backright(40);break;
                case 41:panel.backright(41);break;
            }
        }
        panel.requestFocus();
    }

    void initMenu(Menu menu, MenuItem menuItem, ActionListener actionListener) {
        menuItem.addActionListener(actionListener);
        menu.add(menuItem);
    }

    static ArrayList<Music> musicList = new ArrayList<>();//音乐列表
    static ArrayList<String> btnList = new ArrayList<>();//功能按钮列表

    static {
        musicList.add(new Music("红昭愿", "红昭愿.wav"));
        musicList.add(new Music("月光", "月光.wav"));
        musicList.add(new Music("天行九歌", "天行九歌.wav"));
        musicList.add(new Music("刚好遇见你", "刚好遇见你.wav"));
        musicList.add(new Music("离人愁", "离人愁.wav"));
        btnList.add("重 置");
        btnList.add("悔一步");
        btnList.add("上一关");
        btnList.add("下一关");
        btnList.add("选关");
        btnList.add("第１关");
        btnList.add("最终关");
        btnList.add("帮助");
        btnList.add("音乐");
    }
}
