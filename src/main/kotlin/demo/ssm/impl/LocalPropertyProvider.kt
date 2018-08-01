package demo.ssm.impl

import demo.ssm.PropertyProvider

class LocalPropertyProvider internal constructor() : PropertyProvider {


    override fun getProperty(name: String): String {
        return System.getenv(name)
    }
}
