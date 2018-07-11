package demo.user.ssm

class LocalSsm internal constructor() : SsmProvider {


    override fun getProperty(name: String): String {

        return System.getProperty(name)
    }
}
