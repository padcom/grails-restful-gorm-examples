def person = new Person(firstName: "John", lastName: "Doe")
def address = new Address(city: new City(country: "USA", name: "New York").save(flush: true), street: "13th")
person.addToAddress(address)
return person.save(flush: true)
