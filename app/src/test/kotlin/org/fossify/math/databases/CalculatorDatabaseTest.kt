package org.fossify.math.databases

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.fossify.math.models.History
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class CalculatorDatabaseSingletonTest {

    private lateinit var context: Context
    private val testDbFileName = "test_calculator.db"   // 仅测试用的数据库文件

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        // 只删除测试数据库文件，不碰生产数据库文件
        deleteTestDatabaseFile()
        // 重置单例，确保每个测试独立（但不会删除生产数据库文件）
        CalculatorDatabase.destroyInstance()
    }

    @After
    fun tearDown() {
        // 测试结束后清理测试数据库文件，不影响生产
        deleteTestDatabaseFile()
        CalculatorDatabase.destroyInstance()
    }

    private fun deleteTestDatabaseFile() {
        val dbFile = context.getDatabasePath(testDbFileName)
        if (dbFile.exists()) dbFile.delete()
        context.getDatabasePath("$testDbFileName-wal").takeIf { it.exists() }?.delete()
        context.getDatabasePath("$testDbFileName-shm").takeIf { it.exists() }?.delete()
    }

    // ==================== 单例模式测试（使用生产数据库文件名，但不删除文件） ====================
    @Test
    fun getInstance_returnsSameInstanceForMultipleCalls() {
        val instance1 = CalculatorDatabase.getInstance(context)
        val instance2 = CalculatorDatabase.getInstance(context)
        assertSame("同一个实例应该返回", instance1, instance2)
    }

    @Test
    fun destroyInstance_clearsSingletonAndAllowsNewInstance() {
        val instance1 = CalculatorDatabase.getInstance(context)
        CalculatorDatabase.destroyInstance()
        val instance2 = CalculatorDatabase.getInstance(context)
        assertNotSame("销毁后应创建不同实例", instance1, instance2)
    }

    @Test
    fun getInstance_createsDatabaseFile() {
        val db = CalculatorDatabase.getInstance(context)
        db.openHelper.writableDatabase
        val dbFile = context.getDatabasePath("calculator.db")  // 生产文件名
        assertTrue("数据库文件应存在", dbFile.exists())
        // 注意：测试结束后不删除此文件，因为是生产文件（在模拟环境中可以保留，不影响真实设备）
    }

    @Test
    fun getInstance_isThreadSafe() {
        val threadsCount = 10
        val executor = Executors.newFixedThreadPool(threadsCount)
        val latch = CountDownLatch(threadsCount)
        val instances = mutableListOf<CalculatorDatabase?>()

        repeat(threadsCount) {
            executor.submit {
                try {
                    val instance = CalculatorDatabase.getInstance(context)
                    synchronized(instances) {
                        instances.add(instance)
                    }
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await(5, TimeUnit.SECONDS)
        executor.shutdown()

        assertNotNull("实例列表不应为空", instances.firstOrNull())
        val firstInstance = instances.first()
        instances.forEach { instance ->
            assertSame("所有线程获取的应是同一个实例", firstInstance, instance)
        }
    }

    @Test
    fun concurrentDestroyAndGet_shouldNotBreakSingleton() {
        val executor = Executors.newFixedThreadPool(8)
        val done = CountDownLatch(100)

        repeat(100) {
            executor.submit {
                repeat(10) {
                    if (it % 2 == 0) {
                        CalculatorDatabase.getInstance(context)
                    } else {
                        CalculatorDatabase.destroyInstance()
                    }
                }
                done.countDown()
            }
        }

        done.await(5, TimeUnit.SECONDS)
        executor.shutdown()

        val finalInstance = CalculatorDatabase.getInstance(context)
        assertNotNull("最终应能获取实例", finalInstance)
    }

    // ==================== DAO 功能测试（使用独立的测试数据库文件） ====================
    @Test
    fun testDatabaseOperations() {
        // 使用独立的测试数据库文件，绝对不影响生产数据库
        val db = Room.databaseBuilder(
            context.applicationContext,
            CalculatorDatabase::class.java,
            testDbFileName
        ).allowMainThreadQueries().build()

        val dao = db.CalculatorDao()
        val baseTime = System.currentTimeMillis()

        // 初始为空
        assertEquals(0, dao.getHistory().size)
        assertEquals(0, dao.getHistory(10).size)

        // 插入
        val history1 = History(id = null, formula = "2*3", result = "6", timestamp = baseTime)
        val id1 = dao.insertOrUpdate(history1)
        assertTrue(id1 != -1L)

        // 查询
        var all = dao.getHistory()
        assertEquals(1, all.size)
        assertEquals("2*3", all[0].formula)

        // 插入第二条（时间戳更大，应排前面）
        val history2 = History(id = null, formula = "10/2", result = "5", timestamp = baseTime + 1000)
        val id2 = dao.insertOrUpdate(history2)
        all = dao.getHistory(10)
        assertEquals(2, all.size)
        assertEquals("10/2", all[0].formula)

        // LIMIT
        val limited = dao.getHistory(1)
        assertEquals(1, limited.size)
        assertEquals("10/2", limited[0].formula)

        // 更新
        val updatedHistory = history2.copy(id = id2, result = "5.0")
        val updatedId = dao.insertOrUpdate(updatedHistory)
        assertEquals(id2, updatedId)
        val afterUpdate = dao.getHistory()
        assertEquals("5.0", afterUpdate.find { it.id == id2 }?.result)

        // 批量插入
        (1..30).forEach { i ->
            val h = History(id = null, formula = "$i+$i", result = "${i*2}", timestamp = baseTime + 2000 + i)
            dao.insertOrUpdate(h)
        }
        assertEquals(32, dao.getHistory(Int.MAX_VALUE).size)//getHistory默认limit为20
        val last20 = dao.getHistory()  // limit 20
        assertEquals(20, last20.size)
        assertEquals("30+30", last20.first().formula)

        // 删除所有
        dao.deleteHistory()
        assertEquals(0, dao.getHistory().size)

        db.close()
    }
}
