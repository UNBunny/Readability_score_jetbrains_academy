import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static String readFileToString(String fName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fName)));
    }

    public static void main(String[] args) throws IOException {
        String text = readFileToString(args[0]);

        int countChar = 0, countWords = 0, countSent = 0, countSyllables = 0, coutPolysyllables = 0;
        String[] sentences = Pattern.compile("[?!.][\\s]*").split(text);

        for (String sent : sentences) {
            String[] words = Pattern.compile("\\s").split(sent);
            for (String word : words) {
                int temp = getSyllables(word);
                if (temp > 2) {
                    coutPolysyllables++;
                }
                countSyllables += temp;
            }
            countWords += words.length;
        }
        countChar = text.length() - countWords + 1;
        countSent = sentences.length;

        System.out.println("The text is: " + "\n" + text + "\n");
        System.out.println("Words: " + countWords);
        System.out.println("Sentences: " + countSent);
        System.out.println("Characters: " + countChar);
        System.out.println("Syllables: " + countSyllables);
        System.out.println("Polysyllables: " + coutPolysyllables);

        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String answer = new Scanner(System.in).next();
        int sum = 0;
        if (answer.equals("ARI") || answer.equals("all")) {
            double score = 4.71 * (1.0 * countChar / countWords) + 0.5 * (1.0 * countWords / countSent) - 21.43;
            System.out.printf("\nAutomated Readability Index: %.2f (about " + getAge(score) + " year olds).\n", score);
            sum += getAge(score);
        }
        if (answer.equals("FK") || answer.equals("all")) {
            double score = 0.39 * (1.0 * countWords / countSent) + 11.8 * (1.0 * countSyllables / countWords) - 15.59;
            System.out.printf("Flesch–Kincaid rename.readability tests: %.2f (about " + getAge(score) + " year olds).\n", score);
            sum += getAge(score);
        }
        if (answer.equals("SMOG") || answer.equals("all")) {
            double score = 1.043 * Math.sqrt(coutPolysyllables * (30.0 / countSent)) + 3.1291;
            System.out.printf("Simple Measure of Gobbledygook: %.2f (about " + getAge(score) + " year olds).\n", score);
            sum += getAge(score);
        }
        if (answer.equals("CL") || answer.equals("all")) {
            double score = 0.0588 * (100.0 * countChar / countWords) - 0.296 * (100.0 * countSent / countWords) - 15.8;
            System.out.printf("Coleman–Liau index: %.2f (about " + getAge(score) + " year olds).\n", score);
            sum += getAge(score);
        }
        System.out.printf("\nThis text should be understood in average by %.2f year olds.\n", sum / 4.0);
    }

    public static int getAge(double score) {
        score = Math.ceil(score);
        final int[] age = {5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 24, 25};
        if (score >= age.length - 1) {
            score = age.length - 1;
        }
        return age[(int) score];
    }

    public static int getSyllables(String word) {
        int count = 0;
        boolean isVowel = false;
        for (int i = 0; i < word.length(); i++) {
            char c = Character.toLowerCase(word.charAt(i));
            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y') {
                if (!isVowel) {
                    count++;
                    isVowel = true;
                }
            } else isVowel = false;
        }
        if (word.charAt(word.length() - 1) == 'e') {
            count--;
        }
        return count == 0 ? 1 : count;
    }
}