package demo.user

import com.amazonaws.metrics.AwsSdkMetrics
import java.util.concurrent.atomic.AtomicInteger
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import demo.user.ssm.AwsSsm
import demo.user.ssm.LocalSsm
import demo.user.ssm.SsmParameters

class UserDao {

    // "Initialize" with a few users
    // This demonstrates type inference, map-literals and named parameters
    val users = hashMapOf(
            0 to User(name = "Alice", email = "alice@alice.kt", id = 0),
            1 to User(name = "Bob", email = "bob@bob.kt", id = 1),
            2 to User(name = "Carol", email = "carol@carol.kt", id = 2),
            3 to User(name = "Dave", email = "dave@dave.kt", id = 3)
    )

    var lastId: AtomicInteger = AtomicInteger(users.size - 1)

    fun save(name: String, email: String) {
        val id = lastId.incrementAndGet()
        users.put(id, User(name = name, email = email, id = id))
    }

    fun findById(id: Int): User? {
        return users[id]
    }

    fun findByEmail(email: String): User? {
        return users.values.find { it.email == email } // == is equivalent to java .equals() (referential equality is checked by ===)
    }

    fun update(id: Int, user: User) {
        users.put(id, User(name = user.name, email = user.email, id = id))
    }

    fun publishToSqs(message : Message) {

        val environment = System.getenv("Environment")
        val region = System.getenv("Region") ?: "local"

        val ssmProvider = if(environment.isNullOrEmpty() || environment == "local") LocalSsm() else AwsSsm(environment, region)
        val queueName = ssmProvider.getProperty(SsmParameters.SQS_QUEUE_NAME)

        val sqsClient = AmazonSQSClientBuilder
                .standard()
                .withRegion(region)
                .build()

        val request = sqsClient.listQueues(queueName)

        request.queueUrls.forEach {
            sqsClient.sendMessage(it, message.message)
        }

    }


    fun delete(id: Int) {
        users.remove(id)
    }

}
