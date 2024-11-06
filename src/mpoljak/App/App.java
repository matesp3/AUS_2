package mpoljak.App;

public class App {

    public static void main(String[] args) {
        for (int i = 10; i < 100; i+=10) {
            for (int s = 10; s < (100 - i); s+=10) {
                int d = (100 - i -s);
                System.out.println(String.format("i=%.1f , s=%.1f , d=%.1f", i/100.0, s/100.0, d/100.0));
                System.out.println((i+s+d)+"\n");
            }
        }
    }


}
