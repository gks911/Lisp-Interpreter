/**
 * 
 */

/**
 * @author gaurav
 *
 */
public class LispUtil {

	
	/**
	 * Method to print the parse tree
	 */
	public static void printTree(SExpression root) {
		if (isList(root) && !(root.type == TYPE.LITERAL && root.sValue.equals(Constants.NIL_TOKEN)))
			_printList(root);
		else
			_printSExpression(root);
	}

	private static void _printSExpression(SExpression node) {
		if (node.type == TYPE.INT)
			System.out.print(node.iValue);
		else if (node.type == TYPE.LITERAL)
			System.out.print(node.sValue);
		else {
			//compound SExpression
			System.out.print(Constants.OPEN_BRACKET);
			printTree(node.left);
			System.out.print(Constants.SPACED_DOT);
			printTree(node.right);
			System.out.print(Constants.CLOSED_BRACKET);
		}
	}

	private static void _printList(SExpression node) {
		System.out.print(Constants.OPEN_BRACKET);
		SExpression _tmp = node;
		while (_tmp.type == TYPE.COMPOUND) {
			if (_tmp.left.type == TYPE.INT || _tmp.left.type == TYPE.LITERAL) {
				System.out.print(" ");
				_printSExpression(_tmp.left);
				System.out.print(" ");
			} else printTree(_tmp.left);
			_tmp = _tmp.right;
		}
		System.out.print(") ");
	}

	/**
	 * Check if the tree rooted at 'node' is a list
	 * @return
	 */
	public static boolean isList(SExpression node) {
		if (node.type == TYPE.LITERAL && node.sValue.equals(Constants.NIL_TOKEN))
			return true;
		else if (node.type == TYPE.INT || node.type == TYPE.LITERAL)
			return false;
		else
			return isList(node.right);
	}
	
	public static boolean isIntAtom(SExpression node){
		if (node.type == TYPE.INT)
			return true;
		return false;
	}

	public static boolean isLiteralAtom(SExpression node){
		if (node.type == TYPE.LITERAL)
			return true;
		return false;
	}
	
	private static boolean checkInt(SExpression node){ return (node.type == TYPE.INT); }
	private static boolean checkLiteral(SExpression node){ return (node.type == TYPE.LITERAL); }
	
	public static SExpression plus(SExpression node1, SExpression node2) throws LispException{
		if(checkInt(node1) && checkInt(node2)){
			return new SExpression(node1.iValue + node2.iValue);
		}
		throw new LispException("ERROR: Type mismatch. Expected 'INT'");
	}
	
	public static SExpression minus(SExpression node1, SExpression node2) throws LispException{
		if(checkInt(node1) && checkInt(node2)){
			return new SExpression(node1.iValue - node2.iValue);
		}
		throw new LispException("ERROR: Type mismatch. Expected 'INT'");
	}
	
	public static SExpression times(SExpression node1, SExpression node2) throws LispException{
		if(checkInt(node1) && checkInt(node2)){
			return new SExpression(node1.iValue * node2.iValue);
		}
		throw new LispException("ERROR: Type mismatch. Expected 'INT'");
	}
	
	public static SExpression quotient(SExpression node1, SExpression node2) throws LispException{
		if(checkInt(node1) && checkInt(node2)){
			return new SExpression(node1.iValue / node2.iValue);
		}
		throw new LispException("ERROR: Type mismatch. Expected 'INT'");
	}
	
	public static SExpression remainder(SExpression node1, SExpression node2) throws LispException{
		if(checkInt(node1) && checkInt(node2)){
			return new SExpression(node1.iValue % node2.iValue);
		}
		throw new LispException("ERROR: Type mismatch. Expected 'INT'");
	}
	
	public static boolean equal(SExpression node1, SExpression node2) throws LispException{
		if(checkInt(node1) && checkInt(node2)){
			return (node1.iValue == node2.iValue);
		}else if(checkLiteral(node1) && checkLiteral(node2)){
			return (node1.sValue.equals(node2.sValue));
		}
		throw new LispException("ERROR: Type mismatch. Expected same type of arguments for 'EQUALS'");
	}
	
	public static boolean greater(SExpression node1, SExpression node2) throws LispException{
		if(checkInt(node1) && checkInt(node2)){
			return (node1.iValue > node2.iValue);
		}
		throw new LispException("ERROR: Type mismatch. Expected 'INT'");
	}
	
	public static boolean less(SExpression node1, SExpression node2) throws LispException{
		if(checkInt(node1) && checkInt(node2)){
			return (node1.iValue < node2.iValue);
		}
		throw new LispException("ERROR: Type mismatch. Expected 'INT'");
	}
	
	public static SExpression car(SExpression node) throws LispException{
		if(node.type == TYPE.COMPOUND)
			return node.left;
		throw new LispException("ERROR: 'CAR' undefined on non-compound SExpressions");
	}
	
	public static SExpression cdr(SExpression node) throws LispException{
		if(node.type == TYPE.COMPOUND)
			return node.right;
		throw new LispException("ERROR: 'CDR' undefined on non-compound SExpressions");
	}
	
	public static SExpression cons(SExpression node1, SExpression node2) throws LispException{
		return new SExpression(node1, node2);
	}

	public static boolean isNil(SExpression expr) {
		if(expr.type == TYPE.LITERAL && expr.sValue.equals(Constants.NIL_TOKEN))
			return true;
		return false;
	}

	public static boolean isValidName(SExpression expr) {
		if(Constants.RESERVED_TOKENS.contains(expr.sValue))
			return true;
		return false;
	}
}
