# Day 1 - Sonar Sweep

As usual, AoC begins with easy tasks, so everybody is warming up and feel happy for little achievements.

Yor are receiving signals and should count how many times measurements are increasing. In first part you check 
just `N`th and `N-1`th elements, but for the second part you deal with whole sliding window of size 3. However, I love that
task, because part 1 can be solved as counting increasing sliding window of size 1.

First I've implemented that sliding window
by myself, but then I've got acquainted with function [`windowed`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/windowed.html) 
from Kotlin and solution became even more simple and nice.

# Day 2 - Dive!
Now you're controlling a submarine, that has 3 commands: `up`, `down` and `forward`. After each command there's a number
of units of that action. Feels like a small program itself. Result should be the multiplication of horizontal position and
depth after last command. Part 1 and 2 has different effect on those numbers for each command.

Actually the solution is trivial: iterate over commands and accumulate several values based on the rules. The easiest first
part boils down to the fact that you need to compute sums of units per command and compute final value. When I've been implementing
in that way I've learned cool [`groupingBy`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/grouping-by.html) function,
that creates groping iterator instead of materializing map.

# Day 3 - Binary Diagnostic

In part 1 you can compute second value just inverting the first one, used bitwise operators in Kotlin. For the second part
I was thinking about prefix tree, however it wasn't needed at all and task was solved with recursive DFS. Also, for the part 2
I've used lambdas, syntax feels strange.

# Day 4 - Giant Squid ~~Game~~

Took some more code to solve, but once again both parts can be completed with common solution, is you return Iterator. For
each board I have arrays to store how many cells were marked at each row and column, thus deciding if board won at constant time (with linear space).
Cells are stored in Map<{number},{list of cells}> and each cell has an Id of a board, so it's easy to mark them and to find a winner.
Boards that won are stored in set, so we don't check cells from them in further iterations.

Btw TIL the [`sequence`](https://kotlinlang.org/docs/sequences.html#from-chunks) function in Kotlin.

# Day 5 - Hydrothermal Venture

Ah, that AoC reminds me [Subnautica](https://store.steampowered.com/app/264710/Subnautica/) The hardest part was to compute a sequence of coordinates that form a line. After that - it's just trivial grouping and
counting overlapped points. Tried to implement custom `rangeTo` operator to use it for [progression](https://kotlinlang.org/docs/ranges.html#progression) 
of points on the line.

# Day 6 - Lanternfish

Brute-force solution definitely will lead to OOM, cause fish are growing exponentially. The key to solve it is grouping 
them by counter value, which is just an array of 9 `int64` numbers. Then on each day count how many fish reached `0`,
shift left the array, set newborns to last element (`8`) and add the same value to the `6th` element (counter reset).
Voilà, nice-looking and fast solution! However, no cool `Kotlin` stuff for today.

# Day 7 - The Treachery of Whales

Noting interesting about the solution. For first one the target position is median, but for the second it appears to be
an average. The main problem was with this average, which for test data should be rounded from 4.9 -> 5.0, but for my input 
we should take floor.

# Day 8 - Seven Segment Search

Today was a grate day to write down lots of code for finding which 7-segments digits from corrupted input. The first part
was solved with trivial count of how many digits were triggered and count 1, 4, 7 and 8 digits, luckily the all have unique
number of segments.

For the second pard we should find all digits, so solution was divided into steps:
1. Finding sets of segments for 1, 4, 7 and 8, because it's an easy start.
2. Counting how many digits use each segment.
3. Use that information to find each segment, keeping in mind some sort of normalized representation of them.
4. Combining found segments to find each digit.

### Update
Turns out that part 2 can be elegantly solved with binary masks. We can convert all signal string to normalized binary representation
(`cdfeb` will become `0111110`). Next we group all digits by the number of activated bits using [`countOneBits`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/count-one-bits.html).
Using this map we can easily find signals for 1, 4, 8 and 8, because they had unique numbers of segments. So we shall deal with the rest.

First group is digits with 5 segments - `2`, `3` and `5`. Digits `3` and `1` has 2 segments in common, while others have 1.
Digits `2` and `4` has 2 common segments, while `5` and `4` has 3.

Second group is digits with 6 segments - `0`, `6` and `9`. Digits `6` and `1` has 1 segment in common, while others have 2.
Digits `0` and `4` has 3 common segments, while `9` and `4` has 4.

# Day 9

Today we should recall Breadth First Search! But first solve part 1 with simple 2-dimensional array iteration to find local minimums.
For part 2 we can use BFS to find regions divided by 9's, just like an old friend Paint fills with color. Then sort by basin size in
descending order and multiply them.




