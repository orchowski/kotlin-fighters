import java.lang.Exception
import kotlin.random.Random

fun main() {
    FightersGame(
        listOf(
            Knight("1", 10),
            Elve  ("2", 10),
            Knight("3", 10),
            Elve  ("23", 10),
            Knight("4", 10),
            Knight("5", 10),
            Knight("6", 10),
            Knight("7", 10),
            Knight("8", 10),
        )
    ).startFight()
}


public class FightersGame(val fighters: List<Fighter>) {
    init {
        if(fighters.groupBy { it.id }.filter { it.value.size > 1 }.size > 0 ){
            throw Exception("id duplicate!!")
        }
    }
    fun getNextOne(forFighter: Fighter): Fighter? = fightersInOrder(forFighter).firstOrNull(Fighter::isAlive)
    fun getNextRandomOne(forFighter: Fighter): Fighter? =
        fightersInOrder(forFighter).sortedBy { Random.nextInt() }.firstOrNull(Fighter::isAlive)

    private fun fightersInOrder(fighter: Fighter): List<Fighter> {
        val fighterIndex = fighters.indexOf(fighter)
        return fighters.subList(if (fighters.size == fighterIndex) fighters.size else fighterIndex + 1, fighters.size)
            .plus(fighters.subList(0, fighterIndex))
    }

    fun startFight() {
        var fighter = fighters.first()
        while (fighters.filter { it.isAlive() }.size > 1) {
            fighter.hit(fighter.selectTarget(this))

            fighter = getNextOne(fighter) ?: break
        }
        println("fighter ${fighter.id} won!! // ${fighter.kind}")
    }
}

interface Fighter {
    val kind: String
    val id: String
    fun isAlive(): Boolean
    fun hit(fighter: Fighter?)
    fun selectTarget(fightersGame: FightersGame): Fighter?
    fun getHit(value: Int)
}

data class Knight(override val id: String, var hitPoints: Int) : Fighter {
    override val kind = "knight"
    init {
        hitPoints += 120
    }

    override fun isAlive(): Boolean = hitPoints > 0
    override fun hit(fighter: Fighter?){
        if (fighter == null) {
            return
        }
        val attackPoints = Random.nextInt(0, 6)
        println("$id: attacks fighter ${fighter.id} for $attackPoints points")
        fighter.getHit(attackPoints)
    }

    override fun selectTarget(fightersGame: FightersGame): Fighter? = fightersGame.getNextOne(this)

    override fun getHit(value: Int) {
        hitPoints -= value
        println("$id: hitpoints left $hitPoints")
    }
}

public data class Elve(override val id: String, var hitPoints: Int) : Fighter {
    override val kind = "elve"
    init {
        hitPoints += 50
    }

    override fun isAlive(): Boolean = hitPoints > 0
    override fun hit(fighter: Fighter?) {
        if (fighter == null) {
            return
        }
        val attackPoints = 25
        println("$id: attacks fighter ${fighter.id} for $attackPoints points")
        fighter.getHit(attackPoints)
    }

    override fun selectTarget(fightersGame: FightersGame): Fighter? = fightersGame.getNextRandomOne(this)

    override fun getHit(value: Int) {
        hitPoints -= value - 1
        println("$id: hitpoints left $hitPoints")
    }
}