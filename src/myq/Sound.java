package myq;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.URL;

/**
 * @author 孟赟强
 * @date 2019/5/14-9:21
 **/
public class Sound {
    //存储音乐文件路径
    String path=new String("musics\\");
    //存储音乐文件名称
    String  file=new String("红昭愿.wav");
    //判断音乐开关
    AudioClip loadSound()
    {
        try {
            File f = new File(path+file);
            URL url = f.toURI().toURL();
            AudioClip au = Applet.newAudioClip(url);
            return au;
        } catch (Exception e) {
            e.printStackTrace();}
        return null;
    }
    //设置打开文件名，用来具体打开某个文件
    void setMusic(String e){file=e;}
}
