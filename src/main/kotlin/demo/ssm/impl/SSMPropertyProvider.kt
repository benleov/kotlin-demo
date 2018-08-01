package demo.ssm.impl

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest
import demo.ssm.PropertyProvider

class SSMPropertyProvider(private val environment : String, region: String) : PropertyProvider {

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
