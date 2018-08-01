package demo.main

import demo.dynamo.DynamoDao
import demo.dynamo.DynamoRow
import demo.sqs.SQSDao
import demo.sqs.SQSMessage
import demo.ssm.impl.LocalPropertyProvider
import demo.ssm.impl.SSMPropertyProvider
import io.javalin.ApiBuilder.get
import io.javalin.ApiBuilder.post
import io.javalin.Javalin

fun main(args: Array<String>) {

    val environment = System.getenv("Environment") ?: throw RuntimeException("Environment cannot be null")

    val region = System.getenv("Region") ?: throw RuntimeException("Region cannot be null")

    var propertyProvider = if (environment == "local") LocalPropertyProvider() else SSMPropertyProvider(environment, System.getenv("Region"))

    val sqsDao = SQSDao(region, propertyProvider)
    val dynamoDao = DynamoDao(region)

    val app = Javalin.create().apply {
        port(7000)
        exception(Exception::class.java) { e, ctx -> e.printStackTrace() }
        error(404) { ctx -> ctx.json("not found") }
    }.start()

    app.routes {


        // publishes a message to sqs
        post("/sqs/publish") { ctx ->

            val message = ctx.bodyAsClass(SQSMessage::class.java)
            val sent = sqsDao.publishToSqs(message)
            ctx.status(if (sent) 200 else 400)
        }

        // returns the attributes of the specified queue
        get("/sqs/attributes/:queueName") { ctx ->

            val attributes = sqsDao
                    .getQueueAttributes(ctx.param("queueName").toString())

            ctx.json(attributes)
            ctx.status(200)
        }

        // creates a row in dynamo db
        post("/dynamodb/:tableName") { ctx ->

            val row = ctx.bodyAsClass(DynamoRow::class.java)
            val inserted = dynamoDao.insert(row)
            ctx.status(if (inserted) 200 else 400)
        }

    }

}
