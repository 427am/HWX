# FSU Texas Hold'em and Blackjack

Created by:
Maddy Burns (mrb23g)
Jake Bertorello (jmb23a)

Project Description:
This project is a graphical Java application that simulates two casino-style card games:
Texas Hold'em Poker and Blackjack. Both games include custom FSU-themed card and chip
graphics, multiple players, betting systems, and complete rule implementations.

How to Compile:
javac -cp src -d out $(find src -name "*.java")

How to Run:
java -cp out blackjack.Main
java -cp out poker.Main

Required Java Version:
Java SE 24


---------------------------------------------------------------------
Texas Hold'em Overview
---------------------------------------------------------------------

This project includes a simplified implementation of Texas Hold’em Poker featuring:
-> 4 players by default, with the ability to adjust the number of players in code.
-> Unlimited rounds, allowing continuous play as long as players have chips.
-> Standard Texas Hold’em rules, including:
    > Each player is dealt two private hole cards.
    > Five community cards are revealed across the flop, turn, and river.
    > Hands are ranked using standard Texas Hold’em rankings.
    > The pot is awarded to the player with the best 5-card combination.

---------------------------------------------------------------------
Blackjack Overview
---------------------------------------------------------------------

This project includes a simplified implementation of Blackjack designed to follow
typical casino rules while maintaining intuitive gameplay.

-> 4 players by default (adjustable), playing against a dealer.
-> Unlimited rounds as long as the player has chips.
-> Standard Blackjack rules, such as:
    > Players receive two cards; the dealer shows one and hides one.
    > Players may Hit or Stand to reach a score close to 21 without busting.
    > Dealer hits until reaching 17 and stands on 17 or above.
    > Aces automatically count as 1 or 11 depending on the best outcome.
    > Standard win/lose comparisons and natural Blackjack detection.

---------------------------------------------------------------------
How to Play Texas Hold'em (After Compilation)
---------------------------------------------------------------------

To start the game:
java -cp out poker.Main

Gameplay Flow:
-> The player to the right of the Big Blind begins their turn using the "Start Turn" button.
-> On each turn, players may choose one of the following actions:
        - Fold: Forfeit the current hand.
        - Call: Match the current highest bet.
        - Bet/Raise: Increase the wager (if betting is open).
        - Check: Pass without betting when no bet exists.
-> After choosing an action, players must press "End Turn" to finalize the move.
-> The next player then presses "Start Turn" to begin their turn.

Round Progression:
-> Once all active players have matched the bet (or checked), community cards are revealed:
        - Flop  : First three cards shown.
        - Turn  : Fourth card.
        - River : Fifth and final card.
-> Players continue taking turns and betting between each reveal.

Showdown:
-> After the final betting round, remaining players go to a showdown.
-> The system evaluates all hands, determines the strongest 5-card combination,
   and awards the pot to the winning player.

Continuing the Game:
-> After the winner is determined, press the "Play Again" button to begin a new hand.
-> Players may keep playing indefinitely or quit at any time.

---------------------------------------------------------------------
How to Play Blackjack (After Compilation)
---------------------------------------------------------------------

To start the game:
java -cp out blackjack.Main

Gameplay Overview:
-> Both the player and dealer begin with two cards.
-> The dealer shows one card face up and keeps one card face down.
-> The player's goal is to reach as close to 21 as possible without going over.

Player Actions:
-> Hit  : Draw another card.
-> Stand: End the turn and keep the current hand.

Turn Flow:
-> The player clicks Hit or Stand on their turn.
-> If Hit is chosen, a new card is dealt immediately.
-> If Stand is chosen, control moves to the dealer.

Dealer Rules:
-> The dealer reveals their hidden card after the player stands.
-> The dealer draws until reaching at least 17.

Winning and Losing:
-> Player busts (over 21) = automatic loss.
-> Dealer busts = automatic player win.
-> If neither busts, the higher total wins.
-> Natural Blackjack is recognized when applicable.

Continuing the Game:
-> After each round, chips are updated and a new game may begin.
-> Players may stop playing at any time.

---------------------------------------------------------------------
Code & Class Design Overview
---------------------------------------------------------------------

This project uses a modular, object-oriented architecture with clear separation between
game logic (model) and graphical interface (view). Both games follow the same design
principles while maintaining independent package structures.

Package Overview:
-> blackjack.model  : Core Blackjack logic and rule processing.
-> blackjack.view   : Blackjack GUI rendering and user interaction.
-> poker.model      : Core Texas Hold'em logic, dealing, betting, and evaluation.
-> poker.view       : Poker GUI rendering and user interface interactions.
-> blackjack        : Main entry point for the Blackjack game.
-> poker            : Main entry point for the Texas Hold'em game.

Major Class Responsibilities:

Card:
-> Represents a playing card with suit and rank.
-> Provides card properties used by both games.

Deck:
-> Stores and manages a full deck of cards.
-> Handles shuffling and drawing operations.

Player:
-> Represents a player in Blackjack or Poker.
-> Stores hand, chip count, bets, and gameplay status.

Dealer:
-> Specialized player class for Blackjack.
-> Implements standard dealer rules (hit until 17, etc.).

GameState:
-> Stores and updates the current state of a game round.
-> Manages phases such as dealing, betting, card reveals, and round outcomes.

HandEvaluator:
-> Evaluates hand strengths.
-> For Poker: determines the best 5-card hand.
-> For Blackjack: calculates totals, adjusts Aces, and checks for Blackjack or bust.

---------------------------------------------------------------------
Resources
---------------------------------------------------------------------

-> All card and chip graphics were created by Maddy Burns using official Florida
   State University logos and a seal from the State of Florida.

---------------------------------------------------------------------
Extra Features
---------------------------------------------------------------------

-> FSU-branded custom playing cards and chips.
-> Two complete games included: Texas Hold'em and Blackjack.
-> Fully custom graphical interface for both games.
