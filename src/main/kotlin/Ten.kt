fun main() {
    println(a())
    println(b())
}

private val adapters: List<Int> by lazy { input.lines().map { it.toInt() } }
private val deviceJoltage = adapters.max()!! + 3
private val allAdapters = adapters + listOf(deviceJoltage)
private val adapterGaps = generateGaps()

private fun a(): Int {
    val gapsOfOne = adapterGaps.count { it == 1 }
    val gapsOfThree = adapterGaps.count { it == 3 }
    return gapsOfOne * gapsOfThree
}

private fun b(): Long {
    val groups = calculateAdjacentSections(adapterGaps)
    return countPermutations(groups)
}

private fun generateGaps(): List<Int> {
    val joltages = mutableListOf(0)
    var joltage = 0
    while (joltage < deviceJoltage) {
        joltage = allAdapters.filter { it in joltage+1..joltage+3 }.min()!!
        joltages.add(joltage)
    }
    return joltages.sorted().zipWithNext { a, b -> b-a }
}

private fun calculateAdjacentSections(gaps: List<Int>): List<List<Int>> {
    val groups = mutableListOf<List<Int>>()

    var remainingGaps = gaps
    while (remainingGaps.isNotEmpty()) {
        remainingGaps = remainingGaps.dropWhile { it == 3 }
        val group = remainingGaps.takeWhile { it != 3 }
        groups.add(group)
        remainingGaps = remainingGaps.drop(group.size)
    }

    return groups.filter { it.isNotEmpty() }
}

private fun countPermutations(groups: List<List<Int>>) = groups.map { permutationCountForGroup(it) }.product()

private fun permutationCountForGroup(group: List<Int>) =
        when (group.size) {
            1 -> 1
            2 -> 2
            3 -> 4
            4 -> 7
            else -> error("Group size too big")
        }

private const val input = """67
118
90
41
105
24
137
129
124
15
59
91
94
60
108
63
112
48
62
125
68
126
131
4
1
44
77
115
75
89
7
3
82
28
97
130
104
54
40
80
76
19
136
31
98
110
133
84
2
51
18
70
12
120
47
66
27
39
109
61
34
121
38
96
30
83
69
13
81
37
119
55
20
87
95
29
88
111
45
46
14
11
8
74
101
73
56
132
23"""
