package demo.main

import demo.sqs.SQSDao
import demo.sqs.SQSMessage
import demo.ssm.impl.LocalPropertyProvider
import demo.ssm.impl.SSMPropertyProvider
import io.javalin.ApiBuilder.post
import io.javalin.Javalin

fun main(args: Array<String>) {

    val environment = System.getenv("Environment") ?: throw RuntimeException("Environment cannot be null")

    val region = System.getenv("Region") ?: throw RuntimeException("Region cannot be null")

    var propertyProvider = if (environment == "local") LocalPropertyProvider() else SSMPropertyProvider(environment, System.getenv("Region"))

    val sqsDao = SQSDao(environment, region, propertyProvider)

    val app = Javalin.create().apply {
        port(7000)
        exception(Exception::class.java) { e, ctx -> e.printStackTrace() }
        error(404) { ctx -> ctx.json("not found") }
    }.start()

    app.routes {

//        get("/users") { ctx ->
//            ctx.json(sqsDao.users)
//        }
//
//        get("/users/:id") { ctx ->
//            ctx.json(sqsDao.findById(ctx.param("id")!!.toInt())!!)
//        }
//
//        get("/users/email/:email") { ctx ->
//            ctx.json(sqsDao.findByEmail(ctx.param("email")!!)!!)
//        }
//
//        post("/users/create") { ctx ->
//            val user = ctx.bodyAsClass(User::class.java)
//            sqsDao.save(name = user.name, email = user.email)
//            ctx.status(201)
//        }
//
//        patch("/users/update/:id") { ctx ->
//            val user = ctx.bodyAsClass(User::class.java)
//            sqsDao.update(
//                    id = ctx.param("id")!!.toInt(),
//                    user = user
//            )
//            ctx.status(204)
//        }

        post("/sqs/publish") { ctx ->

            val message = ctx.bodyAsClass(SQSMessage::class.java)
            val sent = sqsDao.publishToSqs(message)
            ctx.status(if (sent) 200 else 400)
        }

//        delete("/users/delete/:id") { ctx ->
//            sqsDao.delete(ctx.param("id")!!.toInt())
//            ctx.status(204)
//        }

    }

}
