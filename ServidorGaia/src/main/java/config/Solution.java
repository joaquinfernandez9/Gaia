package config;

class Solution {

    static String toCamelCase(String s) {
        String[] words = s.split("_|-");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (i == 0) {
                sb.append(words[i]);
            } else {
                sb.append(words[i].substring(0, 1).toUpperCase());
                sb.append(words[i].substring(1));
            }
        }
        return sb.toString();
    }
}
