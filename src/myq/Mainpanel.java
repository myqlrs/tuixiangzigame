package myq;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.net.URL;
import java.util.Stack;

/**
 * @author 孟赟强
 * @date 2019/5/14-9:17
 **/
//添加按钮单击事件，设置乌龟移动箱子方法
public class Mainpanel extends JPanel implements KeyListener {
    //设置最大关卡为50关
    int max=50;
    //存储地图位置信息
    int[][] map,maptmp;
    //存储具体位置横纵坐标
    int manX,manY,boxnum;
    //存放背景图片
    Image[] myImage;
    Readmap Levelmap;
    Readmap Levelmaptmp;
    int len=30;
    //初始化最初关卡为第一关
    public int level=1;
    Stack mystack=new Stack();
    //绘制地图
    Mainpanel()
    {
        setBounds(15,50,600,600);
        setBackground(Color.white);
        addKeyListener(this);
        myImage=new Image[10];
        for(int i=0; i<10; i++)
        {
            myImage[i] = Toolkit.getDefaultToolkit().getImage("pic\\"+i+".gif");
        }

        setVisible(true);
    }

    void Tuixiangzi(int i)
    {
        //地图关卡
        Levelmap=new Readmap(i);
        Levelmaptmp=new Readmap(i);
        //获得当前地图关卡
        map=Levelmap.getmap();
        //地图横向位置
        manX=Levelmap.getmanX();
        //地图纵向位置
        manY=Levelmap.getmanY();
        //获得当前所在地图位置
        maptmp=Levelmaptmp.getmap();
        repaint();
    }
    //最大关卡返回信息
    int maxlevel(){return max;}

    public void paint(Graphics g)
    {
        for(int i=0; i<20; i++)
            for(int j=0; j<20; j++)
            {
                g.drawImage(myImage[map[j][i]],i*len,j*len,this);
            }
        //设置字体颜色
        g.setColor(new Color(0,0,255));
        //加粗显示关卡信息
        g.setFont(new Font("楷体_2312",Font.BOLD,30));
        g.drawString("现在是第",150,40);
        g.drawString(String.valueOf(level),310,40);
        g.drawString("关",360,40);
    }

    public void keyPressed(KeyEvent e)
    {
        //添加单击键盘向上键和W键调用moveup（）方法
        if(e.getKeyCode()==KeyEvent.VK_UP||e.getKeyCode()==KeyEvent.VK_W){moveup();}
        //添加单击键盘向下键和S键调用movedown（）方法
        if(e.getKeyCode()==KeyEvent.VK_DOWN||e.getKeyCode()==KeyEvent.VK_S){movedown();}
        //添加单击键盘向左键和A键调用moveleft（）方法
        if(e.getKeyCode()==KeyEvent.VK_LEFT||e.getKeyCode()==KeyEvent.VK_A){moveleft();}
        //添加单击键盘向右键和D键调用moveright（）方法
        if(e.getKeyCode()==KeyEvent.VK_RIGHT||e.getKeyCode()==KeyEvent.VK_D){moveright();}
        //添加单击键盘ESC键，退出程序
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE){System.exit(0);}
        //判断本关是否通过
        if(iswin())
        {
            //判断是否为最后一关
            if(level==max){JOptionPane.showMessageDialog(this, "恭喜您通过最后一关！！！");}
            else
            {
                //显示通过信息并进入下一关
                String msg="恭喜您通过第"+level+"关!!!\n是否要进入下一关？";
                int type=JOptionPane.YES_NO_OPTION;
                String title="过关";
                int choice=0;
                choice=JOptionPane.showConfirmDialog(null,msg,title,type);
                if(choice==1)System.exit(0);
                else if(choice==0)
                {
                    //进入下一关
                    level++;
                    Tuixiangzi(level);
                }
            }
            mystack.removeAllElements();
        }
    }
    public void keyTyped(KeyEvent e){}
    public void keyReleased(KeyEvent e){}
    boolean isMystackEmpty(){return mystack.isEmpty();}
    //悔一步方法
    int  back(){return (Integer)mystack.pop();}

    void remove(){mystack.removeAllElements();}
    /*
     * 0 背景图片
     * 1 障碍物
     * 2 空地
     * 3 箱子（不在目标位置）
     * 4 目标位置
     * 5 乌龟（身体向下）
     * 6 乌龟（身体向左）
     * 7 乌龟（身体向右）
     * 8 乌龟（身体向上）
     * 9 箱子（在目标位置）
     */
    //向上移动方法
    void moveup()
    {
        //如果乌龟可以向当前方向移动
        if(map[manY-1][manX]==2||map[manY-1][manX]==4)
        {
            //如果这里是目标位置或者已经有箱子在这正确的位置
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                //乌龟移走后显示为目标位置
                map[manY][manX]=4;
                //如果为空地
            else map[manY][manX]=2;
            //乌龟移走动后显示为身体向上的乌龟
            map[manY-1][manX]=8;
            //因为箱子已经移动，所以需要重新计算可以站立的位置，返回值为10
            repaint();manY--;mystack.push(10);
        }
        //如果乌龟上方有箱子
        else if(map[manY-1][manX]==3)
        {
            //如果箱子上方是目标位置
            if(map[manY-2][manX]==4)
            {
                //重新计算当前位置且如果该位置是目标位置或有箱子在这目标位置
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    //移动走后显示为目标位置
                    map[manY][manX]=4;
                    //如果该位置是空地
                else map[manY][manX]=2;
                //该位置上方显示为身体向上的乌龟
                map[manY-1][manX]=8;
                //乌龟的上方显示为箱子（在目标位置）
                map[manY-2][manX]=9;
                //乌龟移动，重新计算位置
                repaint();manY--;mystack.push(11);
            }
            //如果箱子上方是空地
            else if(map[manY-2][manX]==2)
            {
                //重新计算当前位置且如果该位置是目标位置或有箱子在这目标位置
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    //移动走后显示为目标位置
                    map[manY][manX]=4;
                    //如果该位置是空地
                else map[manY][manX]=2;
                //该位置上方显示为身体向上的乌龟
                map[manY-1][manX]=8;
                //乌龟的上方显示为箱子（不在目标位置）
                map[manY-2][manX]=3;
                //乌龟移动，重新计算位置，返回值为11
                repaint();manY--;mystack.push(11);
            }
            //如果箱子已经被移动到目标位置
            else {map[manY][manX]=8;repaint();}
        }
        //如果乌龟上方有已经被移动到正确位置上的箱子
        else if(map[manY-1][manX]==9)
        {
            //如果箱子上方是目标位置
            if(map[manY-2][manX]==4)
            {
                //重新计算当前位置且如果该位置是目标位置或有箱子在这目标位置
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    //移动走后显示为目标位置
                    map[manY][manX]=4;
                    //如果该位置是空地
                else map[manY][manX]=2;
                //该位置上方显示为身体向上的乌龟
                map[manY-1][manX]=8;
                //乌龟的上方显示为箱子（在目标位置）
                map[manY-2][manX]=9;
                //乌龟移动，重新计算位置，返回值为11
                repaint();manY--;mystack.push(11);
            }
            //如果箱子上方是空地
            else if(map[manY-2][manX]==2)
            {
                //重新计算当前位置且如果该位置是目标位置或有箱子在这目标位置
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    //移动走后显示为目标位置
                    map[manY][manX]=4;
                    //如果该位置是空地
                else map[manY][manX]=2;
                //该位置上方显示为身体向上的乌龟
                map[manY-1][manX]=8;
                //乌龟的上方显示为箱子（不在目标位置）
                map[manY-2][manX]=3;
                //乌龟移动，重新计算位置，返回值为11
                repaint();manY--;mystack.push(11);
            }
            //该位置显示为身体向上的乌龟
            else {map[manY][manX]=8;
                //重新计算位置
                repaint();}
        }
        //如果该位置上方有障碍物
        if(map[manY-1][manX]==1)
        {
            //乌龟位置不变，显示为身体向上。重新绘画地图
            map[manY][manX]=8;repaint();
        }
    }
    //悔一步向上移动方法
    void backup(int t)
    {
        //设置整型变量n，接收乌龟移动后的返回值
        int n=t;
        //当返回值为10时
        if(n==10)
        {
            //重新计算当前位置且如果该位置是目标位置或有箱子在这目标位置
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
            {
                //显示为目标位置
                map[manY][manX]=4;
            }
            //否则该位置是空地
            else map[manY][manX]=2;
        }
        //当返回值为11时
        else if(n==11)
        {
            //重新计算当前位置且如果该位置是目标位置或有箱子在这目标位置
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
            {
                //显示为箱子（在正确位置上）
                map[manY][manX]=9;
            }
            //否则显示为箱子（不在正确位置上）
            else map[manY][manX]=3;
            //重新计算当前位置，如果该位置上方是目标位置或有箱子在这目标位置
            if(maptmp[manY-1][manX]==4||maptmp[manY-1][manX]==9)
            {
                //显示为箱子（在正确位置上）
                map[manY-1][manX]=4;
            }
            //显示为空地
            else map[manY-1][manX]=2;
        }
        //显示为身体向上的乌龟
        map[manY+1][manX]=8;
        //重新绘制地图，乌龟移回去
        repaint();manY++;
    }
    /*
     * 以下分别为向下、向左、向右移动方法与向上类似不做过多注释
     */
    //向下移动方法
    void movedown()
    {
        if(map[manY+1][manX]==2||map[manY+1][manX]==4)
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                map[manY][manX]=4;
            else map[manY][manX]=2;
            map[manY+1][manX]=5;
            repaint();manY++;mystack.push(20);
        }
        else if(map[manY+1][manX]==3)
        {
            if(map[manY+2][manX]==4)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY+1][manX]=5;
                map[manY+2][manX]=9;
                repaint();manY++;mystack.push(21);
            }
            else if(map[manY+2][manX]==2)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY+1][manX]=5;
                map[manY+2][manX]=3;
                repaint();manY++;mystack.push(21);
            }
            else {map[manY][manX]=5;repaint();}
        }
        else if(map[manY+1][manX]==9)
        {
            if(map[manY+2][manX]==4)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY+1][manX]=5;
                map[manY+2][manX]=9;
                repaint();manY++;mystack.push(21);
            }
            else if(map[manY+2][manX]==2)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY+1][manX]=5;
                map[manY+2][manX]=3;
                repaint();manY++;mystack.push(21);
            }
            else {map[manY][manX]=5;repaint();}
        }
        else if(map[manY+1][manX]==1)
        {
            map[manY][manX]=5;repaint();
        }
    }
    //悔一步向下移动方法
    void backdown(int t)
    {
        int n=t;
        if(n==20)
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
            {
                map[manY][manX]=4;
            }
            else map[manY][manX]=2;
        }
        else if(n==21)
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
            {
                map[manY][manX]=9;
            }
            else map[manY][manX]=3;
            if(maptmp[manY+1][manX]==4||maptmp[manY+1][manX]==9)
            {
                map[manY+1][manX]=4;
            }
            else map[manY+1][manX]=2;
        }
        map[manY-1][manX]=5;
        repaint();manY--;
    }
    //向左移动方法
    void moveleft()
    {
        if(map[manY][manX-1]==2||map[manY][manX-1]==4)
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                map[manY][manX]=4;
            else map[manY][manX]=2;
            map[manY][manX-1]=6;
            repaint();manX--;mystack.push(30);
        }
        else if(map[manY][manX-1]==3)
        {
            if(map[manY][manX-2]==4)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY][manX-1]=6;
                map[manY][manX-2]=9;
                repaint();manX--;mystack.push(31);
            }
            else if(map[manY][manX-2]==2)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY][manX-1]=6;
                map[manY][manX-2]=3;
                repaint();manX--;mystack.push(31);
            }
            else {map[manY][manX]=6;repaint();}
        }
        else if(map[manY][manX-1]==9)
        {
            if(map[manY][manX-2]==4)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY][manX-1]=6;
                map[manY][manX-2]=9;
                repaint();manX--;mystack.push(31);
            }
            else if(map[manY][manX-2]==2)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY][manX-1]=6;
                map[manY][manX-2]=3;
                repaint();manX--;mystack.push(31);
            }
            else {map[manY][manX]=6;repaint();}
        }
        else if(map[manY][manX-1]==1)
        {
            map[manY][manX]=6;repaint();
        }
    }
    //悔一步向左移动方法
    void backleft(int t)
    {
        int n=t;
        if(n==30)
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
            {
                map[manY][manX]=4;
            }
            else map[manY][manX]=2;
        }
        else if(n==31)
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
            {
                map[manY][manX]=9;
            }
            else map[manY][manX]=3;
            if(maptmp[manY][manX-1]==4||maptmp[manY][manX-1]==9)
            {
                map[manY][manX-1]=4;
            }
            else map[manY][manX-1]=2;
        }
        map[manY][manX+1]=6;
        repaint();manX++;
    }
    //向右移动方法
    void moveright()
    {
        if(map[manY][manX+1]==2||map[manY][manX+1]==4)
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                map[manY][manX]=4;
            else map[manY][manX]=2;
            map[manY][manX+1]=7;
            repaint();manX++;mystack.push(40);
        }
        else if(map[manY][manX+1]==3)
        {
            if(map[manY][manX+2]==4)
            {
                if(maptmp[manY][manX]==4)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY][manX+1]=7;
                map[manY][manX+2]=9;
                repaint();manX++;mystack.push(41);
            }
            else if(map[manY][manX+2]==2)
            {
                if(maptmp[manY][manX]==4)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY][manX+1]=7;
                map[manY][manX+2]=3;
                repaint();manX++;mystack.push(41);
            }
            else {map[manY][manX]=7;repaint();}
        }
        else if(map[manY][manX+1]==9)
        {
            if(map[manY][manX+2]==4)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY][manX+1]=7;
                map[manY][manX+2]=9;
                repaint();manX++;mystack.push(41);
            }
            else if(map[manY][manX+2]==2)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY][manX+1]=7;
                map[manY][manX+2]=3;
                repaint();manX++;mystack.push(41);
            }
            else {map[manY][manX]=7;repaint();}
        }
        else if(map[manY][manX+1]==1)
        {
            map[manY][manX]=7;repaint();
        }
    }
    //悔一步向右移动方法
    void backright(int t)
    {
        int n=t;
        if(n==40)
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
            {
                map[manY][manX]=4;
            }
            else map[manY][manX]=2;
        }
        else if(n==41)
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
            {
                map[manY][manX]=9;
            }
            else map[manY][manX]=3;
            if(maptmp[manY][manX+1]==4||maptmp[manY][manX+1]==9)
            {
                map[manY][manX+1]=4;
            }
            else map[manY][manX+1]=2;
        }
        map[manY][manX-1]=7;
        repaint();manX--;
    }
    //当本关不通过
    boolean iswin()
    {
        //当本关失败重新绘制本关地图
        boolean num=false;
        out:for(int i=0; i<20; i++)
            for(int j=0; j<20; j++)
            {
                if(maptmp[i][j]==4||maptmp[i][j]==9)
                    if(map[i][j]==9)num=true;
                    else {num=false;break out;}
            }
        return num;
    }
}

