package demo.user.ssm

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest

class AwsSsm(private val environment : String, region: String) : SsmProvider {

    private val client = AWSSimpleSystemsManagementClientBuilder
            .standard()
            .withRegion(region)
            .build();

    /**
     * Gets SSM parameter. Convention is /environment/<parameter_name>
     */
    override fun getProperty(name: String): String {

        return client.getParameter(GetParameterRequest()
                .withName("/$environment/$name")).parameter.value.toString()
    }
}
