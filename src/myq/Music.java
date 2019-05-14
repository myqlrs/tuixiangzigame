package myq;

/**
 * @author 孟赟强
 * @date 2019/5/14-9:20
 **/
public class Music {
    String name;
    String fileName;

    public Music(String name, String fileName){
        this.name=name;
        this.fileName=fileName;
    }

    @Override
    public String toString() {
        return "Music{" +
                "name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
