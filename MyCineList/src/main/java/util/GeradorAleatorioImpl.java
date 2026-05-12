package util;

import java.util.Random;

public class GeradorAleatorioImpl implements GeradorAleatorio{

    private final Random random;

    public GeradorAleatorioImpl(){
        this.random = new Random();
    }

    @Override
    public int sortearInteiro(int min, int max) {
        if (min == max){
            return min;
        }
        return min + random.nextInt(max - min + 1);
    }
}
