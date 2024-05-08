import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BlackjackGame {
    private Deck deck;
    private Hand playerHand;
    private Hand dealerHand;

    final JFrame frame;
    final JPanel playerPanel;
    final JPanel dealerPanel;
    final JTextArea playerHandTextArea;
    final JLabel playerScoreLabel;
    final JTextArea dealerHandTextArea;
    final JLabel dealerScoreLabel;
    final JButton hitButton;
    final JButton standButton;

    public BlackjackGame() {
        deck = new Deck();
        playerHand = new Hand();
        dealerHand = new Hand();

        // GUI stuff that I definitely didn't have to search up how to do
        frame = new JFrame("Blackjack game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        playerPanel = new JPanel(new BorderLayout());
        dealerPanel = new JPanel(new BorderLayout());

        // The border stuff for the hand panels
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        playerPanel.setBorder(BorderFactory.createTitledBorder(border, "Player"));
        dealerPanel.setBorder(BorderFactory.createTitledBorder(border, "Dealer"));

        playerHandTextArea = new JTextArea(5, 20);
        playerHandTextArea.setEditable(false);
        playerHandTextArea.setLineWrap(true);
        JScrollPane playerScrollPane = new JScrollPane(playerHandTextArea);

        dealerHandTextArea = new JTextArea(5, 20);
        dealerHandTextArea.setEditable(false);
        dealerHandTextArea.setLineWrap(true);
        JScrollPane dealerScrollPane = new JScrollPane(dealerHandTextArea);

        playerPanel.add(playerScrollPane, BorderLayout.CENTER);
        dealerPanel.add(dealerScrollPane, BorderLayout.CENTER);

        playerScoreLabel = new JLabel("Score: ");
        dealerScoreLabel = new JLabel("Score: ");

        JPanel playerInfoPanel = new JPanel(new BorderLayout());
        playerInfoPanel.add(playerScoreLabel, BorderLayout.NORTH);

        JPanel dealerInfoPanel = new JPanel(new BorderLayout());
        dealerInfoPanel.add(dealerScoreLabel, BorderLayout.NORTH);

        playerPanel.add(playerInfoPanel, BorderLayout.SOUTH);
        dealerPanel.add(dealerInfoPanel, BorderLayout.SOUTH);

        hitButton = new JButton("Hit");
        hitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Card newCard = deck.drawCard();
                playerHand.addCard(newCard);
                updateGUI();  // Ensure GUI is updated to display the new card


                // Check if the game should end after the card is dealt
                if (playerHand.calculateScore() > 21) {
                    hitButton.setEnabled(false);
                    determineWinner();
                }

                else if (playerHand.calculateScore() == 21) {
                    dealerTurn();
                    dealerHandTextArea.setText(dealerHand.toString());
                    dealerScoreLabel.setText("Score: " + dealerHand.calculateScore());
                    determineWinner();
                }

            }


        });


        standButton = new JButton("Stand");
        standButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dealerTurn();
                // Update the GUI to reveal the dealer's full hand
                determineWinner();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);

        frame.add(playerPanel, BorderLayout.WEST);
        frame.add(dealerPanel, BorderLayout.EAST);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        dealInitialCards();
        updateGUI();

        frame.pack();
        frame.setSize(600, 600);
        frame.setVisible(true);
    }

    private void dealInitialCards() {
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
    }

    public void updateGUI() {
        playerHandTextArea.setText(playerHand.toString());
        playerScoreLabel.setText("Score: " + playerHand.calculateScore());

        // Show only the first card of the dealer and hide the score
        if (dealerHand.getSize() > 0) {
            Card firstCard = dealerHand.getCard(0);
            dealerHandTextArea.setText(firstCard.toString() + "\n[Hidden Card]");
            dealerScoreLabel.setText("Score: " + firstCard.getValue());
        } else {
            dealerHandTextArea.setText("");
            dealerScoreLabel.setText("Score: ");
        }
    }

    private void dealerTurn() {
        while (dealerHand.calculateScore() < 17) {
            dealerHand.addCard(deck.drawCard());
        }
        // Update GUI to show full hand after dealer's turn
        dealerHandTextArea.setText(dealerHand.toString());
        dealerScoreLabel.setText("Score: " + dealerHand.calculateScore());
    }

    private void determineWinner() {
        int playerScore = playerHand.calculateScore();
        int dealerScore = dealerHand.calculateScore();

        String resultMessage;
        if (playerScore > 21) {
            resultMessage = "Bust! You lose.";
        } else if (dealerScore > 21 || playerScore > dealerScore) {
            resultMessage = "You win!";
        } else if (playerScore == dealerScore) {
            resultMessage = "It's a tie.";
        }
          else {
              resultMessage = "Dealer wins.";
          }

        JOptionPane.showMessageDialog(frame, resultMessage, "Game Result", JOptionPane.INFORMATION_MESSAGE);
        //restartGame();
        SwingUtilities.invokeLater(this::restartGame);  // Ensure restart is also delayed to avoid immediate prompts
    }




    private void restartGame() {
        //Reset deck and hands
        deck = new Deck();
        playerHand = new Hand();
        dealerHand = new Hand();

        dealInitialCards();

        hitButton.setEnabled(true);

        // Ensure the GUI is reset, including hiding the dealer's score
        updateGUI();
    }
}