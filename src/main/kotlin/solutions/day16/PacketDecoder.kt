package solutions.day16

import solutions.BaseSolution

const val VERSION_LENGTH = 3L
const val TYPE_LENGTH = 3L

const val SUBPACKET_BITS_LENGTH = 15L
const val SUBPACKET_COUNT_LENGTH = 11L

class PacketDecoder : BaseSolution() {
    override fun part1(input: String): Any {
        return ArrayDeque(listOf(ArrayDeque(input.mapHexadecimalToBinary()).parsePacket())).let {
            var result = 0L
            while (it.isNotEmpty()) {
                val packet = it.removeFirst()
                result += packet.version
                if (packet is Operator) {
                    it.addAll(packet.subPackets)
                }
            }
            result
        }
    }

    override fun part2(input: String): Any {
        return ArrayDeque(input.mapHexadecimalToBinary()).parsePacket().value
    }

    private fun String.mapHexadecimalToBinary(): List<Char> = flatMap {
        it.digitToInt(16)
            .toString(2)
            .padStart(4, '0')
            .toList()
    }

    private interface Packet {
        val version: Int
        val value: Long
        val lengthInBits: Long
    }

    private data class Literal(override val version: Int, val data: String) : Packet {
        override val value: Long
            get() = data.toLong(2)
        override val lengthInBits: Long
            get() = VERSION_LENGTH + TYPE_LENGTH + (data.length / 4) + data.length
    }

    private data class Operator(
        override val version: Int,
        val lengthType: Int,
        val subPackets: List<Packet>,
        val valueCalc: (List<Packet>) -> Long
    ) : Packet {
        override val value: Long
            get() = valueCalc(subPackets)

        override val lengthInBits: Long
            get() = VERSION_LENGTH + TYPE_LENGTH + 1 + subPackets.sumOf { it.lengthInBits } + if (lengthType == 0) SUBPACKET_BITS_LENGTH else SUBPACKET_COUNT_LENGTH
    }

    private fun getOperatorCalculator(typeId: Int): (List<Packet>) -> Long {
        return when (typeId) {
            0 -> { subPackets: List<Packet> -> subPackets.sumOf { it.value } }
            1 -> { subPackets: List<Packet> -> subPackets.map { it.value }.reduce { acc, value -> acc * value } }
            2 -> { subPackets: List<Packet> -> subPackets.minOf { it.value } }
            3 -> { subPackets: List<Packet> -> subPackets.maxOf { it.value } }
            5 -> { subPackets: List<Packet> -> if (subPackets[0].value > subPackets[1].value) 1 else 0 }
            6 -> { subPackets: List<Packet> -> if (subPackets[0].value < subPackets[1].value) 1 else 0 }
            7 -> { subPackets: List<Packet> -> if (subPackets[0].value == subPackets[1].value) 1 else 0 }
            else -> error("Unrecognised operator type $typeId")
        }
    }

    private fun ArrayDeque<Char>.dequeN(n: Long): String = (0 until n).map { removeFirst() }.joinToString("")

    private fun ArrayDeque<Char>.parsePacket(): Packet {
        val version = dequeN(VERSION_LENGTH).toInt(2)
        return when (val type = dequeN(TYPE_LENGTH).toInt(2)) {
            4 -> {
                val value = StringBuilder()
                while (true) {
                    val group = dequeN(5)
                    value.append(group.takeLast(4))
                    if (group.first() == '0') break
                }
                Literal(version, value.toString())
            }
            else -> {
                when (val lengthType = dequeN(1).toInt(2)) {
                    0 -> {
                        val totalSubPacketBits = dequeN(SUBPACKET_BITS_LENGTH).toInt(2)
                        val subPackets = mutableListOf<Packet>()
                        var parsedBits = 0L
                        while (parsedBits < totalSubPacketBits) {
                            val packet = parsePacket()
                            subPackets.add(packet)
                            parsedBits += packet.lengthInBits
                        }
                        Operator(version, lengthType, subPackets, getOperatorCalculator(type))
                    }
                    else -> {
                        val totalSubPackets = dequeN(SUBPACKET_COUNT_LENGTH).toInt(2)
                        val subPackets = (0 until totalSubPackets).map { parsePacket() }
                        Operator(version, lengthType, subPackets, getOperatorCalculator(type))
                    }
                }
            }
        }
    }
}

fun main() {
    PacketDecoder().runAll()
}