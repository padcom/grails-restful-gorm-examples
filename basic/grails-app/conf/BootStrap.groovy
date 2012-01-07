class BootStrap {

    def init = { servletContext ->
    	def person = new Person(firstName: "John", lastName: "Doe")
    	person.addToAddress(new Address(city: "New York", street: "13th"))
    	person.save(flush: true)
    }
    def destroy = {
    }
}
