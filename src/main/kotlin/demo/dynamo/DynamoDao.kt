package demo.dynamo

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder
import com.amazonaws.services.dynamodbv2.model.AttributeValue

class DynamoDao(private val region: String) {


    fun insert(tableName: String, row: DynamoRow) : Boolean {

        val dynamoClient = AmazonDynamoDBAsyncClientBuilder
                .standard()
                .withRegion(region)
                .build()

        val values = row.values.mapValues { AttributeValue(it.value) }

        val result = dynamoClient.putItem(tableName, values);

        return true
    }



}
