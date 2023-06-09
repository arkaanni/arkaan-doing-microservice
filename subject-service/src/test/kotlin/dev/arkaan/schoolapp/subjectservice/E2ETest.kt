package dev.arkaan.schoolapp.subjectservice

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.ktor.test.dispatcher.*
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.DockerImageName
import kotlin.test.assertEquals


/**
 * Reference: https://gist.github.com/Stexxe/87b6a4985ceda80e1444208cb64826c4
 */
class E2ETest {

    companion object {
        private lateinit var server: TestApplicationEngine

        @ClassRule
        @JvmField
        val mysql = MySQLContainer(DockerImageName.parse("mysql:8.0.32")).apply {
            withInitScript("init_db.sql")
        }

        @BeforeClass
        @JvmStatic
        fun setUp() {
            server = TestApplicationEngine(createTestEnvironment {
                developmentMode = false
                config = MapApplicationConfig(
                    "db.host" to mysql.host,
                    "db.port" to mysql.firstMappedPort.toString(),
                    "db.user" to mysql.username,
                    "db.password" to mysql.password,
                    "db.database" to mysql.databaseName,
                    "db.driver" to "com.mysql.cj.jdbc.Driver"
                )
                module {
                    setUp()
                }
            })
            server.start(false)
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            server.stop()
            mysql.close()
        }
    }

    @Test
    fun `get all subjects`() = testSuspend {
        val response = server.client.get("/subject")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("[{\"subjectCode\":\"MK01\",\"name\":\"Mathematics\",\"description\":\"Mathematics for first year.\"},{\"subjectCode\":\"MK02\",\"name\":\"Biology\",\"description\":\"Biology\"}]", response.bodyAsText())
    }

    @Test
    fun `get subject by code`() = testSuspend {
        val code = "MK01"
        val response = server.client.get("/subject/$code")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("{\"subjectCode\":\"MK01\",\"name\":\"Mathematics\",\"description\":\"Mathematics for first year.\"}", response.bodyAsText())
    }
}