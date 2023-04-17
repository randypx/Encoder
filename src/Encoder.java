import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Random;

public class Encoder {
    private static ReferenceTable referenceTable = new ReferenceTable();

    public static void main(String[] args) throws IOException{

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] input = reader.readLine().split(" ", 2);
        String modeSelected = input[0];

        switch (modeSelected) {
            case "ENCODE": {
                System.out.println(encodeMessage(input[1]));
                break;
            }

            case "DECODE": {
                System.out.println(decodeMessage(input[1]));
                break;
            }

            default: {
                System.out.printf("\"%s\" is not allowed.%n " +
                        "Please start with ENCODE or DECODE.%n", modeSelected);
                break;
            }
        }

        reader.close();
    }

    private static String encodeMessage(String plainText) {
        return encodeMessage(plainText, referenceTable.getOffsetNo());
    }

    private static String encodeMessage(String plainText, int offset) {
        StringBuilder stringBuilder = new StringBuilder(plainText.length() + 1);
        stringBuilder.append(referenceTable.getChar(offset));
        for (char c : plainText.toCharArray()) {
            if (c == ' ') {
                stringBuilder.append(' ');
            }
            else if (!referenceTable.hasChar(c)) {
                stringBuilder.append(c);
            }
            else {
                stringBuilder.append(referenceTable.getOffsetChar(c, offset));
            }
        }
        return  stringBuilder.toString();
    }

    private static String decodeMessage(String encodedText) {
        StringBuilder stringBuilder = new StringBuilder(encodedText.length() - 1);
        int offset = referenceTable.getCharNo(encodedText.charAt(0));
        for (char c : encodedText.substring(1).toCharArray()) {
            if (c == ' ') {
                stringBuilder.append(' ');
            }
            else if (!referenceTable.hasChar(c)) {
                stringBuilder.append(c);
            }
            else {
                stringBuilder.append(referenceTable.getOffsetChar(c, -offset));
            }
        }
        return stringBuilder.toString();
    }

}

class ReferenceTable {
    private static final int TOTAL_CHAR_NO = 44;

    private final char[] characterList = {'A', 'B', 'C', 'D', 'E', 'F','G','H', 'I', 'J', 'K','L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '(',
            ')', '*', '+', ',', '-', '.', '/'};

    private HashMap<Character, Integer> charToInt = new HashMap<>();
    private Random random = new Random();

    ReferenceTable() {
        for (int x = 0; x < characterList.length; x++) {
            charToInt.put(characterList[x], x);
        }
    }

    public boolean hasChar(char character) {
        return charToInt.containsKey(character);
    }

    public char getChar(int num) {
        return characterList[num];
    }

    public int getCharNo(char character) {
        return charToInt.get(character);
    }

    public char getOffsetChar(char charToOffset, int offset) {
        if (offset < 0) {
            offset += TOTAL_CHAR_NO;
        }
        return characterList[(charToInt.get(charToOffset) + offset) % TOTAL_CHAR_NO];
    }

    public int getOffsetNo() {
        return random.nextInt(TOTAL_CHAR_NO);
    }

}
