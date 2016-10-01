package demo

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.syntax.SyntaxException
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class VersionarASTTransformation extends AbstractASTTransformation {

    private static final String VERSION = 'VERSION'

    @Override
    public void visit(final ASTNode[] nodes, final SourceUnit source) {

        // 1. Verificar tipo de los argumentos
        if (!(nodes[0] instanceof AnnotationNode) || !(nodes[1] instanceof ClassNode)) {
            throw new RuntimeException('Sólo podemos usar @Versionar para anotar clases')
        }

        // 2. Verificar que el trabajo no está hecho
        ClassNode classNode = (ClassNode) nodes[1]
        if (classNode.getField(VERSION)) {
            println 'Campo VERSION ya existe'
            return
        }

        // 3. Añadir el campo
        AnnotationNode annotation = (AnnotationNode)nodes[0]
        def version = annotation.getMember('value')
        if (version instanceof ConstantExpression) {
            classNode.addField(VERSION, ACC_PUBLIC | ACC_STATIC | ACC_FINAL, ClassHelper.STRING_TYPE, version)
        } else {
            source.addError(new SyntaxException('Valor no válido para la anotación', annotation.lineNumber, annotation.columnNumber))
        }
    }
}