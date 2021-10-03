# Double-Dash-Scoring-System

Here is the game. My friend David and I play Mario Kart Double Dash on the Nintendo GameCube. We have a competition that involves playing all 16 tracks. However, we have a bit of a unique scoring system. First, we play the tracks in a random order. On the first track, the winner gets 1 point. On the next track, the winner gets 2 points. This pattern goes on all the way to the last track where the winner of the last race gets 16 points. The point value increases by 1 each race. Doing the calculations, there are 1+2+3...+16 = 16*17/2 = 136 total points possible. This means that both players could tie with 68 points or one player wins once they achieve 69 points or greater. David and I do this competition about every year and we think we are about evenly matched.


The question we had that sparked me to do this project is "What is the probability that we tie assuming we are evenly matched?" For this, we need to know the amount of ways to add any combination of numbers 1,...,16 with no repeats to get 68 and then divide that by 2^16. For example, if one were to win races 1, 3, 4, 8, 10, 13, 14, and 15, each player would have 16 points resulting in a tie. However, finding every combination of that happening is too difficult of a problem. Therefore, I big brained a way to do it with code. 



