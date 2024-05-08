import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Card {    //creates Card class
    private final String suit;    //creates String to display card suit (D, H, C, S)
    private final String rank;    //Ace, Jack, Queen, King, 10, 9, 8 etc
    private int value;  //value of rank in Blackjack

    public Card(String suit, String rank) { //
        this.suit = suit;
        this.rank = rank;
        determineInitialValue();    //determines value of cards
    }

    private void determineInitialValue() {
        switch (rank) {
            case "A":
                this.value = 11; // Default value of Ace = 11
                break;
            case "J":
            case "Q":
            case "K":
                this.value = 10;    //J, Q, K = 10
                break;
            default:
                this.value = Integer.parseInt(rank);    //10, 9, 8-2
        }
    }

    public String getRank() {   //gets the rank of current card
        return rank;
    }

    public int getValue() { //gets the value of current card
        return value;
    }


    @Override
    public String toString() {  //Displayed in GUI for player and dealer (ex: 4 of Spades)
        return rank + " of " + suit;
    }
}

class Deck {    //class for the deck of cards
    private final List<Card> cards; //list of individual cards of type "Card"

    public Deck() {
        cards = new ArrayList<>();  //cards is an ArrayList
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"}; //String array of suits
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};    //String array of ranks

        for (String suit : suits) { //for a suit in suits array
            for (String rank : ranks) { //for a rank in ranks array
                cards.add(new Card(suit, rank));    //add a card object to the cards list with the corresponding suit and rank of a card
            }
        }
        Collections.shuffle(cards); //randomize cards
    }

    public Card drawCard() {    //draws a random card from a shuffled deck of cards
        return cards.removeFirst();
    }
}

class Hand {    //class for the hand of player and dealer
    private final List<Card> cards; //creates a list of type Card named "cards"

    public Hand() { //constructor that initializes a new Array List for cards
        cards = new ArrayList<>();
    }

    public void addCard(Card card) {    //adds Card parameter object to 'cards' list
        cards.add(card);
    }


    //public void handleAceValue(Card aceCard) {  //handles Ace value for Player
    //     if (!"A".equals(aceCard.getRank())) return; //if card is not an Ace, return to function called
    //     int currentScore = calculatePlayerScore(true) - aceCard.getValue(); // Subtracting because Ace is initially 11
    //     if (currentScore + 11 > 21) {
    //         aceCard.setValue(1); // Change Ace to 1 if 11 would cause bust
    //         JOptionPane.showMessageDialog(null, "Ace is counted as 1 to avoid bust.");
    //     } else {
    //         Object[] options = {"11", "1"};
    //         int response = JOptionPane.showOptionDialog(null, "Choose the value for Ace. Your current score is " + currentScore + ".\nDo you want the Ace to count as 11 or 1?", "Choose Ace Value",
    //                 JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    //         aceCard.setValue(response == JOptionPane.YES_OPTION ? 11 : 1);
    //
    //         //updateGUIInitial();
    //     }
    //     //updateGUIInitial();
    // }

    //public int calculatePlayerScore() {
    //     int score = 0;
    //     for (Card card : cards) {
    //         score += card.getValue();
    //     }
    //     return score;
    // }

    //public int calculatePlayerScore(boolean ignoreUnconfirmedAces) {
    //     int score = 0;
    //     int numAces = 0;
    //     for (Card card : cards) {
    //         if (card.getRank().equals("A")) {
    //             numAces++;  // Count aces to adjust later if needed
    //             if (!ignoreUnconfirmedAces) {
    //                 score += card.getValue(); // Normally count the Ace as its current value
    //             } else if (card.getValue() != 1) { // Only ignore Ace if it's still set to default 11
    //                 // This condition ensures we only ignore Ace when it's not yet confirmed
    //                 continue; // Skip adding the Ace's value to the score
    //             } else {
    //                 score += 1; // Add as 1 if already confirmed to 1
    //             }
    //         } else {
    //             score += card.getValue();
    //         }
    //     }
    //     // Adjust score for Aces if necessary
    //     while (numAces > 0 && score > 21 && !ignoreUnconfirmedAces) {
    //         score -= 10; // Change an Ace from 11 to 1
    //         numAces--;
    //     }
    //     return score;
    // }

    public int calculateScore() {
        int score = 0;
        int numAces = 0;
        for (Card card : cards) {
            score += card.getValue();
            if (card.getRank().equals("A")) {
                numAces++;
            }
        }
        while (numAces > 0 && score > 21) {
            score -= 10;
            numAces--;
        }
        return score;
    }

    @Override
    public String toString() {
        StringBuilder handString = new StringBuilder();
        for (Card card : cards) {
            handString.append(card).append("\n");
        }
        return handString.toString();
    }

    public int getSize() {
        return cards.size();
    }

    public Card getCard(int index) {
        if (index >= 0 && index < cards.size()) {
            return cards.get(index);
        }
        return null;
    }

    public static void main(String[] args) {
        new BlackjackGame();
    }
}