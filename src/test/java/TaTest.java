import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TaTest {

    private ByteArrayOutputStream outStream;

    public static String ExpectedOutput(List<Integer> inputList) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        int testCount = inputList.get(index++);

        for (int t = 0; t < testCount; t++) {
            int len = inputList.get(index++);
            List<Integer> sublist = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                sublist.add(inputList.get(index++));
            }
            Collections.sort(sublist);
            int median = sublist.get(len / 2);
            sb.append(median).append(" ");
        }

        return sb.toString().trim();
    }

    public static List<Integer> generateRandomTestNumbers() {
        Random random = new Random();
        List<Integer> input = new ArrayList<>();
        int count =  1 + random.nextInt(3)
        input.add(testCount);

        for (int i = 0; i < testCount; i++) {
            int len = 1 + 2 * random.nextInt(5);
            input.add(len);

            for (int j = 0; j < len; j++) {
                int value = 1 + random.nextInt(1_000_000);
                input.add(value);
            }
        }

        return input;
    }

    public void baseTest(List<Integer> inputList, String correctResult) {
        Process p;

        try {
            StringBuilder inputBuilder = new StringBuilder();
            for (int num : inputList) {
                inputBuilder.append(num).append("\\n");
            }

            if (inputBuilder.length() >= 2) {
                inputBuilder.setLength(inputBuilder.length() - 2);
            }

            String echoInput = "echo -e \"" + inputBuilder.toString() + "\"";
            String fullCmd = echoInput + " | java -jar lib/rars.jar nc src/main/java/solution.s";

            String[] cmd = {"/bin/bash", "-c", fullCmd};

            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String result = br.readLine().trim();
            br.close();

            assertEquals(correctResult, result);
            p.destroy();

        } catch (Exception e) {
            System.err.println("Execution error: " + e.getMessage());
            fail();
        }
    }

    @Before
    public void initStreams() {
        outStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outStream));
    }

    @Test
    public void test1() {
        List<Integer> input = generateRandomTestNumbers();
        String correctOutput = ExpectedOutput(input);
        baseTest(input, correctOutput);
    }
}
