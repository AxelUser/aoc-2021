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


