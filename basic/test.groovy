import grails.converters.*
import org.grails.plugins.rest.url.*

def url = ctx.restfulUrlParser.parse("/person/1/address/1/city")
def root = ctx.restfulDomainClassResolver.resolve(url[0].name)

def hql = " from ${root.naturalName} a "
def select = "select a"
def join = ""
def where = ""
def aliases = 'abcdefghijklmnopqrstuvwxyz'
def lastAliasIndex = 0

def c = root
def lastIsCollection = true
def lastIdentifier = url[0].name

for (int i = 1; i < url.size(); i++) {
    if (url[i] instanceof PropertyElement) {
        def f = c.properties.find { it.name == url[i].name }
        if (f?.referencedDomainClass) {
            join += "inner join ${aliases[lastAliasIndex++]}.${url[i].name} ${aliases[lastAliasIndex]} "
            select = "select ${aliases[lastAliasIndex]}"
            lastIsCollection = Collection.isAssignableFrom(f?.type)
        } else {
            select += ".${url[i].name}"
            lastIsCollection = false
        }
        lastIdentifier = url[i].name
        c = f.referencedDomainClass
    } else if (url[i] instanceof IdentifierElement) {
        if (where != "") where += "and "
        where += "${aliases[lastAliasIndex]}.id = ${url[i].id} "
        lastIsCollection = false
    }
}

hql = select + hql + join + (where == "" ? "" : "where " + where)

def result = root.clazz.executeQuery(hql, [ cache: false ])
if (!lastIsCollection) result = result.size() > 0 ? result = result[0] : null

println ([ (lastIdentifier): result ] as JSON)
