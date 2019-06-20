package producer;

public interface Logger {
    default void print(String str){
        System.out.println(str);
    }
}
