---Miles Todtfeld---

This program is designed take in information about what happens in a game of Clue, and determines what cards people have in their hands.

After each turn, the program will output two things. It will output a table of what cards each player definitely does (check) or doesn't (X) have. It will also output the "logic" of what cards each player could have. For example (gr ca ba) means they have green, candlestick, or ballroom. When inputing cards, you could use the first two letters abreviations or the card's number, but it is easier to use the abreviations.

How to run it:
Run Main class with these command arguments:
If you want to input the moves of a game yourself: play -1
If you want to take input moves from a file (sampleMoves1 for example): play data/sampleMoves1.txt -1
If you want to add a time limit to how long it can run the algorithm for, replace "-1" with the time limit in seconds. For the large majority of cases, this isn't necessary.


---ARCHITECTURE OVERVIEW---

The Main class simply controls the ClueGame class.

The ClueGame class takes user input to simulate a game by communicating with the clueSolvingAlgorithm class.
The input can refer to cards by their numbers or the first 2 letters.

The clueSolvingAlgorithm class holds an instance of Hands and ClueLogic to hold the information we know.
Then it takes suggestions made by players and updates the information we know.
It also holds an instance of ClueBackTracker, and uses it to find out more information based on the information we know.

The ClueBacktracker holds the same instance of Hands and ClueLogic as the clueSolvingAlgorithm class does.
It has a method to determine if it's possible to set a certain entry to a check or X using backtracking.
It does this by filling in that entry with the check or X, and then it tries to fill in the rest of the entries while following the logical constraints.
If it's impossible to set an entry to a check, for example, then the clueSolvingAlgorithm class would set that entry to an X.

The Hands class holds the table of checks and Xs.
You can set any entry to a check or X, and it will recursively set other entries to checks or Xs based on the rules.
It will also tell you if you tried to set a check or X somewhere you weren't allowed to. (this is used by the backtracker to determine validity)

The ClueLogic class just holds the information for each player of what cards they could have.
It also holds the information of failed accusations.
I just made this one to make the other code look cleaner.
