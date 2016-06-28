package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Extract.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.utils.Utils.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.utils.*;

import org.eclipse.jdt.core.dom.*;

/**
 * A {@link Wring} that eliminates redundant comparison with the two boolean
 * literals: <code><b>true</b></code> and <code><b>false</b></code>.
 *
 * @author Yossi Gil
 * @since 2015-07-17
 */
public final class InfixComparisonBooleanLiteral extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.Simplify {
  private static BooleanLiteral literal(final InfixExpression e) {
    return asBooleanLiteral(core(literalOnLeft(e) ? left(e) : right(e)));
  }
  private static boolean negating(final InfixExpression e, final BooleanLiteral l) {
    return l.booleanValue() != (e.getOperator() == EQUALS);
  }
  private static Expression nonLiteral(final InfixExpression e) {
    return literalOnLeft(e) ? right(e) : left(e);
  }
  private static boolean literalOnLeft(final InfixExpression e) {
    return Is.booleanLiteral(core(left(e)));
  }
  private static boolean literalOnRight(final InfixExpression e) {
    return Is.booleanLiteral(core(right(e)));
  }
  @Override public final boolean scopeIncludes(final InfixExpression e) {
    return !e.hasExtendedOperands() && in(e.getOperator(), EQUALS, NOT_EQUALS) && (literalOnLeft(e) || literalOnRight(e));
  }
  @Override String description(final InfixExpression e) {
    return "Eliminate redundant comparison with '" + literal(e) + "'";
  }
  @Override Expression replacement(final InfixExpression e) {
    final BooleanLiteral literal = literal(e);
    final Expression nonliteral = core(nonLiteral(e));
    return new Plant(!negating(e, literal) ? nonliteral : logicalNot(nonliteral)).into(e.getParent());
  }
}