# Day 1
As usual, AoC begins with easy tasks, so everybody is warming up and feel happy for little achievements.

Yor are receiving signals and should count how many times measurements are increasing. In first part you check 
just `N`th and `N-1`th elements, but for the second part you deal with whole sliding window of size 3. However, I love that
task, because part 1 can be solved as counting increasing sliding window of size 1.

First I've implemented that sliding window
by myself, but then I've got acquainted with function [`windowed`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/windowed.html) 
from Kotlin and solution became even more simple and nice.

# Day 2
Now you're controlling a submarine, that has 3 commands: `up`, `down` and `forward`. After each command there's a number
of units of that action. Feels like a small program itself. Result should be the multiplication of horizontal position and
depth after last command. Part 1 and 2 has different effect on those numbers for each command.

Actually the solution is trivial: iterate over commands and accumulate several values based on the rules. The easiest first
part boils down to the fact that you need to compute sums of units per command and compute final value. When I've been implementing
in that way I've learned cool [`groupingBy`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/grouping-by.html) function,
that creates groping iterator instead of materializing map.