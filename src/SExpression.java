/**
 * @author gaurav
 *
 */
public class SExpression {
	int iValue;
	String sValue;
	TYPE type;
	SExpression left;
	SExpression right;

	/**
	 * Constructor for an Integer
	 * @param value
	 */
	public SExpression(int value) {
		iValue = value;
		type = TYPE.INT;
	}

	/**
	 * Constructor for a String literal 
	 * @param value
	 */
	public SExpression(String value) {
		sValue = value;
		type = TYPE.LITERAL;
	}

	/**
	 * Constructor for a compound expression
	 * @param left2
	 * @param right2
	 */
	public SExpression(SExpression left2, SExpression right2) {
		left = left2;
		right = right2;
		type = TYPE.COMPOUND;
	}
}
