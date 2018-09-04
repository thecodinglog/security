package learning;

import org.junit.Ignore;
import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 30.
 */
@Ignore
public class RegularExpressionTest {
    @Test
    public void rexPractice1() {
        System.out.println("rexPractice1");
        long start = Instant.now().toEpochMilli();

        for (int i = 0; i < 100000; i++) {

            reg1();
        }

        long end = Instant.now().toEpochMilli();

        long elapsedTime = end - start;

        System.out.println(elapsedTime);

    }
    @Test
    public void rexPractice2() {
        System.out.println("rexPractice2");
        long start = Instant.now().toEpochMilli();

        for (int i = 0; i < 100000; i++) {

            reg2();
        }

        long end = Instant.now().toEpochMilli();

        long elapsedTime = end - start;

        System.out.println(elapsedTime);

    }

    @Test
    public void rexPractice3() {
        System.out.println("rexPractice3");
        long start = Instant.now().toEpochMilli();

        for (int i = 0; i < 100000; i++) {

            reg3();
        }

        long end = Instant.now().toEpochMilli();

        long elapsedTime = end - start;

        System.out.println(elapsedTime);

    }

    private void reg1() {
        String sample = "search,/find.*/";
        String input = "find3";

        String[] keywords = sample.split(",");

        for (int i = 0; i < keywords.length; i++) {
            if (keywords[i].startsWith("/") && keywords[i].endsWith("/")) {
                String rex = keywords[i].substring(1, keywords[i].length() - 1);
                Pattern pattern = Pattern.compile(rex);
                Matcher matcher = pattern.matcher(input);

            }else{
                keywords[i].equals(input);
            }
        }
    }
    private void reg2() {
        String sample = "search,/find.*/";
        String input = "find3";

        String[] keywords = sample.split(",");
        boolean b;

        for (int i = 0; i < keywords.length; i++) {
            if (keywords[i].startsWith("/") && keywords[i].endsWith("/")) {
                String rex = keywords[i].substring(1, keywords[i].length() - 1);
                b = input.matches(rex);

            }else {
                b = keywords[i].equals(input);
            }
        }

    }
    private void reg3() {
        String sample = "search,/find.*/";
        String input = "find3";

        String[] keywords = sample.split(",");
        boolean b = Arrays.stream(keywords).anyMatch(s -> {
            if (s.startsWith("/") && s.endsWith("/")) {
                String rex = s.substring(1, s.length() - 1);
                return input.matches(rex);

            } else {
                return s.equals(input);
            }
        });



    }
}
