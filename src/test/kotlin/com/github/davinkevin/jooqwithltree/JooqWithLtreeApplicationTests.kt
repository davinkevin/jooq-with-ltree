package com.github.davinkevin.jooqwithltree

import com.github.davinkevin.jooqwithltree.database.Tables.TREE
import org.assertj.core.api.Assertions.assertThat
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Field
import org.jooq.impl.DSL.condition
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class JooqWithLtreeApplicationTests {

    @Autowired lateinit var query: DSLContext

    @Test
    fun `should read data from tree`() {
        val results = query.selectFrom(TREE).fetch()
        assertThat(results).hasSize(7)
    }

    @Test
    fun `should display type of path`() {
        query.selectFrom(TREE)
                .fetch()
                .forEach {
                    println(it.path)
                    println(it.path.javaClass)
                }
    }

    @Test
    fun `should find ancestor of A-C`() {
        /* Given */
        // select count(*) from tree where ‘A.C' @> path

        /* When */
        val condition = TREE.LETTER.eq("12")
        val result = query.selectFrom(TREE)
                .where(path("A.B' = 'A.B'); SELECT * FROM TREE; SELECT * FROM TREE WHERE ('A.B").isAncestorOf(TREE.PATH))
                .and(condition)
                .fetch()

        /* Then */
        assertThat(result).hasSize(2)
    }
}

private fun path(p: String): ltree = when {
    p.matches("[^;]*".toRegex()) -> ltree(p)
    else -> throw IllegalArgumentException()
}

private class ltree(val path: String) {
    fun isAncestorOf(v: Field<String>): Condition = condition("'$path' @> {0}", v)
}