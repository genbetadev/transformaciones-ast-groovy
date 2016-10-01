// Para ejecutar:
//  ./gradlew build
//  groovy -cp build/libs/add-version-1.0.jar EjemploASTLocal.groovy

@demo.Versionar('2.0')
class Foo {
}

println "La versión de Foo es: ${Foo.VERSION}"
