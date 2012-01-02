class Person {
	Long id
	String firstName
	String lastName

	static expose = 'person'

	static hasMany = [ address: Address ]
}
