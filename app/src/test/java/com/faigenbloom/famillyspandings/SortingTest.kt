package com.faigenbloom.famillyspandings

import com.faigenbloom.famillyspandings.comon.Countable
import com.faigenbloom.famillyspandings.comon.PlateSizeType
import com.faigenbloom.famillyspandings.comon.PlatesSorter
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

typealias SpendingsMock = com.faigenbloom.famillyspandings.spandings.Mock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SortingTest {
    val sorter = PlatesSorter<Countable>()
    val arrayOfSpendings: List<SpendingEntity> = emptyList()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `check sorter init`() {
        sorter shouldNotBe null
    }

    @Test
    fun `sorter can recieve array`() = runTest {
        sorter.findPattern(sorter.preparePlatesSizes(arrayOfSpendings)) shouldBe null
    }

    @Test
    fun `sorter works with empty list`() = runTest {
        sorter.preparePlatesSizes(emptyList()) shouldBe hashMapOf()
    }

    @Test
    fun `sorter can prepare Single Array Size`() = runTest {
        val plates = sorter.preparePlatesSizes(
            listOf(
                TestCountable(100),
            ),
        )
        plates shouldContain Pair(TestCountable(100), PlateSizeType.SIZE_ONE_BY_ONE)
    }

    @Test
    fun `sorter can prepare Array with two elements Sizes`() = runTest {
        val plates = sorter.preparePlatesSizes(
            listOf(
                TestCountable(100),
                TestCountable(200),
            ),
        )
        plates shouldContain Pair(TestCountable(100), PlateSizeType.SIZE_ONE_BY_ONE)
        plates shouldContain Pair(TestCountable(200), PlateSizeType.SIZE_TWO_BY_ONE)
    }

    @Test
    fun `sorter can prepare Array with two elements Not arranged properly`() = runTest {
        val plates = sorter.preparePlatesSizes(
            listOf(
                TestCountable(200),
                TestCountable(100),
            ),
        )
        plates shouldContain Pair(TestCountable(100), PlateSizeType.SIZE_ONE_BY_ONE)
        plates shouldContain Pair(TestCountable(200), PlateSizeType.SIZE_TWO_BY_ONE)
    }

    @Test
    fun `sorter can prepare Array with close elements Sizes`() = runTest {
        val plates = sorter.preparePlatesSizes(
            listOf(
                TestCountable(100),
                TestCountable(130),
            ),
        )
        plates shouldContain Pair(TestCountable(100), PlateSizeType.SIZE_ONE_BY_ONE)
        plates shouldContain Pair(TestCountable(130), PlateSizeType.SIZE_ONE_BY_ONE)
    }

    @Test
    fun `sorter can prepare Array with elements With too big of a difference between Sizes`() =
        runTest {
            val plates = sorter.preparePlatesSizes(
                listOf(
                    TestCountable(100),
                    TestCountable(300),
                ),
            )

            plates shouldContain Pair(TestCountable(100), PlateSizeType.SIZE_ONE_BY_ONE)
            plates shouldContain Pair(TestCountable(300), PlateSizeType.SIZE_THREE_BY_ONE)
        }

    @Test
    fun `sorter can prepare Array with three elements Sizes`() = runTest {
        val plates = sorter.preparePlatesSizes(
            listOf(
                TestCountable(100),
                TestCountable(200),
                TestCountable(300),
            ),
        )

        plates shouldContain Pair(TestCountable(100), PlateSizeType.SIZE_ONE_BY_ONE)
        plates shouldContain Pair(TestCountable(200), PlateSizeType.SIZE_TWO_BY_ONE)
        plates shouldContain Pair(TestCountable(300), PlateSizeType.SIZE_THREE_BY_ONE)
    }

    @Test
    fun `sorter can prepare Array with three elements With close Sizes`() = runTest {
        val plates = sorter.preparePlatesSizes(
            listOf(
                TestCountable(100),
                TestCountable(200),
                TestCountable(220),
            ),
        )

        plates shouldContain Pair(TestCountable(100), PlateSizeType.SIZE_ONE_BY_ONE)
        plates shouldContain Pair(TestCountable(200), PlateSizeType.SIZE_TWO_BY_ONE)
        plates shouldContain Pair(TestCountable(220), PlateSizeType.SIZE_TWO_BY_ONE)
    }

    @Test
    fun `sorter can prepare Array with elements Without loosing proper delta between Sizes`() =
        runTest {
            val plates = sorter.preparePlatesSizes(
                listOf(
                    TestCountable(100),
                    TestCountable(120),
                    TestCountable(200),
                ),
            )

            plates shouldContain Pair(TestCountable(100), PlateSizeType.SIZE_ONE_BY_ONE)
            plates shouldContain Pair(TestCountable(120), PlateSizeType.SIZE_ONE_BY_ONE)
            plates shouldContain Pair(TestCountable(200), PlateSizeType.SIZE_TWO_BY_ONE)
        }

    @Test
    fun `sorter can prepare Array with elements With too small and too big difference in Sizes`() =
        runTest {
            val plates = sorter.preparePlatesSizes(
                listOf(
                    TestCountable(100),
                    TestCountable(120),
                    TestCountable(300),
                ),
            )

            plates shouldContain Pair(TestCountable(100), PlateSizeType.SIZE_ONE_BY_ONE)
            plates shouldContain Pair(TestCountable(120), PlateSizeType.SIZE_ONE_BY_ONE)
            plates shouldContain Pair(TestCountable(200), PlateSizeType.SIZE_THREE_BY_ONE)
        }

    @Test
    fun `sorter can prepare finalTestCase Sizes`() =
        runTest {
            val testCaseList = arrayListOf<TestCountable>()
            for (i in 100..1000) {
                TestCountable(i.toLong())
            }
            val plates = sorter.preparePlatesSizes(testCaseList)
            for (i in 100..1000) {
                var plateSizeType: PlateSizeType = PlateSizeType.SIZE_ONE_BY_ONE
                if (i < 200) {
                    PlateSizeType.SIZE_ONE_BY_ONE
                } else if (i < 300) {
                    PlateSizeType.SIZE_TWO_BY_ONE
                } else if (i < 400) {
                    PlateSizeType.SIZE_THREE_BY_ONE
                } else if (i < 600) {
                    PlateSizeType.SIZE_TWO_BY_TWO
                } else if (i < 900) {
                    PlateSizeType.SIZE_THREE_BY_TWO
                } else {
                    PlateSizeType.SIZE_THREE_BY_THREE
                }

                plates shouldContain Pair(TestCountable(i.toLong()), plateSizeType)
            }
        }

    @Test
    fun `sorter can find single ONE BY ONE pattern`() =
        runTest {
            val patterns = sorter.findPattern(
                sorter.preparePlatesSizes(
                    listOf(
                        TestCountable(100),
                    ),
                ),
            )
            patterns[0].plates[0] shouldBe PlateSizeType.SIZE_ONE_BY_ONE
            patterns[0].items[0] shouldBe TestCountable(100)
        }

    @Test
    fun `sorter can find TWO BY ONE with ONE BY ONE pattern`() =
        runTest {
            val patterns = sorter.findPattern(
                sorter.preparePlatesSizes(
                    listOf(
                        TestCountable(200),
                        TestCountable(100),
                    ),
                ),
            )
            patterns[0].plates[0] shouldBe PlateSizeType.SIZE_TWO_BY_ONE
            patterns[0].items shouldContain TestCountable(200)
            patterns[0].plates[1] shouldBe PlateSizeType.SIZE_ONE_BY_ONE
            patterns[0].items shouldContain TestCountable(100)
        }

    @Test
    fun `sorter can find TWO sdfaBY ONE with ONE BY ONE pattern`() =
        runTest {
            val patterns = sorter.findPattern(
                sorter.preparePlatesSizes(
                    listOf(
                        SpendingsMock.spendingsList[9],
                        SpendingsMock.spendingsList[10],
                        SpendingsMock.spendingsList[11],
                        SpendingsMock.spendingsList[12],
                        SpendingsMock.spendingsList[13],
                    ),
                ),
            )
            patterns[0].plates[0] shouldBe PlateSizeType.SIZE_THREE_BY_ONE
            patterns[0].plates[1] shouldBe PlateSizeType.SIZE_THREE_BY_ONE
            patterns[0].plates[2] shouldBe PlateSizeType.SIZE_TWO_BY_ONE
            patterns[0].plates[3] shouldBe PlateSizeType.SIZE_ONE_BY_ONE
            patterns[1].plates[0] shouldBe PlateSizeType.SIZE_TWO_BY_ONE
            patterns[0].items shouldContain SpendingsMock.spendingsList[9]
            patterns[0].items shouldContain SpendingsMock.spendingsList[10]
            patterns[0].items shouldContain SpendingsMock.spendingsList[11]
            patterns[0].items shouldContain SpendingsMock.spendingsList[12]
            patterns[1].items shouldContain SpendingsMock.spendingsList[13]
        }

    @Test
    fun `sorter can find TWO - ONE BY ONE pattern`() =
        runTest {
            val patterns = sorter.findPattern(
                sorter.preparePlatesSizes(
                    listOf(
                        TestCountable(100),
                        TestCountable(120),
                    ),
                ),
            )
            patterns[0].plates[0] shouldBe PlateSizeType.SIZE_ONE_BY_ONE
            patterns[0].items shouldContain TestCountable(100)
            patterns[0].plates[1] shouldBe PlateSizeType.SIZE_ONE_BY_ONE
            patterns[0].items shouldContain TestCountable(120)
        }

    @Test
    fun `sorter can find THREE - ONE BY ONE pattern`() =
        runTest {
            val patterns = sorter.findPattern(
                sorter.preparePlatesSizes(
                    listOf(
                        TestCountable(100),
                        TestCountable(120),
                        TestCountable(130),
                    ),
                ),
            )
            patterns[0].plates[0] shouldBe PlateSizeType.SIZE_ONE_BY_ONE
            patterns[0].plates[1] shouldBe PlateSizeType.SIZE_ONE_BY_ONE
            patterns[0].plates[2] shouldBe PlateSizeType.SIZE_ONE_BY_ONE
            patterns[0].items shouldContainOnly listOf(
                TestCountable(100),
                TestCountable(120),
                TestCountable(130),
            )
        }

    @Test
    fun `sorter can duplicate THREE - ONE BY ONE pattern`() =
        runTest {
            val patterns = sorter.findPattern(
                sorter.preparePlatesSizes(
                    listOf(
                        TestCountable(101),
                        TestCountable(102),
                        TestCountable(103),
                        TestCountable(104),
                        TestCountable(105),
                        TestCountable(106),
                    ),
                ).apply {
                    size shouldBe 6
                },
            )

            patterns.size shouldBe 2
            patterns[0].plates[0] shouldBe PlateSizeType.SIZE_ONE_BY_ONE
            patterns[0].plates[1] shouldBe PlateSizeType.SIZE_ONE_BY_ONE
            patterns[0].plates[2] shouldBe PlateSizeType.SIZE_ONE_BY_ONE
            patterns[1].plates[0] shouldBe PlateSizeType.SIZE_ONE_BY_ONE
            patterns[1].plates[1] shouldBe PlateSizeType.SIZE_ONE_BY_ONE
            patterns[1].plates[2] shouldBe PlateSizeType.SIZE_ONE_BY_ONE

            patterns[0].items shouldContainOnly listOf(
                TestCountable(106),
                TestCountable(105),
                TestCountable(104),
            )
            patterns[1].items shouldContainOnly listOf(
                TestCountable(103),
                TestCountable(102),
                TestCountable(101),
            )
        }

    @Test
    fun `sorter can find TWO PATTERNS - 2x1+1x1 AND  1x1`() =
        runTest {
            val patterns = sorter.findPattern(
                sorter.preparePlatesSizes(
                    listOf(
                        TestCountable(100),
                        TestCountable(120),
                        TestCountable(200),
                    ),
                ),
            )
            patterns[0].plates[0] shouldBe PlateSizeType.SIZE_TWO_BY_ONE
            patterns[0].items shouldContain TestCountable(200)
            patterns[0].plates[1] shouldBe PlateSizeType.SIZE_ONE_BY_ONE
            patterns[0].items shouldContain TestCountable(120)
            patterns[1].plates[0] shouldBe PlateSizeType.SIZE_ONE_BY_ONE
            patterns[1].items shouldContain TestCountable(100)
        }

    @Test
    fun `sorter can sort by dates`() =
        runTest {
            val listByDate = sorter.prepareByDates(
                SpendingsMock.spendingsList,
            )
            listByDate.size shouldBe 12
            listByDate[0].size shouldBe 6
        }
}

data class TestCountable(val value: Long) : Countable {
    override fun getSortableValue(): Long {
        return value
    }

    override fun getSortableDate(): Int {
        return 0
    }
}
