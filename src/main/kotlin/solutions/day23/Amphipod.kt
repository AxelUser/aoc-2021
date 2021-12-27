package solutions.day23

import solutions.BaseSolution
import java.util.*
import java.util.Map.copyOf
import kotlin.math.abs

class Amphipod: BaseSolution() {
    override fun part1(input: String): Any {
        return solve(input.lines().parse(), 2)
    }

    override fun part2(input: String): Any {
        val state = input.lines().toMutableList().apply {
            addAll(3, listOf("  #D#C#B#A#", "  #D#B#A#C#"))
        }.parse()

        return solve(state, 4)
    }

    private data class Spot(val number: Int, val aboveRoomKind: Char? = null): Comparable<Spot> {
        val canBeOccupied = aboveRoomKind == null
        override fun compareTo(other: Spot) = number.compareTo(other.number)
        override fun toString() = number.toString()
    }

    private object HallwayMap {
        val roomSpots = mapOf(
            'A' to Spot(2, 'A'),
            'B' to Spot(4, 'B'),
            'C' to Spot(6, 'C'),
            'D' to Spot(8, 'D')
        )

        val spots = arrayListOf(
            Spot(0), Spot(1), roomSpots['A']!!,
            Spot(3), roomSpots['B']!!,
            Spot(5), roomSpots['C']!!,
            Spot(7), roomSpots['D']!!,
            Spot(9), Spot(10)
        )
    }

    private data class HallwayState(val occupiedSpots: Map<Spot, Char> = mapOf()) {
        fun leave(spot: Spot): HallwayState {
            return HallwayState(occupiedSpots.minus(spot))
        }

        fun occupy(spot: Spot, kind: Char): HallwayState {
            return if (spot.canBeOccupied) HallwayState(occupiedSpots.plus(spot to kind)) else error("can't occupy spot $spot")
        }

        fun getReachableHallwaySpotsFromRoom(kind: Char): List<Spot> {
            val roomSpot = HallwayMap.roomSpots[kind] ?: error("unexpected kind $kind")

            val reachableLeft = HallwayMap.spots.take(roomSpot.number).takeLastWhile { spot -> !occupiedSpots.containsKey(spot) }
            val reachableRight = HallwayMap.spots.drop(roomSpot.number + 1).takeWhile { spot -> !occupiedSpots.containsKey(spot) }

            return (reachableLeft + reachableRight).filter { it.canBeOccupied }
        }

        fun canReachHomeRoom(spot: Spot, kind: Char): Spot? {
            val roomSpot = HallwayMap.roomSpots[kind] ?: error("unexpected kind $kind")

            if (roomSpot < spot) {
                // search left
                if (HallwayMap.spots.take(spot.number).drop(roomSpot.number + 1).any { occupiedSpots.containsKey(it) }) return null
            } else if (roomSpot > spot) {
                // search right
                if (HallwayMap.spots.take(roomSpot.number).drop(spot.number + 1).any { occupiedSpots.containsKey(it) }) return null
            } else error("wrong position in hallway $spot")

            return roomSpot
        }

        override fun toString(): String {
            return HallwayMap.spots.map { if (occupiedSpots.containsKey(it)) occupiedSpots[it] else '-' }.joinToString("")
        }
    }

    private data class State(val hallway: HallwayState, val rooms: Map<Char, List<Char>>) {

        override fun toString(): String {
            val roomA = rooms['A']!!.joinToString("")
            val roomB = rooms['B']!!.joinToString("")
            val roomC = rooms['C']!!.joinToString("")
            val roomD = rooms['D']!!.joinToString("")

            return "${hallway},A($roomA),B($roomB),C($roomC),D($roomD)"
        }

        fun isCompleted(roomCapacity: Int): Boolean {
            if (!rooms['A']!!.allHome('A', roomCapacity)) return false
            if (!rooms['B']!!.allHome('B', roomCapacity)) return false
            if (!rooms['C']!!.allHome('C', roomCapacity)) return false
            if (!rooms['D']!!.allHome('D', roomCapacity)) return false

            return true
        }

        fun getNextStates(roomCapacity: Int): Sequence<CostedState> {
            return sequence {
                for ((spot, kind) in hallway.occupiedSpots) {
                    val reachableRoomSpot = hallway.canReachHomeRoom(spot, kind)
                    if (reachableRoomSpot != null && rooms[kind]!!.canGetIntoHomeRoom(kind, roomCapacity)) {
                        val energyToMove = countEnergyToMove(kind, abs(spot.number - reachableRoomSpot.number).toLong() +
                                rooms[kind]!!.movesToGetIntoRoom(roomCapacity))
                        val newState = State(hallway.leave(spot), copyOf(rooms).toMutableMap().apply { set(kind, listOf(kind) + rooms[kind]!!) })
                        yield(CostedState(energyToMove, newState))
                    }
                }

                yieldAll(statesAfterLeavingRoom('A', roomCapacity))
                yieldAll(statesAfterLeavingRoom('B', roomCapacity))
                yieldAll(statesAfterLeavingRoom('C', roomCapacity))
                yieldAll(statesAfterLeavingRoom('D', roomCapacity))
            }
        }

        private fun statesAfterLeavingRoom(roomKind: Char, roomCapacity: Int): Sequence<CostedState> {
            return sequence {
                val room = rooms[roomKind] ?: error("no such room $roomKind")
                if (room.canLeaveRoom(roomKind)) {
                    val spotsToMove = hallway.getReachableHallwaySpotsFromRoom(roomKind)
                    if (spotsToMove.isNotEmpty()) {
                        val whoIsLeaving = room[0]
                        val energyToLeaveRoom = countEnergyToMove(whoIsLeaving, room.movesToGetIntoHallway(roomCapacity))
                        for (spot in spotsToMove) {
                            val roomSpot = HallwayMap.roomSpots[roomKind]!!
                            val energyToGoIntoSpot = energyToLeaveRoom + countEnergyToMove(whoIsLeaving, abs(roomSpot.number - spot.number).toLong())
                            val newRoomState = copyOf(rooms).toMutableMap().apply { set(roomKind, room.drop(1)) }

                            yield(CostedState(energyToGoIntoSpot, State(hallway.occupy(spot, whoIsLeaving), newRoomState)))
                        }
                    }
                }
            }
        }
    }

    private data class CostedState(val energy: Long, val state: State)

    private fun solve(initial: State, roomCapacity: Int): Long {
        val pq = PriorityQueue<CostedState>(compareBy { it.energy })
        val visited = mutableSetOf<CostedState>()
        val energyCosts = mutableMapOf<State, Long>().withDefault { Long.MAX_VALUE }
        pq.offer(CostedState(0, initial))

        while (pq.isNotEmpty()) {
            val (totalEnergy, state) = pq.poll().also { visited.add(it) }

            state.getNextStates(roomCapacity).forEach { next ->
                if (!visited.contains(next)) {
                    val newTotal = next.energy + totalEnergy
                    if (newTotal < energyCosts.getValue(next.state)) {
                        energyCosts[next.state] = newTotal
                        pq.offer(CostedState(newTotal, next.state))
                    }
                }
            }
        }

        return energyCosts.keys.first { it.isCompleted(roomCapacity) }.let { energyCosts.getValue(it) }
    }

    private fun List<String>.parse(): State {
        val rows = filter { l -> l.any{ it.isLetter() } }.map { l -> l.filter { it.isLetter() } }
        val rooms = mutableMapOf(
            'A' to rows.map { it[0] },
            'B' to rows.map { it[1] },
            'C' to rows.map { it[2] },
            'D' to rows.map { it[3] },
        )

        return State(HallwayState(), rooms)
    }
}

private fun List<Char>.canLeaveRoom(room: Char) = any { it != room }
private fun List<Char>.allHome(room: Char, roomCapacity: Int) = count { it == room } == roomCapacity
private fun List<Char>.movesToGetIntoHallway(roomCapacity: Int) = roomCapacity - size + 1L
private fun List<Char>.movesToGetIntoRoom(roomCapacity: Int) = roomCapacity - size
private fun List<Char>.canGetIntoHomeRoom(kind: Char, roomCapacity: Int): Boolean {
    if (size == roomCapacity) return false
    if (any { it != kind }) return false
    return true
}

fun countEnergyToMove(kind: Char, moves: Long): Long = moves * when(kind) {
    'A' -> 1
    'B' -> 10
    'C' -> 100
    'D' -> 1000
    else -> error("unexpected amphipod kind $kind")
}

fun main() {
    Amphipod().runAll()
}