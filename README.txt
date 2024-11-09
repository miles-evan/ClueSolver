---ARCHITECTURE OVERVIEW---
   ---Miles Todtfeld---

The Main class is the entry point to the code. You could write your own, but if you just want to play around with the
program, you can use that.

The ClueGame class takes user input to simulate a game by communicating with the clueSolvingAlgorithm class.
The input can refer to cards by their numbers or the first 2 letters.

The clueSolvingAlgorithm class holds an instance of Hands and ClueLogic to hold the information we know.
Then it takes suggestions made by players and updates the information we know.
It also holds an instance of ClueBackTracker, and uses it to find out more information based on the information we know.

The ClueBacktracker holds the same instance of Hands and ClueLogic as the clueSolvingAlgorithm class does.
It has a method to determine if it's possible to set a certain entry to a check or X using backtracking.
It does this by filling in that entry with the check or X, and then it tries to fill in the rest of the entries while following the logical constraints.
If it's impossible to set an entry to a check, for example, then the clueSolvingAlgorithm class would set that entry to an X.\

The Hands class holds the table of checks and Xs.
You can set any entry to a check or X, and it will recursively set other entries to checks or Xs based on the rules.
It will also tell you if you tried to set a check or X somewhere you weren't allowed to. (this is used by the backtracker to determine validity)

The ClueLogic class just holds the information for each player of what cards they could have.
It also holds the information of failed accusations.
I just made this one to make the other code look cleaner.