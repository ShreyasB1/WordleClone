import java.util.*;

public class WordleGame {
    private static final int MAX_ATTEMPTS = 6;
    private static final int WORD_LENGTH = 5;
    private final String targetWord;
    private final Set<String> validWords;

    public WordleGame(Set<String> dictionary) {
        this.validWords = dictionary;
        this.targetWord = selectRandomWord();
    }

    private String selectRandomWord() {
        int index = new Random().nextInt(validWords.size());
        return validWords.stream().skip(index).findFirst().orElse("error");
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            System.out.print("Enter guess #" + (attempts + 1) + ": ");
            String guess = scanner.nextLine().toLowerCase();

            if (guess.length() != WORD_LENGTH || !validWords.contains(guess)) {
                System.out.println("Invalid word. Try again.");
                continue;
            }

            String feedback = getFeedback(guess);
            System.out.println(feedback);
            attempts++;

            if (guess.equals(targetWord)) {
                System.out.println("You guessed it!");
                return;
            }
        }
        System.out.println("Game over! The word was: " + targetWord);
    }

    private String getFeedback(String guess) {
        StringBuilder result = new StringBuilder();
        boolean[] used = new boolean[WORD_LENGTH];

        // First pass: correct positions
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (guess.charAt(i) == targetWord.charAt(i)) {
                result.append("[").append(guess.charAt(i)).append("]");
                used[i] = true;
            } else {
                result.append("_");
            }
        }

        // Second pass: correct letters in wrong position
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (result.charAt(i) != '_') continue;
            char c = guess.charAt(i);
            boolean found = false;

            for (int j = 0; j < WORD_LENGTH; j++) {
                if (!used[j] && c == targetWord.charAt(j)) {
                    found = true;
                    used[j] = true;
                    break;
                }
            }

            result.setCharAt(i, found ? Character.toUpperCase(c) : '.');
        }
        return result.toString();
    }

    public static void main(String[] args) {
        Set<String> words = loadDictionary("words.txt");
        WordleGame game = new WordleGame(words);
        game.play();
    }

    private static Set<String> loadDictionary(String filename) {
        Set<String> words = new HashSet<>();
        try (Scanner sc = new Scanner(new java.io.File(filename))) {
            while (sc.hasNext()) {
                String word = sc.nextLine().trim().toLowerCase();
                if (word.length() == WORD_LENGTH) {
                    words.add(word);
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to load dictionary.");
        }
        return words;
    }
}
