package com.jano;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        String txt = readFileAsString(args[0]);

        System.out.println("The text is:");
        System.out.println(txt + "\n");
        readabilityScore(txt);


    }

    public static void readabilityScore(String txt) {
        Scanner scanner = new Scanner(System.in);

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);

        int numOfWords = countWords(txt);
        int numOfSentences = countSentences(txt);
        int numOfCharacters = countCharacters(txt);
        int numOfSyllables = countSyllables(txt);
        int numOfPolysyllables = countPolysyllables(txt);

        System.out.println("Words: " + numOfWords);
        System.out.println("Sentences: " + numOfSentences);
        System.out.println("Characters: " + numOfCharacters);
        System.out.println("Syllables: " + numOfSyllables);
        System.out.println("Polysyllables: " + numOfPolysyllables);

        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String input = scanner.nextLine();
        switch (input.toUpperCase()) {
            case "ARI": {
                double scoreARI = calculateARIScore(numOfWords, numOfSentences, numOfCharacters);
                System.out.print("\nAutomated Readability Index: " + decimalFormat.format(scoreARI));
                System.out.println(" (about " + evaluateScore(scoreARI) + ")");
                break;
            }
            case "FK": {
                double scoreFK = calculateFKScore(numOfWords, numOfSentences, numOfSyllables);
                System.out.print("\nFlesch–Kincaid readability tests: " + decimalFormat.format(scoreFK));
                System.out.println(" (about " + evaluateScore(scoreFK) + ")");
                break;
            }
            case "SMOG": {
                double scoreSMOG = calculateSMOGScore(numOfPolysyllables, numOfSentences);
                System.out.print("\nSimple Measure of Gobbledygook: " + decimalFormat.format(scoreSMOG));
                System.out.println(" (about " + evaluateScore(scoreSMOG) + ")");
                break;
            }
            case "CL": {
                double scoreCL = calculateCLScore(numOfWords, numOfSentences, numOfCharacters);
                System.out.print("\nColeman–Liau index: " + decimalFormat.format(scoreCL));
                System.out.println(" (about " + evaluateScore(scoreCL) + ")");
                break;
            }
            case "ALL": {
                double scoreARI = calculateARIScore(numOfWords, numOfSentences, numOfCharacters);
                System.out.print("\nAutomated Readability Index: " + decimalFormat.format(scoreARI));
                System.out.println(" (about " + evaluateScore(scoreARI) + ")");
                double scoreFK = calculateFKScore(numOfWords, numOfSentences, numOfSyllables);
                System.out.print("Flesch–Kincaid readability tests: " + decimalFormat.format(scoreFK));
                System.out.println(" (about " + evaluateScore(scoreFK) + ")");
                double scoreSMOG = calculateSMOGScore(numOfPolysyllables, numOfSentences);
                System.out.print("Simple Measure of Gobbledygook: " + decimalFormat.format(scoreSMOG));
                System.out.println(" (about " + evaluateScore(scoreSMOG) + ")");
                double scoreCL = calculateCLScore(numOfWords, numOfSentences, numOfCharacters);
                System.out.print("Coleman–Liau index: " + decimalFormat.format(scoreCL));
                System.out.println(" (about " + evaluateScore(scoreCL) + ")");
                System.out.println("\nThis text should be understood in average by " + decimalFormat.format(calculateAverageScore(scoreARI, scoreFK, scoreSMOG, scoreCL)) + "-year-olds.");
                break;
            }
            default:
                break;
        }
    }

    public static String evaluateScore(double score) {
        if (score >= 1 && score < 2) {
            return "6-year-olds";
        } else if (score >= 2 && score < 3) {
            return "7-year-olds";
        } else if (score >= 3 && score < 4) {
            return "9-year-olds";
        } else if (score >= 4 && score < 5) {
            return "10-year-olds";
        } else if (score >= 5 && score < 6) {
            return "11-year-olds";
        } else if (score >= 6 && score < 7) {
            return "12-year-olds";
        } else if (score >= 7 && score < 8) {
            return "13-year-olds";
        } else if (score >= 8 && score < 9) {
            return "14-year-olds";
        } else if (score >= 9 && score < 10) {
            return "15-year-olds";
        } else if (score >= 10 && score < 11) {
            return "16-year-olds";
        } else if (score >= 11 && score < 12) {
            return "17-year-olds";
        } else if (score >= 12 && score < 13) {
            return "18-year-olds";
        } else if (score >= 13 && score < 14) {
            return "24-year-olds";
        } else if (score > 14) {
            return "24+year-olds";
        } else {
            return "";
        }
    }

    public static double calculateAverageScore(double scoreARI, double scoreFK, double scoreSMOG, double scoreCL) {
        String evaluatedARI = evaluateScore(scoreARI);
        String evaluatedFK = evaluateScore(scoreFK);
        String evaluatedSMOG = evaluateScore(scoreSMOG);
        String evaluatedCL = evaluateScore(scoreCL);
        double sARI = scoreStringEvaluation(scoreARI, evaluatedARI);
        double sFK = scoreStringEvaluation(scoreFK, evaluatedFK);
        double sSMOG = scoreStringEvaluation(scoreSMOG, evaluatedSMOG);
        double sCL = scoreStringEvaluation(scoreCL, evaluatedCL);
        return (sARI + sFK + sSMOG + sCL) / 4;
    }

    public static double scoreStringEvaluation(double score, String evaluatedScore) {
        if (score < 4) {
            return Double.parseDouble(evaluatedScore.substring(0, 1));
        } else {
            return Double.parseDouble(evaluatedScore.substring(0, 2));
        }
    }

    public static double calculateARIScore(int numOfWords, int numOfSentences, int numOfCharacters) {
        return 4.71 * ((double) numOfCharacters / numOfWords) + 0.5 * ((double) numOfWords / numOfSentences) - 21.43;
    }

    public static double calculateFKScore(int numOfWords, int numOfSentences, int numOfSyllables) {
        return 0.39 * ((double) numOfWords / numOfSentences) + 11.8 * ((double) numOfSyllables / numOfWords) - 15.59;
    }

    public static double calculateSMOGScore(int numOfPolysyllables, int numOfSentences) {
        return 1.043 * Math.sqrt((double) numOfPolysyllables * 30 / numOfSentences) + 3.1291;
    }

    public static double calculateCLScore(int numOfWords, int numOfSentences, int numOfCharacters) {
        double averageLetters100 = (double) numOfCharacters / numOfWords * 100;
        double averageSentences100 = (double) numOfSentences / numOfWords * 100;
        return 0.0588 * averageLetters100 - 0.296 * averageSentences100 - 15.8;
    }

    public static int countWords(String txt) {
        return txt.split("(\\s)+").length;
    }

    public static int countSentences(String txt) {
        return txt.split("[!?.]").length;
    }

    public static int countCharacters(String txt) {
        return txt.replace(" ", "").length();
    }

    public static int countSyllables(String txt) {
        int counter = 0;
        String temp = txt.replaceAll("[.,!?]", "");
        String[] words = temp.toLowerCase().split("(\\s)+");

        for (String word : words ) {
            counter += getNumVowels(word);
            if (getNumVowels(word) == 0) {
                counter++;
            }
        }
        return counter;
    }

    public static int countPolysyllables(String txt) {
        int counter = 0;
        String temp = txt.replaceAll("[.,!?]", "");
        String[] words = temp.toLowerCase().split("(\\s)+");

        for (String word : words) {
            if (getNumVowels(word) > 2) {
                counter++;
            }
        }
        return counter;
    }

    private static int getNumVowels(String word) {
        int counter = 0;
        String regex = "([ayeiou]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(word.toLowerCase());

        while (matcher.find()) {
            counter++;
        }

        if (word.endsWith("e")) {
            counter--;
        }
        return counter;
    }

    public static String readFileAsString(String filename) {
        Scanner scanner;
        StringBuilder output = new StringBuilder();
        try {
            scanner = new Scanner (new FileReader(filename));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                output.append(line.trim());
            }
        } catch (FileNotFoundException e) {
            System.out.println("");
        }
        return output.toString();
    }
}
