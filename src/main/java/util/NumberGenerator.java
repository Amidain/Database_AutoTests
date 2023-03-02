package util;

import java.security.SecureRandom;

public class NumberGenerator {

    public static int generateRandomNumberBetweenOneToNine(){
        final int UPPER_BOUND = 9;
        SecureRandom random = new SecureRandom();
        int digit = random.nextInt(UPPER_BOUND);

        while(digit == 0){
             digit = random.nextInt(UPPER_BOUND);
        }

        return digit;
    }
}
