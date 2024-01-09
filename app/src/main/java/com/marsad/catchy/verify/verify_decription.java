package com.marsad.catchy.verify;

public class verify_decription {
    public static String replaceCharacterReplacements(String text) {
        return text.replace("€", "e")
                .replace("€", "e")
                .replace("@", "a")
                .replace("!", "i")
                .replace("1", "i")
                .replace("3", "e")
                .replace("4", "a")
                .replace("?", "i")
                .replace("#", "a")
                .replace("0", "o")
                .replace("ß", "s")
                .replaceAll("[^A-Za-z0-9]", " ");
    }

    public static boolean containsOffensiveWords(String text, double sensitivity, String[] offensiveWords) {
        text = text.toLowerCase();
        text = replaceCharacterReplacements(text);

        text = text.replace(".", "").replace(",", "");

        String[] words = text.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            String word = removeDuplicates(words[i]);
            if (offensiveWordsContains(word, sensitivity, offensiveWords)) return true;

            for (int j = i + 1; j < words.length; j++) {
                word += removeDuplicates(words[j]);
                if (offensiveWordsContains(word, sensitivity, offensiveWords)) return true;
            }
        }

        return false;
    }

    public static boolean offensiveWordsContains(String word, double sensitivity, String[] offensiveWords) {
        for (String offensiveWord : offensiveWords) {
            if (word.equals(offensiveWord)) return true;
            if (word.contains(offensiveWord)) return true;
            double offensive = findSimilarity(word, offensiveWord);
            if (offensive > sensitivity) return true;
        }
        return false;
    }

//    public  double getOffensiveWordPercentage(String text, String[] offensiveWords) {
//        text = text.toLowerCase();
//        text = replaceCharacterReplacements(text);
//        double percentage = 0.0;
//        for (String word : text.split(" ")) {
//            for (String offensiveWord : offensiveWords) {
//                double offensive = findSimilarity(word, offensiveWord);
//                percentage = percentage + offensive;
//            }
//        }
//        percentage = percentage / text.length();
//        return percentage;
//    }

    public static double findSimilarity(String comparedWord, String word) {
        if (comparedWord.equalsIgnoreCase(word)) return 1.0;
        double maxLength = Double.max(comparedWord.length(), word.length());
        if (maxLength > 0) return (maxLength - getLevenshteinDistance(comparedWord, word)) / maxLength;
        return 1.0;
    }

//    public static String replaceOffensiveWords(String message, String replacementBadWord, String replacementInvalidChar, double sensitivity, String... wordList) {
//        String filteredMessage = message;
//
//        filteredMessage = filteredMessage.replaceAll("[^a-zA-Z0-9?!%&/=:;öäüÖÄÜß\"$€´`'@(){}\\-_,.#*\\s]", replacementInvalidChar);
//
//        for (String word : filteredMessage.split(" ")) {
//            for (String offensiveWord : wordList) {
//                double offensive = findSimilarity(word.toLowerCase(), offensiveWord);
//                if (word.toLowerCase().equals(offensiveWord) || offensive > sensitivity) {
//                    String asterisks = new String(new char[word.length()]).replace("\0", replacementBadWord);
//                    filteredMessage = filteredMessage.replaceAll(word, asterisks);
//                }
//            }
//        }
//        return filteredMessage;
//    }


    private static int getLevenshteinDistance(String x, String y) {
        int m = x.length();
        int n = y.length();
        int[][] T = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            T[i][0] = i;
        }
        for (int j = 1; j <= n; j++) {
            T[0][j] = j;
        }
        int cost;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                cost = x.charAt(i - 1) == y.charAt(j - 1) ? 0: 1;
                T[i][j] = Integer.min(Integer.min(T[i - 1][j] + 1, T[i][j - 1] + 1),
                        T[i - 1][j - 1] + cost);
            }
        }
        return T[m][n];
    }

    private static String removeDuplicates(String text) {
        if (text == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        char prev = 0;
        for (char c : text.toCharArray()) {
            if (c != ' ' || prev != ' ') {
                if (c != prev) {
                    sb.append(c);
                    prev = c;
                }
            }
        }
        return sb.toString();
    }
}
