import java.util.Arrays;
import java.util.HashSet;

/**
 * 
 */

/**
 * @author gaurav Frequently used constants TODO: Will probably add all the
 *         Keywords (such as CONS, CDR, CAR PLUS etc.) here later
 */
public class Constants {

	public static final Character OPEN_BRACKET = '(';
	public static final Character CLOSED_BRACKET = ')';
	public static final Character DOT = '.';
	public static final String SPACED_DOT = " . ";
	public static final Character SPACE = ' ';
	public static final Character TAB_SPACE = '\t';
	public static final Character NEW_LINE = '\n';
	public static final String NIL_TOKEN = "NIL";
	
	public static final String CAR = "CAR";
	public static final String CDR = "CDR";
	public static final String CONS = "CONS";
	public static final String QUOTE = "QUOTE";
	public static final String ATOM = "ATOM";
	public static final String DEFUN = "DEFUN";
	public static final String PLUS = "PLUS";
	public static final String MINUS = "MINUS";
	public static final String TIMES = "TIMES";
	public static final String QUOTIENT = "QUOTIENT";
	public static final String REMAINDER = "REMAINDER";
	public static final String EQ = "EQ";
	public static final String LESS = "LESS";
	public static final String GREATER = "GREATER";
	public static final String COND = "COND";
	public static final String NULL = "NULL";
	public static final String TRUE = "T";
	public static final String INT = "INT";
	
	public static final HashSet<String> RESERVED_TOKENS = new HashSet<String>(Arrays.asList(CAR,CDR,CONS,QUOTE,ATOM,DEFUN,PLUS,MINUS,TIMES,QUOTIENT,REMAINDER,EQ,LESS,GREATER,COND,NULL,TRUE,NIL_TOKEN));
}
