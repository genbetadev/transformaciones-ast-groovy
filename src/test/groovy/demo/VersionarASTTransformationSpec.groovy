package demo

import static groovy.test.GroovyAssert.assertScript

import org.codehaus.groovy.control.MultipleCompilationErrorsException
import spock.lang.Specification

class VersionarASTTransformationSpec extends Specification {

    void 'No podemos anotar attributos'() {
        when:
            assertScript """
                class Foo {
                    @demo.Versionar
                    String bar
                }
            """

        then:
            def e = thrown RuntimeException
            e.message.contains('Sólo podemos usar @Versionar para anotar clases')
    }

    void 'No podemos anotar métodos'() {
        when:
            assertScript """
                class Foo {
                    @demo.Versionar
                    void bar() { }
                }
            """

        then:
            def e = thrown RuntimeException
            e.message.contains('Sólo podemos usar @Versionar para anotar clases')
    }

    void 'No añadimos el campo VERSION si ya existe'() {
        expect:
            assertScript """
                @demo.Versionar
                class Foo {
                    static final String VERSION = '0.1'
                }

                assert Foo.VERSION == '0.1'
            """
    }

    void 'Añadimos el campo VERSION porque no existe'() {
        expect:
            assertScript """
                @demo.Versionar('2.0')
                class Foo { }

                assert Foo.VERSION == '2.0'
            """
    }

    void 'Error de compilación porque el valor de la versión no es una constante'() {
        when:
            assertScript """
                @demo.Versionar(a)
                class Foo { }
            """

        then:
            def e = thrown MultipleCompilationErrorsException
            e.message.contains('Valor no válido para la anotación')
    }

}
