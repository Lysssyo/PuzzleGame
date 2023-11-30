package com.gzhu;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.xml.crypto.Data;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GameJFrame extends JFrame implements KeyListener, ActionListener {
    //成员变量
    int[][] data = new int[4][4];//data用于打乱图片
    int[][] windata = {{1, 5, 9, 13}, {2, 6, 10, 14}, {3, 7, 11, 15}, {4, 8, 12, 0}};
    //x、y用于记录空白图片在二维数组中的位置
    int x = 0;
    int y = 0;
    //定义变量用来统计步数
    int step = 0;

    //创建条目JMenuItem
    JMenuItem replayItem = new JMenuItem("重新游戏");
    JMenuItem reloginItem = new JMenuItem("重新登录");
    JMenuItem closeItem = new JMenuItem("关闭游戏");
    JMenuItem accountItem = new JMenuItem("公众号");
    //说明：上面4项本应该放在InitJMenuBar中，但是因为actionPerformed中也要用，所以放在前面

    public GameJFrame() {
        //构造函数
        //初始化界面
        InitJFrame();

        //初始化菜单
        InitJMenuBar();

        //初始化随机图片的数据
        InitData();

        //初始化图片
        InitImage();


        //setVisible
        this.setVisible(true);
    }

    private void InitBackground() {
        ImageIcon imageIcon = new ImageIcon("UI\\src\\image\\background.png");
        JLabel jLabel = new JLabel(imageIcon);
        jLabel.setBounds(40, 40, 508, 560);
        this.getContentPane().add(jLabel);

    }

    //初始化数据（打乱）
    private void InitData() {
        //1.定义一个一维数组
        int[] tempArr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        //2.打乱数组中的数据的顺序
        //遍历数组，得到每一个元素，拿着每一个元素跟随机索引上的数据进行交换
        Random r = new Random();
        for (int i = 0; i < tempArr.length; i++) {
            //获取到随机索引
            int index = r.nextInt(tempArr.length);
            //拿着遍历到的每一个数据，跟随机索引上的数据进行交换
            int temp = tempArr[i];
            tempArr[i] = tempArr[index];
            tempArr[index] = temp;
        }
        //4.给二维数组添加数据
        //遍历一维数组tempArr得到每一个元素，把每一个元素依次添加到二维数组当中
        for (int i = 0; i < tempArr.length; i++) {
            if (tempArr[i] == 0) {
                x = i / 4;
                y = i % 4;
            }
            data[i / 4][i % 4] = tempArr[i];
        }
    }

    //初始化图片
    public void InitImage() {
        this.getContentPane().removeAll();

        int num = 0;//用于接收data[i][j]的值

        if (victory()) {
            ImageIcon victoryimage = new ImageIcon("UI\\src\\image\\win.png");
            JLabel victoryjlabel = new JLabel(victoryimage);
            victoryjlabel.setBounds(203, 283, 197, 73);
            this.getContentPane().add(victoryjlabel);

        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j <= 3; j++) {
                num = data[i][j];
                ImageIcon imageIcon = new ImageIcon("UI\\src\\image\\animal\\animal3\\" + num + ".jpg");
                //创建JLabel对象来管理ImageIcon
                JLabel jLabel1 = new JLabel(imageIcon);
                //利用JLabel对象设置大小，宽高
                jLabel1.setBounds(105 * i + 83, 105 * j + 134, 105, 105);
                //将JLabel对象添加到整个界面当中
                this.getContentPane().add(jLabel1);
                //添加边框
                jLabel1.setBorder(new BevelBorder(BevelBorder.LOWERED));
            }
        }

        JLabel stepCount = new JLabel("步数：" + step);
        stepCount.setBounds(50, 30, 100, 20);
        this.getContentPane().add(stepCount);

        //初始化背景
        InitBackground();

        this.getContentPane().repaint();
    }

    private boolean victory() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (windata[i][j] != data[i][j])
                    return false;
            }
        }
        return true;
    }

    public void InitJMenuBar() {
        //创建菜单JMenuBar
        JMenuBar jMenuBar = new JMenuBar();

        //创建菜单的选项JMenu
        JMenu functionjMenu = new JMenu("功能");
        JMenu aboutusjMenu = new JMenu("关于我们");



        //jMenu添加到jMenuBar
        jMenuBar.add(functionjMenu);
        jMenuBar.add(aboutusjMenu);

        //给条目绑定事件
        replayItem.addActionListener(this);
        reloginItem.addActionListener(this);
        closeItem.addActionListener(this);
        accountItem.addActionListener(this);

        //item添加到jmenu
        functionjMenu.add(reloginItem);
        functionjMenu.add(replayItem);
        functionjMenu.add(closeItem);
        aboutusjMenu.add(accountItem);

        setJMenuBar(jMenuBar);
    }

    public void InitJFrame() {
        //setSize
        this.setSize(600, 700);
        //将主界面设置到屏幕的正中央
        this.setLocationRelativeTo(null);
        //将主界面置顶
        this.setAlwaysOnTop(true);
        //关闭主界面的时候让代码一起停止
        this.setDefaultCloseOperation(3);
        //给主界面设置一个标题
        this.setTitle("拼图游戏单机版 v1.0");

        //取消默认居中，取消了组件才能按照X、Y轴的方式添加组件
        this.setLayout(null);
        //为整个界面添加监听事件
        this.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }


    @Override
    //实现显示完整图片的操作，按压W时显示完整图片，松开时恢复
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();//W的KeyCode为87
        if (victory())//如果胜利了，就不能再调用这个方法
            return;
        if (keyCode == 87) {
            //打印完整图片
            this.getContentPane().removeAll();
            ImageIcon imageIcon = new ImageIcon("UI\\src\\image\\animal\\animal3\\all.jpg");
            //创建JLabel对象来管理ImageIcon
            JLabel all = new JLabel(imageIcon);
            //利用JLabel对象设置大小，宽高
            all.setBounds(83, 134, 420, 420);


            //将JLabel对象添加到整个界面当中
            this.getContentPane().add(all);
            ImageIcon background = new ImageIcon("UI\\src\\image\\background.png");
            JLabel jLabel = new JLabel(background);
            jLabel.setBounds(40, 40, 508, 560);

            this.getContentPane().add(jLabel);
            this.getContentPane().repaint();

        }

    }

    //松开按键的时候会调用这个方法
    @Override
    public void keyReleased(KeyEvent e) {
        //对上，下，左，右进行判断
        //左：37 上：38 右：39 下：40
        if (victory())//如果胜利了，就不能再调用这个方法
            return;
        int code = e.getKeyCode();
        switch (code) {
            case 37://左键松
                if (x <= 2) {
                    System.out.println("向左移动");
                    //逻辑：
                    //把空白方块右方的数字往左移动
                    data[x][y] = data[x + 1][y];
                    data[x + 1][y] = 0;
                    x++;
                    step++;
                    //调用方法按照最新的数字加载图片
                    InitImage();
                }
                break;
            case 38://上键松
                if (y <= 2) {
                    System.out.println("向上移动");
                    //逻辑：
                    //把空白方块下方的数字往上移动
                    //x，y  表示空白方块
                    //x + 1， y 表示空白方块下方的数字
                    //把空白方块下方的数字赋值给空白方块
                    data[x][y] = data[x][y + 1];
                    data[x][y + 1] = 0;
                    y++;//空白的位置变了
                    step++;
                    //调用方法按照最新的数字加载图片
                    InitImage();
                }
                break;
            case 39://右键松
                if (x >= 1) {
                    System.out.println("向右移动");
                    //逻辑：
                    //把空白方块左方的数字往右移动
                    data[x][y] = data[x - 1][y];
                    data[x - 1][y] = 0;
                    x--;
                    //每移动一次，计数器就自增一次。
                    step++;
                    InitImage();
                }
                break;
            case 40://下键松
                if (y >= 1) {
                    System.out.println("向下移动");
                    //逻辑：
                    //把空白方块上方的数字往下移动
                    data[x][y] = data[x][y - 1];
                    data[x][y - 1] = 0;
                    y--;
                    step++;
                    //调用方法按照最新的数字加载图片
                    InitImage();
                }
                break;
            case 87://W松
                System.out.println(code);
                InitImage();
                break;
            case 10://回车松
                int num = 1;
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        data[j][i] = num;
                        num++;
                    }
                }
                data[3][2] = 0;
                data[3][3] = 12;
                x = 3;
                y = 2;
                InitImage();
                break;

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source==replayItem){
            step=0;
            InitData();
            InitImage();
        }
        else if(source==closeItem){
            //关闭虚拟机
            System.exit(0);
        }
    }
}

