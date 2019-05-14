package myq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author 孟赟强
 * @date 2019/5/14-9:20
 **/
public class Readmap {
    private int level,mx,my;
    private int[][] mymap=new int[20][20];
    FileReader r;
    BufferedReader br;String bb="";
    int[] x;int c=0;
    Readmap(int k)
    {
        level=k;
        String s;
        try
        {
            //打开第level关地图文件
            File f=new File("maps\\"+level+".map");
            r=new FileReader(f);
            br=new BufferedReader(r);
        }
        catch (IOException e)
        {
            //显示出错信息
            System.out.println(e);
        }
        try
        {
            while ((s=br.readLine())!=null)
            {
                bb=bb+s;

            }
        }
        catch (IOException g)
        {
            System.out.println(g);
        }
        byte[] d=bb.getBytes();
        int len=bb.length();
        int[] x=new int[len];
        for(int i=0;i<bb.length();i++)x[i]=d[i]-48;
        for(int i=0;i<20;i++)
        {
            for(int j=0;j<20;j++)
            {
                mymap[i][j]=x[c];
                if(mymap[i][j]==5)
                {
                    mx=j;my=i;
                }
                c++;
            }
        }
    }
    int[][] getmap(){return mymap;}
    int getmanX(){return mx;}
    int getmanY(){return my;}
}
